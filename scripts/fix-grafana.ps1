# Script pour diagnostiquer et résoudre les problèmes Grafana

Write-Host "🔍 Diagnostic Grafana..." -ForegroundColor Green

# Vérifier l'état des conteneurs
Write-Host "`n📊 État des conteneurs :" -ForegroundColor Yellow
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

# Vérifier les logs Grafana
Write-Host "`n📋 Logs Grafana (dernières 20 lignes) :" -ForegroundColor Yellow
docker logs comptel-grafana --tail 20

# Vérifier l'accès à Prometheus
Write-Host "`n🔗 Test de connexion Prometheus :" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:9090/api/v1/status/targets" -UseBasicParsing
    Write-Host "✅ Prometheus accessible" -ForegroundColor Green
    $targets = $response.Content | ConvertFrom-Json
    Write-Host "Targets actifs : $($targets.data.activeTargets.Count)" -ForegroundColor Green
} catch {
    Write-Host "❌ Prometheus non accessible : $($_.Exception.Message)" -ForegroundColor Red
}

# Vérifier l'accès à Grafana
Write-Host "`n🌐 Test de connexion Grafana :" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:3001/api/health" -UseBasicParsing
    Write-Host "✅ Grafana accessible" -ForegroundColor Green
} catch {
    Write-Host "❌ Grafana non accessible : $($_.Exception.Message)" -ForegroundColor Red
}

# Instructions pour créer le dashboard manuellement
Write-Host "`n📝 Instructions pour créer le dashboard manuellement :" -ForegroundColor Cyan
Write-Host "1. Allez sur http://localhost:3001" -ForegroundColor White
Write-Host "2. Connectez-vous avec admin/admin" -ForegroundColor White
Write-Host "3. Cliquez sur le '+' en haut à gauche" -ForegroundColor White
Write-Host "4. Sélectionnez 'Dashboard'" -ForegroundColor White
Write-Host "5. Cliquez sur 'Add new panel'" -ForegroundColor White
Write-Host "6. Dans la requête, tapez : rate(http_server_requests_seconds_count{application='comptel-backend'}[5m])" -ForegroundColor White

Write-Host "`n🎯 Dashboard créé !" -ForegroundColor Green 