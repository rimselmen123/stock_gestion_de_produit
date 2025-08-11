# Production Build Script for Stock Management System

Write-Host "🚀 Building Stock Management System for Production..." -ForegroundColor Green

# Set production profile
$env:SPRING_PROFILES_ACTIVE = "prod"

Write-Host "`n📋 Step 1: Cleaning previous builds..." -ForegroundColor Yellow
try {
    mvn clean
    Write-Host "✅ Clean completed successfully" -ForegroundColor Green
} catch {
    Write-Host "❌ Clean failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

Write-Host "`n📋 Step 2: Running tests..." -ForegroundColor Yellow
try {
    mvn test
    Write-Host "✅ Tests completed successfully" -ForegroundColor Green
} catch {
    Write-Host "❌ Tests failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

Write-Host "`n📋 Step 3: Compiling application..." -ForegroundColor Yellow
try {
    mvn compile
    Write-Host "✅ Compilation completed successfully" -ForegroundColor Green
} catch {
    Write-Host "❌ Compilation failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

Write-Host "`n📋 Step 4: Packaging application..." -ForegroundColor Yellow
try {
    mvn package -DskipTests
    Write-Host "✅ Packaging completed successfully" -ForegroundColor Green
} catch {
    Write-Host "❌ Packaging failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

Write-Host "`n📋 Step 5: Verifying JAR file..." -ForegroundColor Yellow
$jarFile = "target/stock-0.0.1-SNAPSHOT.jar"
if (Test-Path $jarFile) {
    $jarSize = (Get-Item $jarFile).Length / 1MB
    Write-Host "✅ JAR file created successfully: $jarFile ($([math]::Round($jarSize, 2)) MB)" -ForegroundColor Green
} else {
    Write-Host "❌ JAR file not found!" -ForegroundColor Red
    exit 1
}

Write-Host "`n🎉 Production build completed successfully!" -ForegroundColor Green
Write-Host "📦 JAR Location: $jarFile" -ForegroundColor Cyan
Write-Host "🚀 To run in production mode: java -jar $jarFile --spring.profiles.active=prod" -ForegroundColor Cyan

# Optional: Start the application for testing
$startApp = Read-Host "`nDo you want to start the application now? (y/n)"
if ($startApp -eq "y" -or $startApp -eq "Y") {
    Write-Host "`n🚀 Starting application in production mode..." -ForegroundColor Green
    java -jar $jarFile --spring.profiles.active=prod
}
