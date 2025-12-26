#!/usr/bin/env python3
"""
Poll Comentario API for new comments and send notifications to Zulip.

This script:
1. Reads the last check timestamp from a state file
2. Polls the Comentario API for all comments
3. Filters comments created since the last check
4. Sends new comments to Zulip via REST API
5. Updates the state file with the new timestamp
"""

import os
import sys
import json
import urllib.parse
import urllib.request
import base64
from datetime import datetime, timezone
from pathlib import Path


# Configuration from environment variables
COMENTARIO_URL = os.getenv("COMENTARIO_URL", "http://comentario.gitbok.svc.cluster.local")
COMENTARIO_DOMAIN_ID = os.getenv("COMENTARIO_DOMAIN_ID", "")
ZULIP_URL = os.getenv("ZULIP_URL", "")
ZULIP_BOT_EMAIL = os.getenv("ZULIP_BOT_EMAIL", "")
ZULIP_BOT_TOKEN = os.getenv("ZULIP_BOT_TOKEN", "")
ZULIP_STREAM_ID = "43"  # Hardcoded stream ID for blog comments
STATE_FILE = Path(os.getenv("STATE_FILE", "/data/last_check.txt"))


def read_last_check_time():
    """Read the last check timestamp from state file."""
    if STATE_FILE.exists():
        try:
            content = STATE_FILE.read_text().strip()
            return datetime.fromisoformat(content.replace("Z", "+00:00"))
        except Exception as e:
            print(f"Warning: Failed to read last check time: {e}", file=sys.stderr)
            return datetime.now(timezone.utc).replace(hour=0, minute=0, second=0, microsecond=0)
    else:
        return datetime.now(timezone.utc).replace(hour=0, minute=0, second=0, microsecond=0)


def write_last_check_time(timestamp):
    """Write the last check timestamp to state file."""
    try:
        STATE_FILE.parent.mkdir(parents=True, exist_ok=True)
        STATE_FILE.write_text(timestamp.isoformat())
        print(f"Updated last check time to: {timestamp.isoformat()}")
    except Exception as e:
        print(f"Error: Failed to write last check time: {e}", file=sys.stderr)
        sys.exit(1)


def fetch_comments():
    """Fetch all comments from Comentario API."""
    if not COMENTARIO_DOMAIN_ID:
        print("Error: COMENTARIO_DOMAIN_ID not set", file=sys.stderr)
        sys.exit(1)

    # Build API URL with query parameters
    params = {
        "domainId": COMENTARIO_DOMAIN_ID,
        "sortBy": "created",
        "sortDesc": "true",
        "pageNumber": "1"
    }
    url = f"{COMENTARIO_URL}/api/comments?{urllib.parse.urlencode(params)}"

    print(f"Fetching comments from: {url}")

    try:
        req = urllib.request.Request(url)
        req.add_header("Content-Type", "application/json")

        with urllib.request.urlopen(req, timeout=30) as response:
            data = json.loads(response.read().decode())
            comments = data.get("comments", [])
            print(f"Retrieved {len(comments)} comments from Comentario")
            return comments
    except urllib.error.HTTPError as e:
        print(f"Error: HTTP {e.code} fetching comments: {e.reason}", file=sys.stderr)
        sys.exit(1)
    except Exception as e:
        print(f"Error: Failed to fetch comments: {e}", file=sys.stderr)
        sys.exit(1)


def filter_new_comments(comments, last_check_time):
    """Filter comments created after last check time."""
    new_comments = []

    for comment in comments:
        # Parse comment creation time (ISO 8601 format)
        created_at_str = comment.get("createdAt", "")
        if not created_at_str:
            continue

        try:
            created_at = datetime.fromisoformat(created_at_str.replace("Z", "+00:00"))

            if created_at > last_check_time:
                new_comments.append(comment)
        except Exception as e:
            print(f"Warning: Failed to parse timestamp '{created_at_str}': {e}", file=sys.stderr)
            continue

    print(f"Found {len(new_comments)} new comments since {last_check_time.isoformat()}")
    return new_comments


def get_profile_link(provider, author_id, author_link):
    """Generate profile link based on OAuth provider."""
    if author_link:
        return author_link

    if not author_id:
        return None

    # Generate profile link from provider and ID
    if provider == "github":
        return f"https://github.com/{author_id}"
    elif provider == "google":
        # Google doesn't have public profile URLs by user ID
        return None
    elif provider == "linkedin":
        # LinkedIn profile links are in the author_link field
        return None

    return None


def format_zulip_message(comment):
    """Format a Comentario comment for Zulip notification."""
    comment_id = comment.get("commentId", "")
    text = comment.get("markdown", "") or comment.get("html", "") or ""
    created_at = comment.get("createdAt", "")

    # Author info
    author = comment.get("commenterName", "Anonymous")
    author_email = comment.get("commenterEmail", "")

    # OAuth provider info (can be: github, google, linkedin, oidc, etc)
    provider = comment.get("commenterProvider", comment.get("provider", ""))
    author_link = comment.get("commenterLink", comment.get("link", ""))
    author_id = comment.get("commenterId", comment.get("authorId", ""))

    # Location info
    country = comment.get("country", comment.get("location", ""))

    # Page info
    page = comment.get("page", {})
    page_title = page.get("title", "Unknown page")
    page_path = page.get("path", "")

    # Construct comment URL
    # Note: Comentario doesn't provide direct comment links in API
    # We construct it based on domain origin and page path
    comment_url = f"{COMENTARIO_URL}{page_path}#comment-{comment_id}"

    # Normalize provider name for display
    provider_display = {
        "github": "GitHub",
        "google": "Google",
        "linkedin": "LinkedIn",
        "oidc": "OIDC",
        "facebook": "Facebook"
    }.get(provider.lower() if provider else "", provider or "Email")

    # Get profile link
    profile_link = get_profile_link(provider.lower() if provider else "", author_id, author_link)

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
    print("Comentario to Zulip Poller")
    print("=" * 60)

    # Read last check time
    last_check_time = read_last_check_time()
    print(f"Last check time: {last_check_time.isoformat()}")

    # Fetch all comments
    all_comments = fetch_comments()

    # Filter new comments
    new_comments = filter_new_comments(all_comments, last_check_time)

    # Send notifications for new comments
    if new_comments:
        print(f"Processing {len(new_comments)} new comments...")
        success_count = 0

        for comment in reversed(new_comments):  # Send oldest first
            message = format_zulip_message(comment)
            if send_to_zulip(message):
                success_count += 1

        print(f"Successfully sent {success_count}/{len(new_comments)} notifications")
    else:
        print("No new comments to process")

    # Update last check time to now
    current_time = datetime.now(timezone.utc)
    write_last_check_time(current_time)

    print("=" * 60)
    print("Polling complete")
    print("=" * 60)


if __name__ == "__main__":
    main()
