# Script PowerShell pour démarrer le système de monitoring Comptel

Write-Host "🚀 Démarrage du système de monitoring Comptel..." -ForegroundColor Green

# Vérifier que Docker est installé
if (-not (Get-Command docker -ErrorAction SilentlyContinue)) {
    Write-Host "❌ Docker n'est pas installé. Veuillez installer Docker Desktop d'abord." -ForegroundColor Red
    exit 1
}

# Vérifier que Docker Compose est installé
if (-not (Get-Command docker-compose -ErrorAction SilentlyContinue)) {
    Write-Host "❌ Docker Compose n'est pas installé. Veuillez installer Docker Compose d'abord." -ForegroundColor Red
    exit 1
}

# Aller dans le répertoire docker
Set-Location "$PSScriptRoot\..\docker"

Write-Host "📦 Construction des images..." -ForegroundColor Yellow
docker-compose build

Write-Host "🔄 Démarrage des services..." -ForegroundColor Yellow
docker-compose up -d

Write-Host "⏳ Attente du démarrage des services..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

Write-Host "🔍 Vérification des services..." -ForegroundColor Yellow

# Vérifier le backend
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/actuator/health" -UseBasicParsing -TimeoutSec 5
    if ($response.StatusCode -eq 200) {
        Write-Host "✅ Backend Spring Boot: http://localhost:8080" -ForegroundColor Green
        Write-Host "📊 Métriques Prometheus: http://localhost:8080/actuator/prometheus" -ForegroundColor Cyan
    }
} catch {
    Write-Host "❌ Backend Spring Boot n'est pas accessible" -ForegroundColor Red
}

# Vérifier Prometheus
try {
    $response = Invoke-WebRequest -Uri "http://localhost:9090/-/healthy" -UseBasicParsing -TimeoutSec 5
    if ($response.StatusCode -eq 200) {
        Write-Host "✅ Prometheus: http://localhost:9090" -ForegroundColor Green
    }
} catch {
    Write-Host "❌ Prometheus n'est pas accessible" -ForegroundColor Red
}

# Vérifier Grafana
try {
    $response = Invoke-WebRequest -Uri "http://localhost:3001/api/health" -UseBasicParsing -TimeoutSec 5
    if ($response.StatusCode -eq 200) {
        Write-Host "✅ Grafana: http://localhost:3001" -ForegroundColor Green
        Write-Host "👤 Identifiants Grafana: admin/admin" -ForegroundColor Cyan
    }
} catch {
    Write-Host "❌ Grafana n'est pas accessible" -ForegroundColor Red
}

Write-Host ""
Write-Host "🎉 Système de monitoring démarré !" -ForegroundColor Green
Write-Host ""
Write-Host "📋 URLs importantes:" -ForegroundColor White
Write-Host "   • Application: http://localhost:3000" -ForegroundColor Cyan
Write-Host "   • Backend API: http://localhost:8080" -ForegroundColor Cyan
Write-Host "   • Prometheus: http://localhost:9090" -ForegroundColor Cyan
Write-Host "   • Grafana: http://localhost:3001 (admin/admin)" -ForegroundColor Cyan
Write-Host ""
Write-Host "📊 Pour voir les métriques:" -ForegroundColor White
Write-Host "   1. Ouvrez Grafana: http://localhost:3001" -ForegroundColor Yellow
Write-Host "   2. Connectez-vous avec admin/admin" -ForegroundColor Yellow
Write-Host "   3. Le dashboard 'Comptel Application Dashboard' devrait être disponible" -ForegroundColor Yellow
Write-Host ""
Write-Host "🛑 Pour arrêter: docker-compose down" -ForegroundColor Red 