# Complete 3-Entity System Testing Script (Unit, Category, InventoryItem)

Write-Host "=== STOCK MANAGEMENT SYSTEM - 3 ENTITY TESTING ===" -ForegroundColor Green
Write-Host "Base URL: http://localhost:8083" -ForegroundColor Cyan
Write-Host "Entities: Unit, Category, InventoryItem (Brand removed)" -ForegroundColor Cyan

$testResults = @()

# Test 1: Create a new unit
Write-Host "`n1. Testing Unit Creation" -ForegroundColor Yellow
$unitData = @{
    name = "Test Unit"
    symbol = "tu"
} | ConvertTo-Json

try {
    $newUnit = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/units' -Method Post -ContentType 'application/json' -Body $unitData
    Write-Host "✅ Unit created: $($newUnit.name) ($($newUnit.symbol)) (ID: $($newUnit.id))" -ForegroundColor Green
    $testResults += "✅ Unit Creation: PASS"
    $unitId = $newUnit.id
} catch {
    Write-Host "❌ Unit creation failed: $($_.Exception.Message)" -ForegroundColor Red
    $testResults += "❌ Unit Creation: FAIL"
}

# Test 2: Create a new category
Write-Host "`n2. Testing Category Creation" -ForegroundColor Yellow
$categoryData = @{
    name = "Test Category"
    description = "Category for testing 3-entity system"
    branchId = "test-branch"
} | ConvertTo-Json

try {
    $newCategory = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/categories' -Method Post -ContentType 'application/json' -Body $categoryData
    Write-Host "✅ Category created: $($newCategory.name) (ID: $($newCategory.id))" -ForegroundColor Green
    $testResults += "✅ Category Creation: PASS"
    $categoryId = $newCategory.id
} catch {
    Write-Host "❌ Category creation failed: $($_.Exception.Message)" -ForegroundColor Red
    $testResults += "❌ Category Creation: FAIL"
}

# Test 3: Create a new inventory item (without brand)
if ($unitId -and $categoryId) {
    Write-Host "`n3. Testing Inventory Item Creation (No Brand)" -ForegroundColor Yellow
    $itemData = @{
        name = "Test Item Without Brand"
        thresholdQuantity = 5
        reorderQuantity = 25
        unitPurchasePrice = 49.99
        categoryId = $categoryId
        unitId = $unitId
    } | ConvertTo-Json

    try {
        $newItem = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/inventory-items' -Method Post -ContentType 'application/json' -Body $itemData
        Write-Host "✅ Inventory Item created: $($newItem.name) (Price: $($newItem.unitPurchasePrice)) (ID: $($newItem.id))" -ForegroundColor Green
        $testResults += "✅ Inventory Item Creation: PASS"
        $itemId = $newItem.id
    } catch {
        Write-Host "❌ Inventory Item creation failed: $($_.Exception.Message)" -ForegroundColor Red
        $testResults += "❌ Inventory Item Creation: FAIL"
    }
}

# Test 4: Test all GET endpoints
Write-Host "`n4. Testing All GET Endpoints" -ForegroundColor Yellow

$endpoints = @(
    @{name="Units"; url="/api/v1/units"},
    @{name="Categories"; url="/api/v1/categories"},
    @{name="Inventory Items"; url="/api/v1/inventory-items"},
    @{name="Units Summary"; url="/api/v1/units/summary"},
    @{name="Categories Summary"; url="/api/v1/categories/summary"},
    @{name="Inventory Items Summary"; url="/api/v1/inventory-items/summary"}
)

foreach ($endpoint in $endpoints) {
    try {
        $result = Invoke-RestMethod -Uri "http://localhost:8083$($endpoint.url)" -Method Get
        Write-Host "   ✅ $($endpoint.name): $($result.Count) items" -ForegroundColor Green
        $testResults += "✅ $($endpoint.name) GET: PASS"
    } catch {
        Write-Host "   ❌ $($endpoint.name): Failed" -ForegroundColor Red
        $testResults += "❌ $($endpoint.name) GET: FAIL"
    }
}

# Test 5: Test Update Operations
if ($unitId) {
    Write-Host "`n5. Testing Update Operations" -ForegroundColor Yellow
    $updateData = @{
        name = "Updated Test Unit"
        symbol = "utu"
    } | ConvertTo-Json

    try {
        $updatedUnit = Invoke-RestMethod -Uri "http://localhost:8083/api/v1/units/$unitId" -Method Put -ContentType 'application/json' -Body $updateData
        Write-Host "✅ Unit updated: $($updatedUnit.name) ($($updatedUnit.symbol))" -ForegroundColor Green
        $testResults += "✅ Unit Update: PASS"
    } catch {
        Write-Host "❌ Unit update failed: $($_.Exception.Message)" -ForegroundColor Red
        $testResults += "❌ Unit Update: FAIL"
    }
}

# Test 6: Test Search Operations
Write-Host "`n6. Testing Search Operations" -ForegroundColor Yellow

$searchTests = @(
    @{name="Unit Search"; url="/api/v1/units/search?symbol=utu"},
    @{name="Category Search"; url="/api/v1/categories/search?name=Test&branchId=test-branch"},
    @{name="Inventory Item Search"; url="/api/v1/inventory-items/search?name=Test"}
)

foreach ($search in $searchTests) {
    try {
        $result = Invoke-RestMethod -Uri "http://localhost:8083$($search.url)" -Method Get
        Write-Host "   ✅ $($search.name): Found $($result.Count) items" -ForegroundColor Green
        $testResults += "✅ $($search.name): PASS"
    } catch {
        Write-Host "   ❌ $($search.name): Failed" -ForegroundColor Red
        $testResults += "❌ $($search.name): FAIL"
    }
}

# Test 7: Test Health Endpoints
Write-Host "`n7. Testing Health & Monitoring" -ForegroundColor Yellow

$healthTests = @(
    @{name="Health Check"; url="/api/v1/health"},
    @{name="System Info"; url="/api/v1/health/info"},
    @{name="Database Health"; url="/api/v1/health/db"},
    @{name="Actuator Health"; url="/actuator/health"}
)

foreach ($health in $healthTests) {
    try {
        $result = Invoke-RestMethod -Uri "http://localhost:8083$($health.url)" -Method Get
        Write-Host "   ✅ $($health.name): OK" -ForegroundColor Green
        $testResults += "✅ $($health.name): PASS"
    } catch {
        Write-Host "   ❌ $($health.name): Failed" -ForegroundColor Red
        $testResults += "❌ $($health.name): FAIL"
    }
}

# Test 8: Test Swagger UI Access
Write-Host "`n8. Testing Swagger UI Access" -ForegroundColor Yellow
try {
    $swagger = Invoke-WebRequest -Uri 'http://localhost:8083/swagger-ui/index.html' -Method Get
    if ($swagger.StatusCode -eq 200) {
        Write-Host "✅ Swagger UI: Accessible (Status: $($swagger.StatusCode))" -ForegroundColor Green
        $testResults += "✅ Swagger UI: PASS"
    }
} catch {
    Write-Host "❌ Swagger UI failed: $($_.Exception.Message)" -ForegroundColor Red
    $testResults += "❌ Swagger UI: FAIL"
}

# Test 9: Test Inventory Item Relationships
if ($itemId) {
    Write-Host "`n9. Testing Inventory Item Relationships" -ForegroundColor Yellow
    try {
        $item = Invoke-RestMethod -Uri "http://localhost:8083/api/v1/inventory-items/$itemId" -Method Get
        if ($item.category -and $item.unit) {
            Write-Host "✅ Relationships: Category ($($item.category.name)) and Unit ($($item.unit.symbol)) properly linked" -ForegroundColor Green
            $testResults += "✅ Item Relationships: PASS"
        } else {
            Write-Host "❌ Relationships: Missing category or unit data" -ForegroundColor Red
            $testResults += "❌ Item Relationships: FAIL"
        }
    } catch {
        Write-Host "❌ Relationship test failed: $($_.Exception.Message)" -ForegroundColor Red
        $testResults += "❌ Item Relationships: FAIL"
    }
}

# Test 10: Verify Brand Endpoints Are Gone
Write-Host "`n10. Testing Brand Endpoints Removal" -ForegroundColor Yellow
try {
    $brandTest = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/brands' -Method Get
    Write-Host "❌ Brand endpoints still exist!" -ForegroundColor Red
    $testResults += "❌ Brand Removal: FAIL"
} catch {
    Write-Host "✅ Brand endpoints successfully removed (404 expected)" -ForegroundColor Green
    $testResults += "✅ Brand Removal: PASS"
}

# Test 11: Cleanup - Delete created items
Write-Host "`n11. Testing Delete Operations (Cleanup)" -ForegroundColor Yellow

if ($itemId) {
    try {
        Invoke-RestMethod -Uri "http://localhost:8083/api/v1/inventory-items/$itemId" -Method Delete
        Write-Host "   ✅ Inventory Item deleted" -ForegroundColor Green
        $testResults += "✅ Inventory Item Delete: PASS"
    } catch {
        Write-Host "   ❌ Inventory Item delete failed" -ForegroundColor Red
        $testResults += "❌ Inventory Item Delete: FAIL"
    }
}

if ($categoryId) {
    try {
        Invoke-RestMethod -Uri "http://localhost:8083/api/v1/categories/$categoryId" -Method Delete
        Write-Host "   ✅ Category deleted" -ForegroundColor Green
        $testResults += "✅ Category Delete: PASS"
    } catch {
        Write-Host "   ❌ Category delete failed" -ForegroundColor Red
        $testResults += "❌ Category Delete: FAIL"
    }
}

if ($unitId) {
    try {
        Invoke-RestMethod -Uri "http://localhost:8083/api/v1/units/$unitId" -Method Delete
        Write-Host "   ✅ Unit deleted" -ForegroundColor Green
        $testResults += "✅ Unit Delete: PASS"
    } catch {
        Write-Host "   ❌ Unit delete failed" -ForegroundColor Red
        $testResults += "❌ Unit Delete: FAIL"
    }
}

# Summary
Write-Host "`n=== 3-ENTITY SYSTEM TEST RESULTS ===" -ForegroundColor Green
$passCount = ($testResults | Where-Object { $_ -like "*PASS*" }).Count
$failCount = ($testResults | Where-Object { $_ -like "*FAIL*" }).Count
$totalTests = $testResults.Count

Write-Host "Total Tests: $totalTests" -ForegroundColor Cyan
Write-Host "Passed: $passCount" -ForegroundColor Green
Write-Host "Failed: $failCount" -ForegroundColor Red

if ($failCount -eq 0) {
    Write-Host "`n🎉 ALL TESTS PASSED! 3-ENTITY SYSTEM WORKING PERFECTLY! 🎉" -ForegroundColor Green
} else {
    Write-Host "`n⚠️ Some tests failed. Review the results above." -ForegroundColor Yellow
}

Write-Host "`n3-Entity System URLs:" -ForegroundColor Cyan
Write-Host "API Base: http://localhost:8083/api/v1" -ForegroundColor White
Write-Host "Units: http://localhost:8083/api/v1/units" -ForegroundColor White
Write-Host "Categories: http://localhost:8083/api/v1/categories" -ForegroundColor White
Write-Host "Inventory Items: http://localhost:8083/api/v1/inventory-items" -ForegroundColor White
Write-Host "Swagger UI: http://localhost:8083/swagger-ui/index.html" -ForegroundColor White
Write-Host "Actuator: http://localhost:8083/actuator" -ForegroundColor White
