# Test Category API endpoints

Write-Host "Testing Category API..." -ForegroundColor Green

# Test 1: Get all categories (should be empty initially)
Write-Host "`n1. Testing GET /api/v1/categories" -ForegroundColor Yellow
try {
    $categories = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/categories' -Method Get
    Write-Host "Success: Retrieved $($categories.Count) categories" -ForegroundColor Green
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: Create a new category
Write-Host "`n2. Testing POST /api/v1/categories" -ForegroundColor Yellow
$categoryData = @{
    name = "Electronics"
    description = "Electronic devices and components"
    branchId = "branch-001"
} | ConvertTo-Json

try {
    $newCategory = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/categories' -Method Post -ContentType 'application/json' -Body $categoryData
    Write-Host "Success: Created category with ID: $($newCategory.id)" -ForegroundColor Green
    $categoryId = $newCategory.id
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Create another category in same branch
Write-Host "`n3. Testing POST /api/v1/categories (second category)" -ForegroundColor Yellow
$categoryData2 = @{
    name = "Furniture"
    description = "Office and home furniture"
    branchId = "branch-001"
} | ConvertTo-Json

try {
    $newCategory2 = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/categories' -Method Post -ContentType 'application/json' -Body $categoryData2
    Write-Host "Success: Created category with ID: $($newCategory2.id)" -ForegroundColor Green
    $categoryId2 = $newCategory2.id
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Create category in different branch
Write-Host "`n4. Testing POST /api/v1/categories (different branch)" -ForegroundColor Yellow
$categoryData3 = @{
    name = "Electronics"
    description = "Electronic devices for branch 2"
    branchId = "branch-002"
} | ConvertTo-Json

try {
    $newCategory3 = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/categories' -Method Post -ContentType 'application/json' -Body $categoryData3
    Write-Host "Success: Created category with ID: $($newCategory3.id)" -ForegroundColor Green
    $categoryId3 = $newCategory3.id
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 5: Get category by ID
if ($categoryId) {
    Write-Host "`n5. Testing GET /api/v1/categories/$categoryId" -ForegroundColor Yellow
    try {
        $category = Invoke-RestMethod -Uri "http://localhost:8083/api/v1/categories/$categoryId" -Method Get
        Write-Host "Success: Retrieved category '$($category.name)' for branch '$($category.branchId)'" -ForegroundColor Green
    } catch {
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test 6: Get categories by branch
Write-Host "`n6. Testing GET /api/v1/categories/branch/branch-001" -ForegroundColor Yellow
try {
    $branchCategories = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/categories/branch/branch-001' -Method Get
    Write-Host "Success: Retrieved $($branchCategories.Count) categories for branch-001" -ForegroundColor Green
    foreach ($cat in $branchCategories) {
        Write-Host "  - $($cat.name)" -ForegroundColor Cyan
    }
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 7: Get categories summary
Write-Host "`n7. Testing GET /api/v1/categories/summary" -ForegroundColor Yellow
try {
    $summary = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/categories/summary' -Method Get
    Write-Host "Success: Retrieved $($summary.Count) category summaries" -ForegroundColor Green
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 8: Search categories by name
Write-Host "`n8. Testing GET /api/v1/categories/search?name=Elect&branchId=branch-001" -ForegroundColor Yellow
try {
    $searchResult = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/categories/search?name=Elect&branchId=branch-001' -Method Get
    Write-Host "Success: Found $($searchResult.Count) categories matching 'Elect' in branch-001" -ForegroundColor Green
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 9: Find category by exact name
Write-Host "`n9. Testing GET /api/v1/categories/find?name=Electronics&branchId=branch-001" -ForegroundColor Yellow
try {
    $findResult = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/categories/find?name=Electronics&branchId=branch-001' -Method Get
    Write-Host "Success: Found category '$($findResult.name)' in branch '$($findResult.branchId)'" -ForegroundColor Green
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 10: Get category count by branch
Write-Host "`n10. Testing GET /api/v1/categories/count/branch/branch-001" -ForegroundColor Yellow
try {
    $count = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/categories/count/branch/branch-001' -Method Get
    Write-Host "Success: Branch-001 has $count categories" -ForegroundColor Green
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nCategory API testing completed!" -ForegroundColor Green
