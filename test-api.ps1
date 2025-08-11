# Test Brand API endpoints

Write-Host "Testing Brand API..." -ForegroundColor Green

# Test 1: Get all brands (should be empty initially)
Write-Host "`n1. Testing GET /api/v1/brands" -ForegroundColor Yellow
try {
    $brands = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/brands' -Method Get
    Write-Host "Success: Retrieved $($brands.Count) brands" -ForegroundColor Green
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: Create a new brand
Write-Host "`n2. Testing POST /api/v1/brands" -ForegroundColor Yellow
$brandData = @{
    name = "Samsung"
    description = "South Korean multinational electronics company"
    imageUrl = "https://example.com/samsung-logo.png"
} | ConvertTo-Json

try {
    $newBrand = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/brands' -Method Post -ContentType 'application/json' -Body $brandData
    Write-Host "Success: Created brand with ID: $($newBrand.id)" -ForegroundColor Green
    $brandId = $newBrand.id
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Get brand by ID
if ($brandId) {
    Write-Host "`n3. Testing GET /api/v1/brands/$brandId" -ForegroundColor Yellow
    try {
        $brand = Invoke-RestMethod -Uri "http://localhost:8083/api/v1/brands/$brandId" -Method Get
        Write-Host "Success: Retrieved brand '$($brand.name)'" -ForegroundColor Green
    } catch {
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test 4: Get brands summary
Write-Host "`n4. Testing GET /api/v1/brands/summary" -ForegroundColor Yellow
try {
    $summary = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/brands/summary' -Method Get
    Write-Host "Success: Retrieved $($summary.Count) brand summaries" -ForegroundColor Green
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 5: Search brand by name
Write-Host "`n5. Testing GET /api/v1/brands/search?name=Apple" -ForegroundColor Yellow
try {
    $searchResult = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/brands/search?name=Apple' -Method Get
    Write-Host "Success: Found brand '$($searchResult.name)'" -ForegroundColor Green
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nAPI testing completed!" -ForegroundColor Green
