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

  let value, unit;
  if (years > 0) {
    value = -years; unit = "year";
  } else if (months > 0) {
    value = -months; unit = "month";
  } else if (weeks > 0) {
    value = -weeks; unit = "week";
  } else if (days > 0) {
    value = -days; unit = "day";
  } else if (hours > 0) {
    value = -hours; unit = "hour";
  } else if (minutes > 0) {
    value = -minutes; unit = "minute";
  } else {
    value = -seconds; unit = "second";
  }

  const rtf = new Intl.RelativeTimeFormat('en', { numeric: "auto" });
  return rtf.format(value, unit);
}

function updateLastUpdated() {
  const el = document.getElementById("lastupdated");
  if (el) {
    const isoTime = el.getAttribute("data-updated-at");
    const relative = formatRelativeTime(isoTime);
    el.textContent = "Last updated " + relative;
  }
}

// Make function globally available
window.updateLastUpdated = updateLastUpdated;

if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', updateLastUpdated);
} else {
  updateLastUpdated();
}

document.addEventListener('htmx:afterSwap', updateLastUpdated);
