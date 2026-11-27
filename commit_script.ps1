# CommitInfinity - High-Speed Auto-Continuing Commit Script (PowerShell)
# Automatically detects current commit number and pushes in batches of 500.

$End = 100000
$BatchSize = 500

# Remove lock file if lingering from previous interrupted run
if (Test-Path ".git/index.lock") {
    Remove-Item ".git/index.lock" -Force -ErrorAction SilentlyContinue
}

# Auto-detect starting commit number from git log
$lastMsg = git log -n 1 --format="%s"
if ($lastMsg -match '(\d+)') {
    $Start = [int]$Matches[1] + 1
} else {
    $Start = 23326
}
Write-Host " Auto-detected Start Commit : $Start" -ForegroundColor Yellow

if ($Start -gt $End) {
    Write-Host "Repository has already reached target $End commits!" -ForegroundColor Green
    exit 0
}

for ($i = $Start; $i -le $End; $i++) {
    git commit --allow-empty -m "${i}th commit" | Out-Null

    if ($i % $BatchSize -eq 0 -or $i -eq $End) {
        if (Test-Path "metrics_logger.py") {
            python metrics_logger.py
            git add .
            git commit -m "Updated repository live metrics #${i}" | Out-Null
        }
        Write-Host "[LOG] Completed $i commits! Pushing batch to GitHub..." -ForegroundColor Green
        git push origin main
    }
}

Write-Host " Execution Completed Successfully!" -ForegroundColor Green
