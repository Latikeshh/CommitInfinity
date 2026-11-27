# CommitInfinity - Real Append-Log Commit Script (PowerShell)
# Appends a new line with the current commit number to counter.txt on every commit.

$End = 100000
$BatchSize = 500

# Remove lock file if lingering from previous interrupted run
if (Test-Path ".git/index.lock") {
    Remove-Item ".git/index.lock" -Force -ErrorAction SilentlyContinue
}

# Auto-detect starting commit number using exact Git commit count
$totalCommits = [int](git rev-list --count HEAD)
$Start = $totalCommits + 1

Write-Host "==================================================" -ForegroundColor Cyan
Write-Host " CommitInfinity Real-Append Log Script" -ForegroundColor Green
Write-Host " Total Current Commits      : $totalCommits" -ForegroundColor Yellow
Write-Host " Next Start Commit          : $Start" -ForegroundColor Yellow
Write-Host " Target End Commit          : $End" -ForegroundColor Yellow
Write-Host " Batch Push Size            : $BatchSize" -ForegroundColor Yellow
Write-Host "==================================================" -ForegroundColor Cyan

if ($Start -gt $End) {
    Write-Host "Repository has already reached target $End commits!" -ForegroundColor Green
    exit 0
}

$countFile = "$PWD/counter.txt"

for ($i = $Start; $i -le $End; $i++) {
    # Append current commit number as a new line in counter.txt
    [System.IO.File]::AppendAllText($countFile, "$i`r`n")
    git add counter.txt | Out-Null
    git commit -m "${i}th record log" | Out-Null

    if ($i % $BatchSize -eq 0 -or $i -eq $End) {
        if (Test-Path "metrics_logger.py") {
            python metrics_logger.py
            git add . | Out-Null
            git commit -m "Updated repository live metrics #${i}" | Out-Null
        }
        Write-Host "[LOG] Completed $i commits! Pushing batch to GitHub..." -ForegroundColor Green
        git push origin main
    }
}

Write-Host "==================================================" -ForegroundColor Cyan
Write-Host " Execution Completed Successfully!" -ForegroundColor Green
Write-Host "==================================================" -ForegroundColor Cyan
