# Production API Testing Script

Write-Host "Testing Stock Management API in Production Mode..." -ForegroundColor Green
Write-Host "Base URL: http://localhost:8085" -ForegroundColor Cyan

# Test 1: Health Check
Write-Host "`n1. Testing Health Check - GET /api/v1/health" -ForegroundColor Yellow
try {
    $health = Invoke-RestMethod -Uri 'http://localhost:8085/api/v1/health' -Method Get
    Write-Host "✅ Health Status: $($health.status)" -ForegroundColor Green
    Write-Host "   Application: $($health.application)" -ForegroundColor Cyan
    Write-Host "   Profile: $($health.profile)" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Health check failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: System Information
Write-Host "`n2. Testing System Info - GET /api/v1/health/info" -ForegroundColor Yellow
try {
    $info = Invoke-RestMethod -Uri 'http://localhost:8085/api/v1/health/info' -Method Get
    Write-Host "✅ System Info Retrieved" -ForegroundColor Green
    Write-Host "   App Version: $($info.application.version)" -ForegroundColor Cyan
    Write-Host "   Java Version: $($info.system.'java.version')" -ForegroundColor Cyan
    Write-Host "   Database Status: $($info.database.status)" -ForegroundColor Cyan
} catch {
    Write-Host "❌ System info failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Database Health
Write-Host "`n3. Testing Database Health - GET /api/v1/health/db" -ForegroundColor Yellow
try {
    $dbHealth = Invoke-RestMethod -Uri 'http://localhost:8085/api/v1/health/db' -Method Get
    Write-Host "✅ Database Status: $($dbHealth.status)" -ForegroundColor Green
    Write-Host "   Database: $($dbHealth.database)" -ForegroundColor Cyan
    Write-Host "   Version: $($dbHealth.version)" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Database health check failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Actuator Health
Write-Host "`n4. Testing Actuator Health - GET /actuator/health" -ForegroundColor Yellow
try {
    $actuatorHealth = Invoke-RestMethod -Uri 'http://localhost:8085/actuator/health' -Method Get
    Write-Host "✅ Actuator Health: $($actuatorHealth.status)" -ForegroundColor Green
} catch {
    Write-Host "❌ Actuator health failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 5: API Endpoints - Brands
Write-Host "`n5. Testing Brand API - GET /api/v1/brands" -ForegroundColor Yellow
try {
    $brands = Invoke-RestMethod -Uri 'http://localhost:8085/api/v1/brands' -Method Get
    Write-Host "✅ Brands API: Retrieved $($brands.Count) brands" -ForegroundColor Green
} catch {
    Write-Host "❌ Brands API failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 6: API Endpoints - Units
Write-Host "`n6. Testing Unit API - GET /api/v1/units" -ForegroundColor Yellow
try {
    $units = Invoke-RestMethod -Uri 'http://localhost:8085/api/v1/units' -Method Get
    Write-Host "✅ Units API: Retrieved $($units.Count) units" -ForegroundColor Green
} catch {
    Write-Host "❌ Units API failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 7: API Endpoints - Categories
Write-Host "`n7. Testing Category API - GET /api/v1/categories" -ForegroundColor Yellow
try {
    $categories = Invoke-RestMethod -Uri 'http://localhost:8085/api/v1/categories' -Method Get
    Write-Host "✅ Categories API: Retrieved $($categories.Count) categories" -ForegroundColor Green
} catch {
    Write-Host "❌ Categories API failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 8: API Endpoints - Inventory Items
Write-Host "`n8. Testing Inventory Item API - GET /api/v1/inventory-items" -ForegroundColor Yellow
try {
    $items = Invoke-RestMethod -Uri 'http://localhost:8085/api/v1/inventory-items' -Method Get
    Write-Host "✅ Inventory Items API: Retrieved $($items.Count) items" -ForegroundColor Green
} catch {
    Write-Host "❌ Inventory Items API failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 9: Swagger UI Access
Write-Host "`n9. Testing Swagger UI Access" -ForegroundColor Yellow
try {
    $swagger = Invoke-WebRequest -Uri 'http://localhost:8085/swagger-ui/index.html' -Method Get
    if ($swagger.StatusCode -eq 200) {
        Write-Host "✅ Swagger UI: Accessible (Status: $($swagger.StatusCode))" -ForegroundColor Green
    }
} catch {
    Write-Host "❌ Swagger UI failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 10: CORS Headers
Write-Host "`n10. Testing CORS Headers" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri 'http://localhost:8085/api/v1/brands' -Method Options
    $corsHeader = $response.Headers['Access-Control-Allow-Origin']
    if ($corsHeader) {
        Write-Host "✅ CORS: Headers present ($corsHeader)" -ForegroundColor Green
    } else {
        Write-Host "⚠️ CORS: Headers not found" -ForegroundColor Yellow
    }
} catch {
    Write-Host "❌ CORS test failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nProduction API Testing Completed!" -ForegroundColor Green
Write-Host "Production URL: http://localhost:8085" -ForegroundColor Cyan
Write-Host "Swagger UI: http://localhost:8085/swagger-ui/index.html" -ForegroundColor Cyan
Write-Host "Actuator: http://localhost:8085/actuator" -ForegroundColor Cyan
