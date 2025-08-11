# Test Brand Update API

Write-Host "Testing Brand Update API..." -ForegroundColor Green

# Get the Samsung brand ID
$brands = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/brands' -Method Get
$samsungBrand = $brands | Where-Object { $_.name -eq "Samsung" }

if ($samsungBrand) {
    Write-Host "`nFound Samsung brand with ID: $($samsungBrand.id)" -ForegroundColor Yellow
    
    # Test update
    $updateData = @{
        name = "Samsung Electronics"
        description = "South Korean multinational electronics and technology company"
        imageUrl = "https://example.com/samsung-new-logo.png"
    } | ConvertTo-Json
    
    try {
        $updatedBrand = Invoke-RestMethod -Uri "http://localhost:8083/api/v1/brands/$($samsungBrand.id)" -Method Put -ContentType 'application/json' -Body $updateData
        Write-Host "Success: Updated brand name to '$($updatedBrand.name)'" -ForegroundColor Green
    } catch {
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    }
} else {
    Write-Host "Samsung brand not found" -ForegroundColor Red
}

Write-Host "`nUpdate test completed!" -ForegroundColor Green
