📦 Installation de Comptel
Ce guide explique comment configurer votre environnement pour travailler sur Comptel. Suivez ces étapes pour installer les outils nécessaires, cloner le projet, et le lancer. Pas besoin d’être un expert, on vous guide pas à pas ! 😊

🛠️ Prérequis
Vous aurez besoin de ces outils sur votre ordinateur :

Java 21 :

Téléchargez le JDK 21 depuis Adoptium.
Installez et vérifiez :java -version

Résultat attendu : openjdk 21.x.x.


Node.js 16+ :

Téléchargez depuis Node.js (version 16 ou supérieure).
Vérifiez :node -v
npm -v

Résultat attendu : v16.x.x ou plus, 8.x.x ou plus pour npm.


Docker Desktop :

Téléchargez depuis Docker.
Lancez Docker Desktop et vérifiez :docker --version




Git :

Installez Git depuis git-scm.com.
Vérifiez :git --version





Problème ? Exécutez le script de vérification :

Windows : .\scripts\check-prereqs.ps1
Mac/Linux : ./scripts/check-prereqs.sh


📥 Cloner le projet

Ouvrez un terminal :
Windows : Cherchez "Git Bash" ou "PowerShell".
Mac/Linux : Ouvrez "Terminal".


Clonez le dépôt :git clone https://github.com/pycrafted/comptel.git
cd comptel




⚙️ Configurer le projet
Utilisez nos scripts pour tout configurer automatiquement :

Windows :.\scripts\setup.ps1


Mac/Linux :./scripts/setup.sh



Ce script :

Vérifie les prérequis.
Installe les dépendances backend (Maven) et frontend (npm).
Met à jour les branches Git.


▶️ Lancer le projet
Option 1 : Localement
Pour lancer le backend et le frontend sur votre machine :

Windows :.\scripts\start.ps1


Mac/Linux :./scripts/start.sh



Accédez à :

Backend : http://localhost:8080
Frontend : http://localhost:3000

Arrêtez avec Ctrl+C.
Option 2 : Avec Docker (recommandé)
Pour lancer tout (backend, frontend, base de données) :

Windows :.\scripts\docker-start.ps1


Mac/Linux :./scripts/docker-start.sh



Accédez à :

Backend : http://localhost:8080
Frontend : http://localhost:3000

Arrêtez :
cd docker
docker-compose down


🐛 Dépannage

Erreur de prérequis : Vérifiez les messages du script check-prereqs.
Port occupé (8080, 3000) :netstat -aon | findstr :8080
taskkill /PID <PID> /F


Problème Git : Assurez-vous que votre authentification (HTTPS ou SSH) est configurée.

Besoin d’aide ? Contactez l’équipe !
