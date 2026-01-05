#!/usr/bin/env python3
"""
Poll Comentario SQLite database for new comments and send notifications to Zulip.

This script:
1. Queries the Comentario SQLite database for recent comments (last 20 minutes)
2. Sends new comments to Zulip via REST API
"""

import os
import sys
import sqlite3
import urllib.parse
import urllib.request
import base64
from datetime import datetime, timezone, timedelta
from pathlib import Path


# Configuration from environment variables
DB_PATH = os.getenv("DB_PATH", "/comentario-data/comentario.db")
COMENTARIO_DOMAIN_ID = os.getenv("COMENTARIO_DOMAIN_ID", "")
COMENTARIO_BASE_URL = os.getenv("COMENTARIO_BASE_URL", "https://www.health-samurai.io/docs/futureblog/comentario")
ZULIP_URL = os.getenv("ZULIP_URL", "")
ZULIP_BOT_EMAIL = os.getenv("ZULIP_BOT_EMAIL", "")
ZULIP_BOT_TOKEN = os.getenv("ZULIP_BOT_TOKEN", "")
ZULIP_STREAM_ID = "43"  # Hardcoded stream ID for blog comments
LOOKBACK_MINUTES = int(os.getenv("LOOKBACK_MINUTES", "20"))  # Check last 20 minutes by default


def fetch_recent_comments():
    """Fetch recent comments from SQLite database."""
    if not COMENTARIO_DOMAIN_ID:
        print("Error: COMENTARIO_DOMAIN_ID not set", file=sys.stderr)
        sys.exit(1)

    if not Path(DB_PATH).exists():
        print(f"Error: Database not found at {DB_PATH}", file=sys.stderr)
        sys.exit(1)

    print(f"Connecting to database: {DB_PATH}")

    # Calculate lookback time
    lookback_time = datetime.now(timezone.utc) - timedelta(minutes=LOOKBACK_MINUTES)
    print(f"Looking for comments since: {lookback_time.isoformat()}")

    try:
        conn = sqlite3.connect(f"file:{DB_PATH}?mode=ro", uri=True)
        conn.row_factory = sqlite3.Row
        cursor = conn.cursor()

        # Query recent comments with user and page info
        # Join cm_comments + cm_users + cm_domain_pages
        query = """
            SELECT
                c.id as comment_id,
                c.markdown,
                c.html,
                c.ts_created,
                c.page_id,
                u.email as author_email,
                u.name as author_name,
                u.federated_idp as provider,
                u.signup_country as country,
                u.website_url as author_link,
                u.id as user_id,
                p.path as page_path,
                p.title as page_title,
                p.domain_id
            FROM cm_comments c
            LEFT JOIN cm_users u ON c.user_created = u.id
            LEFT JOIN cm_domain_pages p ON c.page_id = p.id
            WHERE p.domain_id = ?
              AND c.ts_created > ?
              AND c.is_approved = 1
            ORDER BY c.ts_created ASC
        """

        # Convert lookback_time to SQLite timestamp format
        lookback_str = lookback_time.strftime('%Y-%m-%d %H:%M:%S')

        cursor.execute(query, (COMENTARIO_DOMAIN_ID, lookback_str))
        rows = cursor.fetchall()

        comments = []
        for row in rows:
            comment = {
                'comment_id': row['comment_id'],
                'markdown': row['markdown'],
                'html': row['html'],
                'ts_created': row['ts_created'],
                'page_path': row['page_path'] or '',
                'page_title': row['page_title'] or 'Unknown page',
                'author_email': row['author_email'] or '',
                'author_name': row['author_name'] or 'Anonymous',
                'provider': row['provider'] or '',
                'country': row['country'] or '',
                'author_link': row['author_link'] or '',
                'user_id': row['user_id'] or ''
            }
            comments.append(comment)

        cursor.close()
        conn.close()

        print(f"Retrieved {len(comments)} new comments from database")
        return comments

    except sqlite3.Error as e:
        print(f"Error: SQLite error: {e}", file=sys.stderr)
        sys.exit(1)
    except Exception as e:
        print(f"Error: Failed to fetch comments: {e}", file=sys.stderr)
        sys.exit(1)


def get_profile_link(author_link):
    """Return profile link if user provided website_url."""
    # Only use website_url if provided by user
    # OAuth providers (Google, GitHub, LinkedIn) don't expose public profile URLs
    return author_link if author_link else None


def format_zulip_message(comment):
    """Format a Comentario comment for Zulip notification."""
    comment_id = comment['comment_id']
    text = comment['markdown'] or comment['html'] or ''
    created_at = comment['ts_created']

    # Author info
    author = comment['author_name']
    author_email = comment['author_email']
    provider = comment['provider']
    author_link = comment['author_link']
    country = comment['country']

    # Page info
    page_title = comment['page_title']
    page_path = comment['page_path']

    # Construct comment URL
    # Comentario uses #comment-{uuid} anchors
    comment_url = f"{COMENTARIO_BASE_URL}{page_path}#comment-{comment_id}"

    # Normalize provider name for display
    provider_display = {
        "github": "GitHub",
        "google": "Google",
        "linkedin": "LinkedIn",
        "oidc": "OIDC",
        "facebook": "Facebook"
    }.get(provider.lower() if provider else "", provider or "Local")

    # Get profile link
    profile_link = get_profile_link(author_link)

    # Format message in Markdown (Zulip supports markdown)
    message_parts = [
        "**New blog comment**",
        "",
        f"**Page**: {page_title}",
        f"**Author**: {author} ({provider_display})"
    ]

    # Add profile link if available
    if profile_link:
        message_parts.append(f"**Profile**: {profile_link}")

    # Add email
    if author_email:
        message_parts.append(f"**Email**: {author_email}")

    # Add country if available
    if country:
        message_parts.append(f"**Country**: {country}")

    # Add comment link and time
    message_parts.extend([
        f"**Comment link**: {comment_url}",
        f"**Time**: {created_at}",
        "",
        "**Comment**:",
        text
    ])

    return "\n".join(message_parts)


def send_to_zulip(message):
    """Send formatted message to Zulip via REST API."""
    if not ZULIP_URL or not ZULIP_BOT_EMAIL or not ZULIP_BOT_TOKEN:
        print("Error: Zulip credentials not set", file=sys.stderr)
        return False

    # Create Basic Auth header
    auth_string = f"{ZULIP_BOT_EMAIL}:{ZULIP_BOT_TOKEN}"
    encoded_auth = base64.b64encode(auth_string.encode()).decode()

    # Build form data (application/x-www-form-urlencoded)
    form_data = {
        "type": "stream",
        "to": ZULIP_STREAM_ID,
        "topic": "blog comments",
        "content": message
    }
    encoded_data = urllib.parse.urlencode(form_data).encode()

    # Send request
    url = f"{ZULIP_URL}/api/v1/messages"

    try:
        req = urllib.request.Request(
            url,
            data=encoded_data,
            headers={
                "Authorization": f"Basic {encoded_auth}",
                "Content-Type": "application/x-www-form-urlencoded"
            }
        )

        with urllib.request.urlopen(req, timeout=10) as response:
            if response.status == 200:
                print(f"✓ Sent notification to Zulip")
                return True
            else:
                print(f"Warning: Zulip returned status {response.status}", file=sys.stderr)
                return False
    except Exception as e:
        print(f"Error: Failed to send to Zulip: {e}", file=sys.stderr)
        return False


def main():
    """Main polling logic."""
    print("=" * 60)
    print("Comentario to Zulip Poller (SQLite)")
    print("=" * 60)

    print(f"Domain ID: {COMENTARIO_DOMAIN_ID}")
    print(f"Lookback window: {LOOKBACK_MINUTES} minutes")

    # Fetch recent comments from database
    recent_comments = fetch_recent_comments()

    # Send notifications for new comments
    if recent_comments:
        print(f"Processing {len(recent_comments)} recent comments...")
        success_count = 0

        for comment in recent_comments:
            message = format_zulip_message(comment)
            if send_to_zulip(message):
                success_count += 1

        print(f"Successfully sent {success_count}/{len(recent_comments)} notifications")
    else:
        print("No new comments to process")

    print("=" * 60)
    print("Polling complete")
    print("=" * 60)


if __name__ == "__main__":
    main()
