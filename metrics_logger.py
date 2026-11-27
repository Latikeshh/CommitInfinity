#!/usr/bin/env python3
"""
CommitInfinity Metrics & Activity Logger - Repository Statistics Generator
Generates real activity data and updates repository statistics.
"""

import json
import os
from datetime import datetime, timezone

DATA_DIR = "data"
LOG_FILE = os.path.join(DATA_DIR, "activity_log.json")
STATS_FILE = "STATS.md"

def ensure_data_dir():
    if not os.path.exists(DATA_DIR):
        os.makedirs(DATA_DIR)

def update_activity_log():
    ensure_data_dir()
    now_utc = datetime.now(timezone.utc).isoformat()

    log_data = {"last_updated": now_utc, "total_records": 0, "history": []}

    if os.path.exists(LOG_FILE):
        try:
            with open(LOG_FILE, "r", encoding="utf-8") as f:
                log_data = json.load(f)
        except Exception:
            pass

    log_data["total_records"] = log_data.get("total_records", 0) + 1
    log_data["last_updated"] = now_utc

    # Keep last 50 execution timestamps
    history = log_data.get("history", [])
    history.append({"timestamp": now_utc, "run_id": log_data["total_records"]})
    log_data["history"] = history[-50:]

    with open(LOG_FILE, "w", encoding="utf-8") as f:
        json.dump(log_data, f, indent=2)

    return log_data

def update_stats_markdown(log_data):
    content = f"""# CommitInfinity Repository Metrics 📊

Automatically generated activity metrics to track repository history and execution health.

---

### 📈 Current Summary
* **Last Activity Sync**: `{log_data['last_updated']}`
* **Total Logged Runs**: `{log_data['total_records']}`
* **Status**: Operational ✅

---

### 🕒 Recent Execution Logs (UTC)
| Run ID | Timestamp | Status |
| :--- | :--- | :--- |
"""
    for item in reversed(log_data.get("history", [])):
        content += f"| #{item['run_id']} | `{item['timestamp']}` | Success ✅ |\n"

    with open(STATS_FILE, "w", encoding="utf-8") as f:
        f.write(content)

if __name__ == "__main__":
    data = update_activity_log()
    update_stats_markdown(data)
    print(f"[CommitInfinity] Activity log updated. Total runs: {data['total_records']}")
