# Test InventoryItem API advanced features

Write-Host "Testing InventoryItem API Advanced Features..." -ForegroundColor Green

# Get all inventory items first
$items = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/inventory-items' -Method Get
Write-Host "Current inventory items:" -ForegroundColor Yellow
foreach ($item in $items) {
    Write-Host "  - $($item.name) (Price: $($item.unitPurchasePrice)) - ID: $($item.id)" -ForegroundColor Cyan
}

# Test 6: Get inventory items by category
if ($items.Count -gt 0) {
    $categoryId = $items[0].category.id
    Write-Host "`n6. Testing GET /api/v1/inventory-items/category/$categoryId" -ForegroundColor Yellow
    try {
        $categoryItems = Invoke-RestMethod -Uri "http://localhost:8083/api/v1/inventory-items/category/$categoryId" -Method Get
        Write-Host "Success: Retrieved $($categoryItems.Count) items in category" -ForegroundColor Green
    } catch {
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test 7: Search inventory items by name
Write-Host "`n7. Testing GET /api/v1/inventory-items/search?name=Laptop" -ForegroundColor Yellow
try {
    $searchResult = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/inventory-items/search?name=Laptop' -Method Get
    Write-Host "Success: Found $($searchResult.Count) items matching 'Laptop'" -ForegroundColor Green
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 8: Get inventory items by price range
Write-Host "`n8. Testing GET /api/v1/inventory-items/price-range?minPrice=100&maxPrice=500" -ForegroundColor Yellow
try {
    $priceRangeResult = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/inventory-items/price-range?minPrice=100&maxPrice=500' -Method Get
    Write-Host "Success: Found $($priceRangeResult.Count) items in price range 100-500" -ForegroundColor Green
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 9: Get low stock items
Write-Host "`n9. Testing GET /api/v1/inventory-items/low-stock?threshold=15" -ForegroundColor Yellow
try {
    $lowStockResult = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/inventory-items/low-stock?threshold=15' -Method Get
    Write-Host "Success: Found $($lowStockResult.Count) items with threshold <= 15" -ForegroundColor Green
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 10: Update an inventory item
if ($items.Count -gt 0) {
    $itemToUpdate = $items[0]
    Write-Host "`n10. Testing PUT /api/v1/inventory-items/$($itemToUpdate.id)" -ForegroundColor Yellow
    $updateData = @{
        name = "Updated Laptop Computer"
        thresholdQuantity = 8
        reorderQuantity = 25
        unitPurchasePrice = 1199.99
        categoryId = $itemToUpdate.category.id
        unitId = $itemToUpdate.unit.id
        brandId = $itemToUpdate.brand.id
    } | ConvertTo-Json
    
    try {
        $updatedItem = Invoke-RestMethod -Uri "http://localhost:8083/api/v1/inventory-items/$($itemToUpdate.id)" -Method Put -ContentType 'application/json' -Body $updateData
        Write-Host "Success: Updated item name to '$($updatedItem.name)' with new price: $($updatedItem.unitPurchasePrice)" -ForegroundColor Green
    } catch {
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test 11: Test validation - try to create item with invalid data (reorderQuantity <= thresholdQuantity)
Write-Host "`n11. Testing validation - invalid reorder quantity" -ForegroundColor Yellow
$invalidData = @{
    name = "Invalid Item"
    thresholdQuantity = 20
    reorderQuantity = 15  # This should fail - reorder must be > threshold
    unitPurchasePrice = 50.00
    categoryId = $items[0].category.id
    unitId = $items[0].unit.id
} | ConvertTo-Json

try {
    $invalidItem = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/inventory-items' -Method Post -ContentType 'application/json' -Body $invalidData
    Write-Host "Error: Validation failed - this should not succeed" -ForegroundColor Red
} catch {
    Write-Host "Success: Validation working correctly - reorder quantity validation" -ForegroundColor Green
}

# Test 12: Test duplicate name validation
Write-Host "`n12. Testing duplicate name validation" -ForegroundColor Yellow
$duplicateData = @{
    name = "Updated Laptop Computer"  # This name should already exist
    thresholdQuantity = 5
    reorderQuantity = 20
    unitPurchasePrice = 999.99
    categoryId = $items[0].category.id
    unitId = $items[0].unit.id
} | ConvertTo-Json

try {
    $duplicateItem = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/inventory-items' -Method Post -ContentType 'application/json' -Body $duplicateData
    Write-Host "Error: Duplicate name validation failed - this should not succeed" -ForegroundColor Red
} catch {
    Write-Host "Success: Duplicate name validation working correctly" -ForegroundColor Green
}

# Test 13: Delete an inventory item
if ($items.Count -gt 1) {
    $itemToDelete = $items[1]
    Write-Host "`n13. Testing DELETE /api/v1/inventory-items/$($itemToDelete.id)" -ForegroundColor Yellow
    try {
        Invoke-RestMethod -Uri "http://localhost:8083/api/v1/inventory-items/$($itemToDelete.id)" -Method Delete
        Write-Host "Success: Item deleted successfully" -ForegroundColor Green
    } catch {
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    }
    
    # Verify deletion
    Write-Host "`nVerifying deletion - GET /api/v1/inventory-items" -ForegroundColor Yellow
    try {
        $remainingItems = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/inventory-items' -Method Get
        Write-Host "Success: Retrieved $($remainingItems.Count) items after deletion" -ForegroundColor Green
    } catch {
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Final verification - get all items
Write-Host "`nFinal state - GET /api/v1/inventory-items" -ForegroundColor Yellow
try {
    $finalItems = Invoke-RestMethod -Uri 'http://localhost:8083/api/v1/inventory-items' -Method Get
    Write-Host "Success: Retrieved $($finalItems.Count) items total" -ForegroundColor Green
    foreach ($item in $finalItems) {
        Write-Host "  - $($item.name) (Price: $($item.unitPurchasePrice))" -ForegroundColor Cyan
    }
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nInventoryItem Advanced API testing completed!" -ForegroundColor Green
