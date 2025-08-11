# Test Unit API endpoints

Write-Host "Testing Unit API..." -ForegroundColor Green

# Test 1: Get all units (should be empty initially)
Write-Host "`n1. Testing GET /api/v1/units" -ForegroundColor Yellow
try {
    $units = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/units' -Method Get
    Write-Host "Success: Retrieved $($units.Count) units" -ForegroundColor Green
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: Create a new unit
Write-Host "`n2. Testing POST /api/v1/units" -ForegroundColor Yellow
$unitData = @{
    name = "Kilogram"
    symbol = "kg"
} | ConvertTo-Json

try {
    $newUnit = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/units' -Method Post -ContentType 'application/json' -Body $unitData
    Write-Host "Success: Created unit with ID: $($newUnit.id)" -ForegroundColor Green
    $unitId = $newUnit.id
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Create another unit
Write-Host "`n3. Testing POST /api/v1/units (second unit)" -ForegroundColor Yellow
$unitData2 = @{
    name = "Piece"
    symbol = "pcs"
} | ConvertTo-Json

try {
    $newUnit2 = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/units' -Method Post -ContentType 'application/json' -Body $unitData2
    Write-Host "Success: Created unit with ID: $($newUnit2.id)" -ForegroundColor Green
    $unitId2 = $newUnit2.id
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Get unit by ID
if ($unitId) {
    Write-Host "`n4. Testing GET /api/v1/units/$unitId" -ForegroundColor Yellow
    try {
        $unit = Invoke-RestMethod -Uri "http://localhost:8083/api/v1/units/$unitId" -Method Get
        Write-Host "Success: Retrieved unit '$($unit.name)' with symbol '$($unit.symbol)'" -ForegroundColor Green
    } catch {
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test 5: Get units summary
Write-Host "`n5. Testing GET /api/v1/units/summary" -ForegroundColor Yellow
try {
    $summary = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/units/summary' -Method Get
    Write-Host "Success: Retrieved $($summary.Count) unit summaries" -ForegroundColor Green
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 6: Search unit by symbol
Write-Host "`n6. Testing GET /api/v1/units/search?symbol=kg" -ForegroundColor Yellow
try {
    $searchResult = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/units/search?symbol=kg' -Method Get
    Write-Host "Success: Found unit '$($searchResult.name)' with symbol '$($searchResult.symbol)'" -ForegroundColor Green
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 7: Update unit
if ($unitId) {
    Write-Host "`n7. Testing PUT /api/v1/units/$unitId" -ForegroundColor Yellow
    $updateData = @{
        name = "Kilogram (Weight)"
        symbol = "kg"
    } | ConvertTo-Json
    
    try {
        $updatedUnit = Invoke-RestMethod -Uri "http://localhost:8083/api/v1/units/$unitId" -Method Put -ContentType 'application/json' -Body $updateData
        Write-Host "Success: Updated unit name to '$($updatedUnit.name)'" -ForegroundColor Green
    } catch {
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test 8: Get all units again to see the changes
Write-Host "`n8. Testing GET /api/v1/units (after updates)" -ForegroundColor Yellow
try {
    $allUnits = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/units' -Method Get
    Write-Host "Success: Retrieved $($allUnits.Count) units" -ForegroundColor Green
    foreach ($unit in $allUnits) {
        Write-Host "  - $($unit.name) ($($unit.symbol))" -ForegroundColor Cyan
    }
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nUnit API testing completed!" -ForegroundColor Green
