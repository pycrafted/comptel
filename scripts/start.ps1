Write-Host "Lancement de Comptel (backend + frontend)..."

# Vérifier les prérequis
& .\scripts\check-prereqs.ps1
if ($LASTEXITCODE -ne 0) { exit 1 }

# Lancer le backend
Set-Location backend
Start-Process -NoNewWindow -FilePath ".\mvnw" -ArgumentList "spring-boot:run"
Start-Sleep -Seconds 5  # Attendre que le backend démarre
Write-Host "Backend lancé sur http://localhost:8080" -ForegroundColor Green
Set-Location ..

# Lancer le frontend
Set-Location frontend
Start-Process -NoNewWindow -FilePath "npm" -ArgumentList "start"
Write-Host "Frontend lancé sur http://localhost:3000" -ForegroundColor Green
Set-Location ..

Write-Host "Appuyez sur Ctrl+C pour arrêter..."
pause