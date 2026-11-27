# Security Policy

## Supported Versions

Only the latest `main` branch of **CommitInfinity** is actively supported with security updates and configuration patches.

| Version | Supported          |
| ------- | ------------------ |
| `2.0.x` | :white_check_mark: |
| Legacy  | :x:                |

---

## Security Best Practices

When using repository automation tools such as CommitInfinity, adhere to the following security recommendations:

1. **Local Script Execution**:
   - Keep local automation scripts (`commit_script.ps1`, `commit_script.sh`) local to your workstation device.
   - Do not commit secrets, private tokens, or authentication credentials into repository files or scripts.
2. **Personal Access Tokens (PATs) & Secrets**:
   - Never hardcode GitHub Personal Access Tokens (PATs) or private keys inside automation scripts.
   - Use standard Git credentials manager or SSH keys for remote authentication.
3. **GitHub Actions Permissions**:
   - Workflows in `.github/workflows/` should operate with read-only permissions (`contents: read`) unless explicitly required.

---

## Reporting a Vulnerability

If you discover a security vulnerability or misconfiguration in **CommitInfinity**:

1. **Do not** report security vulnerabilities through public GitHub issues.
2. Please open a private security disclosure on GitHub or contact repository maintainers directly.
3. Detail the nature of the vulnerability, step-by-step reproduction instructions, and potential impact.
