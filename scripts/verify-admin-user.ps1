# Script de vérification de l'utilisateur admin (PowerShell)
# Usage: .\verify-admin-user.ps1 <backend-url>

param(
    [string]$BackendUrl = "http://localhost:8080"
)

Write-Host "🔍 Vérification de l'utilisateur admin sur $BackendUrl" -ForegroundColor Cyan

# Attendre que le backend soit prêt
Write-Host "⏳ Attente du démarrage du backend..." -ForegroundColor Yellow
for ($i = 1; $i -le 30; $i++) {
    try {
        $response = Invoke-WebRequest -Uri "$BackendUrl/actuator/health" -Method GET -UseBasicParsing
        if ($response.StatusCode -eq 200) {
            Write-Host "✅ Backend est prêt!" -ForegroundColor Green
            break
        }
    }
    catch {
        Write-Host "⏳ Tentative $i/30..." -ForegroundColor Yellow
        Start-Sleep -Seconds 2
    }
}

# Test de connexion avec l'utilisateur admin
Write-Host "🔐 Test de connexion avec admin/admin..." -ForegroundColor Cyan

$loginBody = @{
    username = "admin"
    password = "admin"
} | ConvertTo-Json

try {
    $loginResponse = Invoke-WebRequest -Uri "$BackendUrl/login" -Method POST -Body $loginBody -ContentType "application/json" -UseBasicParsing
    
    if ($loginResponse.Content -match "token") {
        Write-Host "✅ Connexion réussie! L'utilisateur admin/admin fonctionne correctement." -ForegroundColor Green
        Write-Host "🎉 Votre application est prête à être utilisée!" -ForegroundColor Green
        Write-Host ""
        Write-Host "📝 Informations de connexion:" -ForegroundColor White
        Write-Host "   Username: admin" -ForegroundColor White
        Write-Host "   Password: admin" -ForegroundColor White
        Write-Host "   URL Backend: $BackendUrl" -ForegroundColor White
        Write-Host ""
        Write-Host "⚠️  IMPORTANT: Changez le mot de passe admin après la première connexion!" -ForegroundColor Red
    }
    else {
        Write-Host "❌ Échec de la connexion. Vérifiez les logs du backend." -ForegroundColor Red
        Write-Host "Réponse reçue: $($loginResponse.Content)" -ForegroundColor Red
        exit 1
    }
}
catch {
    Write-Host "❌ Erreur lors de la connexion: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
} 