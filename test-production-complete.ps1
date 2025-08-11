# Complete Production API Testing Script

Write-Host "=== STOCK MANAGEMENT SYSTEM - PRODUCTION TESTING ===" -ForegroundColor Green
Write-Host "Base URL: http://localhost:8085" -ForegroundColor Cyan
Write-Host "Profile: Production Mode" -ForegroundColor Cyan

$testResults = @()

# Test 1: Create a new brand
Write-Host "`n1. Testing Brand Creation" -ForegroundColor Yellow
$brandData = @{
    name = "Production Test Brand"
    description = "Brand created during production testing"
    imageUrl = "https://example.com/test-brand.png"
} | ConvertTo-Json

try {
    $newBrand = Invoke-RestMethod -Uri 'http://localhost:8085/api/v1/brands' -Method Post -ContentType 'application/json' -Body $brandData
    Write-Host "✅ Brand created: $($newBrand.name) (ID: $($newBrand.id))" -ForegroundColor Green
    $testResults += "✅ Brand Creation: PASS"
    $brandId = $newBrand.id
} catch {
    Write-Host "❌ Brand creation failed: $($_.Exception.Message)" -ForegroundColor Red
    $testResults += "❌ Brand Creation: FAIL"
}

# Test 2: Create a new unit
Write-Host "`n2. Testing Unit Creation" -ForegroundColor Yellow
$unitData = @{
    name = "Production Test Unit"
    symbol = "ptu"
} | ConvertTo-Json

try {
    $newUnit = Invoke-RestMethod -Uri 'http://localhost:8085/api/v1/units' -Method Post -ContentType 'application/json' -Body $unitData
    Write-Host "✅ Unit created: $($newUnit.name) ($($newUnit.symbol)) (ID: $($newUnit.id))" -ForegroundColor Green
    $testResults += "✅ Unit Creation: PASS"
    $unitId = $newUnit.id
} catch {
    Write-Host "❌ Unit creation failed: $($_.Exception.Message)" -ForegroundColor Red
    $testResults += "❌ Unit Creation: FAIL"
}

# Test 3: Create a new category
Write-Host "`n3. Testing Category Creation" -ForegroundColor Yellow
$categoryData = @{
    name = "Production Test Category"
    description = "Category created during production testing"
    branchId = "prod-test-branch"
} | ConvertTo-Json

try {
    $newCategory = Invoke-RestMethod -Uri 'http://localhost:8085/api/v1/categories' -Method Post -ContentType 'application/json' -Body $categoryData
    Write-Host "✅ Category created: $($newCategory.name) (ID: $($newCategory.id))" -ForegroundColor Green
    $testResults += "✅ Category Creation: PASS"
    $categoryId = $newCategory.id
} catch {
    Write-Host "❌ Category creation failed: $($_.Exception.Message)" -ForegroundColor Red
    $testResults += "❌ Category Creation: FAIL"
}

# Test 4: Create a new inventory item
if ($brandId -and $unitId -and $categoryId) {
    Write-Host "`n4. Testing Inventory Item Creation" -ForegroundColor Yellow
    $itemData = @{
        name = "Production Test Item"
        thresholdQuantity = 10
        reorderQuantity = 50
        unitPurchasePrice = 99.99
        categoryId = $categoryId
        unitId = $unitId
        brandId = $brandId
    } | ConvertTo-Json

    try {
        $newItem = Invoke-RestMethod -Uri 'http://localhost:8085/api/v1/inventory-items' -Method Post -ContentType 'application/json' -Body $itemData
        Write-Host "✅ Inventory Item created: $($newItem.name) (Price: $($newItem.unitPurchasePrice)) (ID: $($newItem.id))" -ForegroundColor Green
        $testResults += "✅ Inventory Item Creation: PASS"
        $itemId = $newItem.id
    } catch {
        Write-Host "❌ Inventory Item creation failed: $($_.Exception.Message)" -ForegroundColor Red
        $testResults += "❌ Inventory Item Creation: FAIL"
    }
}

# Test 5: Test all GET endpoints
Write-Host "`n5. Testing All GET Endpoints" -ForegroundColor Yellow

$endpoints = @(
    @{name="Brands"; url="/api/v1/brands"},
    @{name="Units"; url="/api/v1/units"},
    @{name="Categories"; url="/api/v1/categories"},
    @{name="Inventory Items"; url="/api/v1/inventory-items"},
    @{name="Brands Summary"; url="/api/v1/brands/summary"},
    @{name="Units Summary"; url="/api/v1/units/summary"},
    @{name="Categories Summary"; url="/api/v1/categories/summary"},
    @{name="Inventory Items Summary"; url="/api/v1/inventory-items/summary"}
)

foreach ($endpoint in $endpoints) {
    try {
        $result = Invoke-RestMethod -Uri "http://localhost:8085$($endpoint.url)" -Method Get
        Write-Host "   ✅ $($endpoint.name): $($result.Count) items" -ForegroundColor Green
        $testResults += "✅ $($endpoint.name) GET: PASS"
    } catch {
        Write-Host "   ❌ $($endpoint.name): Failed" -ForegroundColor Red
        $testResults += "❌ $($endpoint.name) GET: FAIL"
    }
}

# Test 6: Test Update Operations
if ($brandId) {
    Write-Host "`n6. Testing Update Operations" -ForegroundColor Yellow
    $updateData = @{
        name = "Updated Production Test Brand"
        description = "Updated during production testing"
        imageUrl = "https://example.com/updated-brand.png"
    } | ConvertTo-Json

    try {
        $updatedBrand = Invoke-RestMethod -Uri "http://localhost:8085/api/v1/brands/$brandId" -Method Put -ContentType 'application/json' -Body $updateData
        Write-Host "✅ Brand updated: $($updatedBrand.name)" -ForegroundColor Green
        $testResults += "✅ Brand Update: PASS"
    } catch {
        Write-Host "❌ Brand update failed: $($_.Exception.Message)" -ForegroundColor Red
        $testResults += "❌ Brand Update: FAIL"
    }
}

# Test 7: Test Search Operations
Write-Host "`n7. Testing Search Operations" -ForegroundColor Yellow

$searchTests = @(
    @{name="Brand Search"; url="/api/v1/brands/search?name=Production"},
    @{name="Unit Search"; url="/api/v1/units/search?symbol=ptu"},
    @{name="Category Search"; url="/api/v1/categories/search?name=Production&branchId=prod-test-branch"},
    @{name="Inventory Item Search"; url="/api/v1/inventory-items/search?name=Production"}
)

foreach ($search in $searchTests) {
    try {
        $result = Invoke-RestMethod -Uri "http://localhost:8085$($search.url)" -Method Get
        Write-Host "   ✅ $($search.name): Found $($result.Count) items" -ForegroundColor Green
        $testResults += "✅ $($search.name): PASS"
    } catch {
        Write-Host "   ❌ $($search.name): Failed" -ForegroundColor Red
        $testResults += "❌ $($search.name): FAIL"
    }
}

# Test 8: Test Health Endpoints
Write-Host "`n8. Testing Health & Monitoring" -ForegroundColor Yellow

$healthTests = @(
    @{name="Health Check"; url="/api/v1/health"},
    @{name="System Info"; url="/api/v1/health/info"},
    @{name="Database Health"; url="/api/v1/health/db"},
    @{name="Actuator Health"; url="/actuator/health"},
    @{name="Actuator Info"; url="/actuator/info"},
    @{name="Actuator Metrics"; url="/actuator/metrics"}
)

foreach ($health in $healthTests) {
    try {
        $result = Invoke-RestMethod -Uri "http://localhost:8085$($health.url)" -Method Get
        Write-Host "   ✅ $($health.name): OK" -ForegroundColor Green
        $testResults += "✅ $($health.name): PASS"
    } catch {
        Write-Host "   ❌ $($health.name): Failed" -ForegroundColor Red
        $testResults += "❌ $($health.name): FAIL"
    }
}

# Test 9: Cleanup - Delete created items
Write-Host "`n9. Testing Delete Operations (Cleanup)" -ForegroundColor Yellow

if ($itemId) {
    try {
        Invoke-RestMethod -Uri "http://localhost:8085/api/v1/inventory-items/$itemId" -Method Delete
        Write-Host "   ✅ Inventory Item deleted" -ForegroundColor Green
        $testResults += "✅ Inventory Item Delete: PASS"
    } catch {
        Write-Host "   ❌ Inventory Item delete failed" -ForegroundColor Red
        $testResults += "❌ Inventory Item Delete: FAIL"
    }
}

if ($categoryId) {
    try {
        Invoke-RestMethod -Uri "http://localhost:8085/api/v1/categories/$categoryId" -Method Delete
        Write-Host "   ✅ Category deleted" -ForegroundColor Green
        $testResults += "✅ Category Delete: PASS"
    } catch {
        Write-Host "   ❌ Category delete failed" -ForegroundColor Red
        $testResults += "❌ Category Delete: FAIL"
    }
}

if ($unitId) {
    try {
        Invoke-RestMethod -Uri "http://localhost:8085/api/v1/units/$unitId" -Method Delete
        Write-Host "   ✅ Unit deleted" -ForegroundColor Green
        $testResults += "✅ Unit Delete: PASS"
    } catch {
        Write-Host "   ❌ Unit delete failed" -ForegroundColor Red
        $testResults += "❌ Unit Delete: FAIL"
    }
}

if ($brandId) {
    try {
        Invoke-RestMethod -Uri "http://localhost:8085/api/v1/brands/$brandId" -Method Delete
        Write-Host "   ✅ Brand deleted" -ForegroundColor Green
        $testResults += "✅ Brand Delete: PASS"
    } catch {
        Write-Host "   ❌ Brand delete failed" -ForegroundColor Red
        $testResults += "❌ Brand Delete: FAIL"
    }
}

# Summary
Write-Host "`n=== PRODUCTION TEST RESULTS ===" -ForegroundColor Green
$passCount = ($testResults | Where-Object { $_ -like "*PASS*" }).Count
$failCount = ($testResults | Where-Object { $_ -like "*FAIL*" }).Count
$totalTests = $testResults.Count

Write-Host "Total Tests: $totalTests" -ForegroundColor Cyan
Write-Host "Passed: $passCount" -ForegroundColor Green
Write-Host "Failed: $failCount" -ForegroundColor Red

if ($failCount -eq 0) {
    Write-Host "`n🎉 ALL TESTS PASSED! PRODUCTION READY! 🎉" -ForegroundColor Green
} else {
    Write-Host "`n⚠️ Some tests failed. Review the results above." -ForegroundColor Yellow
}

Write-Host "`nProduction URLs:" -ForegroundColor Cyan
Write-Host "API Base: http://localhost:8085/api/v1" -ForegroundColor White
Write-Host "Swagger UI: http://localhost:8085/swagger-ui/index.html" -ForegroundColor White
Write-Host "Actuator: http://localhost:8085/actuator" -ForegroundColor White
