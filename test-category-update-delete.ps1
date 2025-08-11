# Test Category Update and Delete API

Write-Host "Testing Category Update and Delete API..." -ForegroundColor Green

# Get all categories first
$categories = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/categories' -Method Get
Write-Host "Current categories:" -ForegroundColor Yellow
foreach ($cat in $categories) {
    Write-Host "  - $($cat.name) (Branch: $($cat.branchId)) - ID: $($cat.id)" -ForegroundColor Cyan
}

# Test update on the "Furniture" category
$furnitureCategory = $categories | Where-Object { $_.name -eq "Furniture" }

if ($furnitureCategory) {
    Write-Host "`nTesting PUT /api/v1/categories/$($furnitureCategory.id)" -ForegroundColor Yellow
    $updateData = @{
        name = "Office Furniture"
        description = "Professional office furniture and equipment"
    } | ConvertTo-Json
    
    try {
        $updatedCategory = Invoke-RestMethod -Uri "http://localhost:8083/api/v1/categories/$($furnitureCategory.id)" -Method Put -ContentType 'application/json' -Body $updateData
        Write-Host "Success: Updated category name to '$($updatedCategory.name)'" -ForegroundColor Green
    } catch {
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    }
} else {
    Write-Host "Furniture category not found" -ForegroundColor Red
}

# Test duplicate name validation (should fail)
Write-Host "`nTesting duplicate name validation" -ForegroundColor Yellow
$duplicateData = @{
    name = "Electronics"
    description = "This should fail due to duplicate name in same branch"
    branchId = "branch-001"
} | ConvertTo-Json

try {
    $duplicateCategory = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/categories' -Method Post -ContentType 'application/json' -Body $duplicateData
    Write-Host "Error: Duplicate validation failed - this should not succeed" -ForegroundColor Red
} catch {
    Write-Host "Success: Duplicate name validation working correctly" -ForegroundColor Green
}

# Get categories from branch-002 for deletion test
$branch2Categories = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/categories/branch/branch-002' -Method Get
$electronicsCategory2 = $branch2Categories | Where-Object { $_.name -eq "Electronics" }

if ($electronicsCategory2) {
    Write-Host "`nTesting DELETE /api/v1/categories/$($electronicsCategory2.id)" -ForegroundColor Yellow
    try {
        Invoke-RestMethod -Uri "http://localhost:8083/api/v1/categories/$($electronicsCategory2.id)" -Method Delete
        Write-Host "Success: Category deleted successfully" -ForegroundColor Green
    } catch {
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    }
    
    # Verify deletion
    Write-Host "`nVerifying deletion - GET /api/v1/categories/branch/branch-002" -ForegroundColor Yellow
    try {
        $remainingCategories = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/categories/branch/branch-002' -Method Get
        Write-Host "Success: Retrieved $($remainingCategories.Count) categories in branch-002 after deletion" -ForegroundColor Green
    } catch {
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    }
} else {
    Write-Host "Electronics category in branch-002 not found" -ForegroundColor Red
}

# Final verification - get all categories
Write-Host "`nFinal state - GET /api/v1/categories" -ForegroundColor Yellow
try {
    $finalCategories = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/categories' -Method Get
    Write-Host "Success: Retrieved $($finalCategories.Count) categories total" -ForegroundColor Green
    foreach ($cat in $finalCategories) {
        Write-Host "  - $($cat.name) (Branch: $($cat.branchId))" -ForegroundColor Cyan
    }
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nCategory Update and Delete test completed!" -ForegroundColor Green
