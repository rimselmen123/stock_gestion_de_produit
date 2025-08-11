# Test InventoryItem API endpoints

Write-Host "Testing InventoryItem API..." -ForegroundColor Green

# First, let's ensure we have the required data (brands, units, categories)
Write-Host "`n=== Setting up test data ===" -ForegroundColor Cyan

# Get existing brands
$brands = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/brands' -Method Get
Write-Host "Available brands: $($brands.Count)" -ForegroundColor Yellow

# Get existing units
$units = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/units' -Method Get
Write-Host "Available units: $($units.Count)" -ForegroundColor Yellow

# Get existing categories
$categories = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/categories' -Method Get
Write-Host "Available categories: $($categories.Count)" -ForegroundColor Yellow

# Create additional test data if needed
if ($brands.Count -eq 0) {
    Write-Host "Creating test brand..." -ForegroundColor Yellow
    $brandData = @{
        name = "Test Brand"
        description = "Test brand for inventory items"
        imageUrl = "https://example.com/test-brand.png"
    } | ConvertTo-Json
    
    $newBrand = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/brands' -Method Post -ContentType 'application/json' -Body $brandData
    $brands = @($newBrand)
}

if ($units.Count -eq 0) {
    Write-Host "Creating test unit..." -ForegroundColor Yellow
    $unitData = @{
        name = "Test Unit"
        symbol = "tu"
    } | ConvertTo-Json
    
    $newUnit = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/units' -Method Post -ContentType 'application/json' -Body $unitData
    $units = @($newUnit)
}

if ($categories.Count -eq 0) {
    Write-Host "Creating test category..." -ForegroundColor Yellow
    $categoryData = @{
        name = "Test Category"
        description = "Test category for inventory items"
        branchId = "test-branch-001"
    } | ConvertTo-Json
    
    $newCategory = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/categories' -Method Post -ContentType 'application/json' -Body $categoryData
    $categories = @($newCategory)
}

Write-Host "`n=== Starting InventoryItem API Tests ===" -ForegroundColor Cyan

# Test 1: Get all inventory items (should be empty initially)
Write-Host "`n1. Testing GET /api/v1/inventory-items" -ForegroundColor Yellow
try {
    $items = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/inventory-items' -Method Get
    Write-Host "Success: Retrieved $($items.Count) inventory items" -ForegroundColor Green
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: Create a new inventory item
Write-Host "`n2. Testing POST /api/v1/inventory-items" -ForegroundColor Yellow
$itemData = @{
    name = "Laptop Computer"
    thresholdQuantity = 5
    reorderQuantity = 20
    unitPurchasePrice = 999.99
    categoryId = $categories[0].id
    unitId = $units[0].id
    brandId = $brands[0].id
} | ConvertTo-Json

try {
    $newItem = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/inventory-items' -Method Post -ContentType 'application/json' -Body $itemData
    Write-Host "Success: Created inventory item with ID: $($newItem.id)" -ForegroundColor Green
    $itemId = $newItem.id
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Create another inventory item without brand
Write-Host "`n3. Testing POST /api/v1/inventory-items (without brand)" -ForegroundColor Yellow
$itemData2 = @{
    name = "Office Chair"
    thresholdQuantity = 10
    reorderQuantity = 50
    unitPurchasePrice = 149.50
    categoryId = $categories[0].id
    unitId = $units[0].id
} | ConvertTo-Json

try {
    $newItem2 = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/inventory-items' -Method Post -ContentType 'application/json' -Body $itemData2
    Write-Host "Success: Created inventory item with ID: $($newItem2.id)" -ForegroundColor Green
    $itemId2 = $newItem2.id
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Get inventory item by ID
if ($itemId) {
    Write-Host "`n4. Testing GET /api/v1/inventory-items/$itemId" -ForegroundColor Yellow
    try {
        $item = Invoke-RestMethod -Uri "http://localhost:8083/api/v1/inventory-items/$itemId" -Method Get
        Write-Host "Success: Retrieved item '$($item.name)' - Price: $($item.unitPurchasePrice)" -ForegroundColor Green
    } catch {
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test 5: Get inventory items summary
Write-Host "`n5. Testing GET /api/v1/inventory-items/summary" -ForegroundColor Yellow
try {
    $summary = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/inventory-items/summary' -Method Get
    Write-Host "Success: Retrieved $($summary.Count) inventory item summaries" -ForegroundColor Green
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nInventoryItem API testing completed!" -ForegroundColor Green
