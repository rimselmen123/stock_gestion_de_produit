# Test Unit Delete API

Write-Host "Testing Unit Delete API..." -ForegroundColor Green

# Get all units first
$units = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/units' -Method Get
Write-Host "Current units:" -ForegroundColor Yellow
foreach ($unit in $units) {
    Write-Host "  - $($unit.name) ($($unit.symbol)) - ID: $($unit.id)" -ForegroundColor Cyan
}

# Test delete on the "Piece" unit
$pieceUnit = $units | Where-Object { $_.symbol -eq "pcs" }

if ($pieceUnit) {
    Write-Host "`nTesting DELETE /api/v1/units/$($pieceUnit.id)" -ForegroundColor Yellow
    try {
        Invoke-RestMethod -Uri "http://localhost:8083/api/v1/units/$($pieceUnit.id)" -Method Delete
        Write-Host "Success: Unit deleted successfully" -ForegroundColor Green
    } catch {
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    }
    
    # Verify deletion
    Write-Host "`nVerifying deletion - GET /api/v1/units" -ForegroundColor Yellow
    try {
        $remainingUnits = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/units' -Method Get
        Write-Host "Success: Retrieved $($remainingUnits.Count) units after deletion" -ForegroundColor Green
        foreach ($unit in $remainingUnits) {
            Write-Host "  - $($unit.name) ($($unit.symbol))" -ForegroundColor Cyan
        }
    } catch {
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    }
} else {
    Write-Host "Piece unit not found" -ForegroundColor Red
}

Write-Host "`nUnit Delete test completed!" -ForegroundColor Green
