# CommitInfinity - High-Speed Batch Commit Script (PowerShell)
# Generates local commits cleanly and updates repository metrics every 500 commits.

$Start = 22896
$End = 100000
$BatchSize = 500

Write-Host "Starting CommitInfinity loop from $Start to $End..." -ForegroundColor Cyan

# Remove lock file if lingering from previous interrupted run
if (Test-Path ".git/index.lock") {
    Remove-Item ".git/index.lock" -Force -ErrorAction SilentlyContinue
}

for ($i = $Start; $i -le $End; $i++) {
    # 1. Fast empty commit (avoids Windows file handle locks)
    git commit --allow-empty -m "${i}th commit" | Out-Null

    # 2. Every 500 commits: update python metrics & push batch to GitHub
    if ($i % $BatchSize -eq 0 -or $i -eq $End) {
        python metrics_logger.py
        git add .
        git commit -m "Updated repository live metrics #${i}" | Out-Null
        Write-Host "Completed $i commits! Pushing batch to GitHub..." -ForegroundColor Green
        git push origin main
    }
}

Write-Host "Execution finished successfully!" -ForegroundColor Cyan
