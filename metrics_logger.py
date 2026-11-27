#!/usr/bin/env python3
"""
CommitInfinity Advanced Live Dashboard & Metrics Generator
Generates live statistics, progress bars, badges, and updates STATS.md.
"""

import json
import os
import subprocess
from datetime import datetime, timezone

DATA_DIR = "data"
LOG_FILE = os.path.join(DATA_DIR, "activity_log.json")
STATS_FILE = "STATS.md"
TARGET_COMMITS = 100000

def get_git_commit_count():
    try:
        res = subprocess.run(["git", "rev-list", "--count", "HEAD"], capture_output=True, text=True, check=True)
        return int(res.stdout.strip())
    except Exception:
        return 0

def generate_progress_bar(current, target, length=25):
    percent = min(1.0, max(0.0, current / target))
    filled = int(round(length * percent))
    bar = "█" * filled + "░" * (length - filled)
    return f"`[{bar}] {percent * 100:.2f}%`"

def update_activity_log(commit_count):
    if not os.path.exists(DATA_DIR):
        os.makedirs(DATA_DIR)

    now_utc = datetime.now(timezone.utc).strftime("%Y-%m-%d %H:%M:%S UTC")
    log_data = {"last_updated": now_utc, "total_records": 0, "history": []}

    if os.path.exists(LOG_FILE):
        try:
            with open(LOG_FILE, "r", encoding="utf-8") as f:
                log_data = json.load(f)
        except Exception:
            pass

    log_data["total_records"] = log_data.get("total_records", 0) + 1
    log_data["last_updated"] = now_utc
    log_data["current_commits"] = commit_count

    history = log_data.get("history", [])
    history.append({"timestamp": now_utc, "commit_count": commit_count, "run_id": log_data["total_records"]})
    log_data["history"] = history[-50:]

    with open(LOG_FILE, "w", encoding="utf-8") as f:
        json.dump(log_data, f, indent=2)

    return log_data

def update_stats_markdown(log_data, commit_count):
    progress_bar = generate_progress_bar(commit_count, TARGET_COMMITS, length=25)
    percentage = (commit_count / TARGET_COMMITS) * 100
    remaining = max(0, TARGET_COMMITS - commit_count)

    content = f"""# ⚡ CommitInfinity Live Performance Dashboard ♾️

<div align="center">

![Commits Counter](https://img.shields.io/badge/Total_Commits-{commit_count:,}-brightgreen?style=for-the-badge&logo=git&logoColor=white)
![Progress](https://img.shields.io/badge/Progress-{percentage:.1f}%25-blue?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Active_Automation-success?style=for-the-badge)
![Target](https://img.shields.io/badge/Goal-100%2C000_Commits-orange?style=for-the-badge)

</div>

---

### 📊 Real-Time Progress Metric

| Metric | Live Value |
| :--- | :--- |
| 🚀 **Current Commit Count** | **`{commit_count:,}`** |
| 🎯 **Target Milestone** | **`{TARGET_COMMITS:,}`** |
| 📉 **Remaining Commits** | **`{remaining:,}`** |
| 📈 **Completion Percentage** | **`{percentage:.2f}%`** |
| ⏳ **Last Sync (UTC)** | `{log_data['last_updated']}` |

#### 🔄 Live Progress Bar
{progress_bar}

---

### 📜 Execution Audit Log (Recent Syncs)

| Run ID | UTC Timestamp | Commits at Sync | System Status |
| :---: | :---: | :---: | :---: |
"""
    for item in reversed(log_data.get("history", [])):
        content += f"| #{item['run_id']} | `{item['timestamp']}` | `{item.get('commit_count', 0):,}` | Operational ✅ |\n"

    with open(STATS_FILE, "w", encoding="utf-8") as f:
        f.write(content)

if __name__ == "__main__":
    count = get_git_commit_count()
    data = update_activity_log(count)
    update_stats_markdown(data, count)
    print(f"[CommitInfinity] Dashboard updated successfully! Current commits: {count:,}")
