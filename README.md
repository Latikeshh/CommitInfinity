<div align="center">

# CommitInfinity ♾️

[![Repository Health](https://github.com/Latikeshh/CommitInfinity/actions/workflows/repo-health.yml/badge.svg)](https://github.com/Latikeshh/CommitInfinity/actions)
[![License](https://img.shields.io/github/license/Latikeshh/CommitInfinity?style=flat-square)](LICENSE.md)
[![Version](https://img.shields.io/badge/Version-2.0.0-blue?style=flat-square)](CHANGELOG.md)

**CommitInfinity** is an experimental Git automation and repository history management toolkit.

</div>

---

## 📋 Overview

CommitInfinity provides a structured set of local automation utilities for experimenting with Git repository history, batch commit workflows, and dynamic metrics logging.

Designed as a local development tool, CommitInfinity allows developers to explore Git automation, test repository indexing, and manage batch commit operations safely from their workstation terminal.

---

## ✨ Features

* 🛠️ **Local Batch Commit Generator**: Configurable local script (`commit_script.ps1` & `commit_script.sh`) supporting batch generation, custom message formatting, and start/end range control.
* 🔍 **Execution Preview Mode**: Interactively displays execution parameters (active branch, commit count, target range, push mode) before committing.
* 🛡️ **Safety & Validation Rules**: Verifies Git repository status, active branch, and working tree before execution; prevents accidental automatic pushing.
* 📊 **Dynamic Metrics Logger**: Python utility (`metrics_logger.py`) that calculates genuine repository metrics directly from Git history.
* 💻 **Local Device Execution**: Automation scripts and local logs remain safely stored on your local device and are untracked on remote GitHub repositories via `.gitignore`.

---

## 🛠️ Technology Stack

| Component | Technology |
| :--- | :--- |
| **Automation Scripts** | PowerShell 7+, Bash |
| **Metrics Engine** | Python 3.8+ |
| **VCS / Engine** | Git |
| **CI / CD Validation** | GitHub Actions |

---

## 🚀 Quick Start & Usage

### 1. Prerequisites
Ensure you have the following installed on your system:
- **Git** (`git --version`)
- **PowerShell** (Windows) or **Bash** (Linux/macOS)
- **Python 3** (`python --version`)

### 2. Local Execution (Windows PowerShell)

Run the local execution script from your project directory:

```powershell
# Interactive execution with preview (Default: 10 local commits)
.\commit_script.ps1

# Custom commit count and batch size
.\commit_script.ps1 -Count 50 -BatchSize 10

# Express execution with explicit remote push
.\commit_script.ps1 -Count 20 -Push -Force
```

### 3. Local Execution (Linux / macOS Bash)

```bash
# Make script executable
chmod +x commit_script.sh

# Run interactive script (Default: 10 commits)
./commit_script.sh 10 50 false
```

---

## ⚙️ Configuration & Parameters

| Parameter | Type | Default | Description |
| :--- | :--- | :--- | :--- |
| `-Count` | Integer | `10` | Number of local commits to generate. |
| `-BatchSize` | Integer | `50` | Interval size for progress logging. |
| `-Push` | Switch | `False` | Explicitly push commits to remote repository upon completion. |
| `-Force` | Switch | `False` | Bypass interactive confirmation prompt. |
| `-MessageSuffix` | String | `"experimental record"` | Custom suffix for commit messages. |

---

## 🛡️ Safety & Limitations

* **Manual Push Default**: Local commit generation never automatically pushes to remote repositories unless the `-Push` switch is explicitly provided.
* **No History Rewriting**: The toolkit operates strictly via additive Git commits. It does not perform force pushes, rebase operations, or destructive history rewrites.
* **Local Device Isolation**: Execution scripts, local counter files (`counter.txt`), and activity logs (`data/`) remain stored on your local device and are ignored by Git remote via `.gitignore`.

---

## 📄 Documentation & Policies

* [STATS.md](STATS.md) — Dynamic repository statistics and metrics summary.
* [CHANGELOG.md](CHANGELOG.md) — Version release notes and update history.
* [SECURITY.md](SECURITY.md) — Security policies and vulnerability reporting procedures.
* [CONTRIBUTING.md](CONTRIBUTING.md) — Guidelines for contributing to CommitInfinity.
* [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) — Community covenant and behavior standards.
* [LICENSE.md](LICENSE.md) — Open source license terms (MIT).

---

## 📜 License

This project is open source under the terms of the [MIT License](LICENSE.md).
