🧙‍♂️ Scripts d’automatisation
Les scripts dans scripts/ simplifient la configuration, le lancement, et les tests de Comptel. Ils sont conçus pour gagner du temps et éviter les erreurs, même pour les débutants ! 🚀


![](C:\Users\pc\Documents\C.png)

🛠️ Comment utiliser les scripts

Ouvrir un terminal :
Windows : "Git Bash" ou "PowerShell".
Mac/Linux : "Terminal".


Naviguer dans le projet :cd chemin/vers/comptel


Exécuter un script :
Exemple (Windows) :.\scripts\setup.ps1


Exemple (Mac/Linux) :./scripts/setup.sh






📋 Détails des scripts
1. Vérifier les prérequis

Commande :
Windows : .\scripts\check-prereqs.ps1
Mac/Linux : ./scripts/check-prereqs.sh


Action : Vérifie que Java, Node.js, npm, et Docker sont installés et fonctionnels.
Sortie : Messages indiquant si chaque outil est OK ou s’il faut l’installer.

2. Configurer le projet

Commande :
Windows : .\scripts\setup.ps1
Mac/Linux : ./scripts/setup.sh


Action :
Clone le dépôt (si nécessaire).
Installe les dépendances Maven (backend/) et npm (frontend/).
Met à jour les branches Git (develop, feature/authentification, etc.).


Sortie : “Configuration terminée !”

3. Lancer localement

Commande :
Windows : .\scripts\start.ps1
Mac/Linux : ./scripts/start.sh


Action :
Lance le backend Spring Boot (http://localhost:8080).
Lance le frontend React (http://localhost:3000).


Sortie : URLs d’accès et PIDs des processus.

4. Lancer avec Docker

Commande :
Windows : .\scripts\docker-start.ps1
Mac/Linux : ./scripts/docker-start.sh


Action :
Construit et lance les services Docker (backend, frontend, PostgreSQL).


Sortie : Logs des services, accès via http://localhost:8080 (backend) et http://localhost:3000 (frontend).


🐛 Dépannage

Script échoue : Vérifiez les messages d’erreur. Ils indiquent souvent un prérequis manquant.
Permissions (Mac/Linux) : Rendez les scripts exécutables :chmod +x scripts/*.sh


Problème Docker : Assurez-vous que Docker Desktop est en cours d’exécution.

Besoin d’aide ? Consultez docs/installation.md ou contactez l’équipe !
