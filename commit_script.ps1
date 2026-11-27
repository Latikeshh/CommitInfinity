# CommitInfinity - Counter-Based Automated Commit Script (PowerShell)
# Increments counter.txt on every commit and updates live metrics every 500 commits.

$Start = 16100
$End = 100000
$BatchSize = 500

Write-Host "Starting CommitInfinity counter loop from $Start to $End..." -ForegroundColor Cyan

for ($i = $Start; $i -le $End; $i++) {
    # 1. Update counter.txt with current commit number
    $i | Set-Content counter.txt
    git add counter.txt
    git commit -m "${i}th commit update"

    # 2. Every 500 commits: update python metrics and push batch to GitHub
    if ($i % $BatchSize -eq 0 -or $i -eq $End) {
        python metrics_logger.py
        git add .
        git commit -m "Updated repository live metrics #${i}"
        Write-Host "Completed $i commits! Pushing batch to GitHub..." -ForegroundColor Green
        git push origin main
    }
}

Write-Host "Execution finished successfully!" -ForegroundColor Cyan
