<!-- CommitInfinity Security Policy -->
# Security Policy

## Supported Versions

Only the latest `main` branch of **CommitInfinity** is actively supported with security updates and configuration patches.

| Version | Supported          |
| ------- | ------------------ |
| `main`  | :white_check_mark: |
| Legacy  | :x:                |

---

## Security Best Practices for CommitInfinity

When using automated workflow repositories such as CommitInfinity, adhere to the following security recommendations:

1. **GitHub Actions Permissions**:
   - Limit workflow permissions strictly to `contents: write` as defined in `.github/workflows/auto-commit.yml`.
   - Avoid granting administrative permissions (`id-token`, `secrets`, etc.) unless specifically required.
2. **Personal Access Tokens (PATs) & Secrets**:
   - Never commit API keys, GitHub Personal Access Tokens (PAT), or private keys directly into repository files or workflow scripts.
   - Use GitHub Repository Secrets (`${{ secrets.GITHUB_TOKEN }}`) for authentication.
3. **Branch Protection**:
   - Enable branch protection rules on `main` to regulate direct pushes and require status checks if necessary.

---

## Reporting a Vulnerability

If you discover a security vulnerability or misconfiguration in **CommitInfinity**:

1. **Do not** report security vulnerabilities through public GitHub issues.
2. Send a report detailing:
   - Description of the issue or security misconfiguration.
   - Steps to reproduce the issue.
   - Potential impact of the vulnerability.

We aim to respond to security reports promptly and release patches as needed.
