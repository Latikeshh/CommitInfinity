# Changelog

All notable changes to the **CommitInfinity** project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [2.0.0] - 2026-11-27

### Changed & Refactored
- **Project Repositioning**: Redefined project core identity as an experimental Git automation and repository history management toolkit.
- **Device-Only Local Scripts**: Configured `.gitignore` to keep execution scripts (`commit_script.ps1`, `commit_script.sh`, `metrics_logger.py`) and data logs (`counter.txt`, `data/`) on local device only.
- **Local Automation Script**: Added execution preview mode, Git repository and active branch validation, parameter controls, interactive confirmation prompts, and manual push defaults.
- **Workflow Audit**: Removed high-frequency 5-minute auto-commit cron workflow (`auto-commit.yml`). Added repository health and linting workflow (`repo-health.yml`).
- **Dynamic Statistics**: Replaced artificial 100,000 commit progress bars with genuine Git repository metrics (`git rev-list --count HEAD`).
- **Documentation Suite**: Rewrote `README.md`, `STATS.md`, `CONTRIBUTING.md`, `SECURITY.md`, and `CODE_OF_CONDUCT.md` to reflect professional open-source standards.

---

## [1.0.0] - 2025-03-08

### Added
- Initial release of local commit generation script and workflow automation prototypes.
