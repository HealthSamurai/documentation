function formatRelativeTime(date) {
  const now = new Date();
  const then = new Date(date);
  const diffMs = now - then;
  const seconds = Math.floor(diffMs / 1000);
  const minutes = Math.floor(seconds / 60);
  const hours = Math.floor(minutes / 60);
  const days = Math.floor(hours / 24);
  const weeks = Math.floor(days / 7);
  const months = Math.floor(days / 30);
  const years = Math.floor(days / 365);

  if (years > 0) {
    return years === 1 ? "a year ago" : years + " years ago";
  } else if (months > 0) {
    return months === 1 ? "a month ago" : months + " months ago";
  } else if (weeks > 0) {
    return weeks === 1 ? "a week ago" : weeks + " weeks ago";
  } else if (days > 0) {
    return days === 1 ? "yesterday" : days + " days ago";
  } else if (hours > 0) {
    return hours === 1 ? "an hour ago" : hours + " hours ago";
  } else if (minutes > 0) {
    return minutes === 1 ? "a minute ago" : minutes + " minutes ago";
  } else {
    return "just now";
  }
}

function updateLastUpdated() {
  const el = document.getElementById("lastupdated");
  if (el) {
    const isoTime = el.getAttribute("data-updated-at");
    if (isoTime) {
      const relative = formatRelativeTime(isoTime);
      el.textContent = "Last updated " + relative;
    }
  }
}

// Update on page load
document.addEventListener('DOMContentLoaded', updateLastUpdated);

// Update every minute to keep relative time fresh
setInterval(updateLastUpdated, 60000);