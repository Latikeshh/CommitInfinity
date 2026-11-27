# CommitInfinity ♾️

![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/latikeshh/CommitInfinity/auto-commit.yml?branch=main&style=flat-square)
![License](https://img.shields.io/github/license/latikeshh/CommitInfinity?style=flat-square)
![Automated Commit Interval](https://img.shields.io/badge/interval-every%205%20mins-brightgreen?style=flat-square)

**CommitInfinity** is an automated GitHub Actions workflow designed to maintain seamless, continuous repository activity. It runs automatically on a scheduled interval (every 5 minutes) or on-demand via manual trigger, generating lightweight empty commits without cluttering project files or codebase history.

---

## 📌 Repository Information & Remote Setup

- **Owner / Username**: `latikeshh`
- **Repository Name**: `CommitInfinity`
- **Remote Origin URL**: `https://github.com/latikeshh/CommitInfinity.git`

### Quick Setup / Remote URL Configuration

To set or update your repository's origin remote URL:

```bash
git remote set-url origin https://github.com/latikeshh/CommitInfinity.git
```

Verify your current remote configuration:

```bash
git remote -v
```

---

## ✨ Features

- **Automated Scheduling**: Runs every 5 minutes using GitHub Actions Cron (`*/5 * * * *`).
- **Manual Trigger**: Supports `workflow_dispatch` for instant manual executions anytime.
- **Clean Commits**: Employs `git commit --allow-empty` to keep your project file tree clean without introducing redundant files or code diffs.
- **UTC Timestamped Messages**: Each commit carries a UTC timestamp tag (e.g., `CommitInfinity: automated commit YYYY-MM-DD HH:MM:SS UTC`).
- **Concurrency Protection**: Avoids race conditions by configuring concurrency group constraints.

---

## ⚙️ How It Works

The workflow is defined under `.github/workflows/auto-commit.yml`:

```yaml
name: CommitInfinity - Auto Commit

on:
  schedule:
    - cron: '*/5 * * * *'
  workflow_dispatch:

permissions:
  contents: write

concurrency:
  group: commit-infinity-auto
  cancel-in-progress: false

jobs:
  auto-commit:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout repository
        uses: actions/checkout@v4
        with:
          fetch-depth: 0

      - name: Configure Git
        run: |
          git config user.name "CommitInfinity Bot"
          git config user.email "41898282+github-actions[bot]@users.noreply.github.com"

      - name: Create empty commit
        run: |
          git commit --allow-empty \
            -m "CommitInfinity: automated commit $(date -u '+%Y-%m-%d %H:%M:%S UTC')"

      - name: Push commit
        run: git push origin HEAD:${GITHUB_REF_NAME}
```

---

## 🚀 Setup & Requirements

### 1. Enable GitHub Actions Permissions
Ensure that your GitHub repository allows Actions to write to the repository:
1. Navigate to **Settings** > **Actions** > **General**.
2. Scroll down to **Workflow permissions**.
3. Select **Read and write permissions**.
4. Check **Allow GitHub Actions to create and approve pull requests** (optional, recommended).
5. Click **Save**.

### 2. Adjusting Commit Frequency
To change the cron schedule, update the `schedule` block in `.github/workflows/auto-commit.yml`:

| Frequency | Cron Expression |
| :--- | :--- |
| Every 5 minutes | `*/5 * * * *` |
| Every hour | `0 * * * *` |
| Every 6 hours | `0 */6 * * *` |
| Daily at midnight UTC | `0 0 * * *` |

---

## 🛡️ Security & Community Health

Please refer to our community guidelines:
- [SECURITY.md](SECURITY.md) for security policy and reporting vulnerabilities.
- [CONTRIBUTING.md](CONTRIBUTING.md) for contribution guidelines.
- [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) for community standards.
- [CHANGELOG.md](CHANGELOG.md) for version release notes.

---

## 📄 License

This project is licensed under the [MIT License](LICENSE.md).
