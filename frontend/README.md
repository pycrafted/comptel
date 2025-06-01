# Comptel

[![codecov](https://codecov.io/gh/pycrafted/comptel/branch/develop/graph/badge.svg)](https://codecov.io/gh/pycrafted/comptel)

🌟 Bienvenue dans Comptel ! 🌟
Comptel est une application moderne de comptabilité conçue pour simplifier la gestion financière.
Avec un backend robuste en Spring Boot et une interface élégante en React, elle est facile à utiliser et à configurer.
Ce guide vous accompagne pas à pas pour démarrer, même si vous n’êtes pas expert en informatique ! 🚀

🎯 À quoi sert ce projet ?
Comptel vous permet de :

Gérer vos comptes,

Suivre vos transactions,

Générer des rapports financiers.

Que vous soyez développeur ou novice, l’installation et le lancement sont aussi simples qu’un clic ! 😊

📂 Structure du projet
backend/ : Le cœur de l’application (gère les données et la logique).

frontend/ : L’interface que vous voyez (formulaires, tableaux, etc.).

docker/ : Fichiers pour lancer l’application facilement avec Docker.

scripts/ : Scripts magiques pour automatiser tout ! ✨

docs/ : Guides détaillés pour en savoir plus.

🛠️ Prérequis
Avant de commencer, assurez-vous d’avoir installé :

Java 21
➔ Télécharger ici (Adoptium) (choisissez JDK 21).

Node.js 16+
➔ Télécharger ici (Node.js).

Docker Desktop
➔ Télécharger ici (Docker).

Pas sûr d’avoir tout ? Nos scripts vérifieront pour vous. 😎

🚀 Démarrer en 3 étapes simples
1. 📥 Cloner le projet
Ouvrez un terminal :

Windows : Cherchez "Git Bash".

Mac/Linux : Ouvrez "Terminal".

Clonez le projet :

bash
Copier
Modifier
git clone git@github.com:pycrafted/comptel.git
cd comptel
2. ⚙️ Configurer automatiquement
Exécutez le script adapté :

Windows :

bash
Copier
Modifier
.\scripts\setup.ps1
Mac/Linux :

bash
Copier
Modifier
./scripts/setup.sh
Ce script :

Vérifie que Java, Node.js et Docker sont installés,

Télécharge les dépendances backend et frontend,

Prépare les branches Git.

Si vous voyez "Configuration terminée !", tout est prêt ! 🎉

3. ▶️ Lancer l’application
Deux options s’offrent à vous :

Option 1 : Lancer localement
Windows :

bash
Copier
Modifier
.\scripts\start.ps1
Mac/Linux :

bash
Copier
Modifier
./scripts/start.sh
Cela lance :

Backend : http://localhost:8080

Frontend : http://localhost:3000

Pour arrêter : Ctrl+C dans le terminal.

Option 2 : Lancer avec Docker (recommandé)
Windows :

bash
Copier
Modifier
.\scripts\docker-start.ps1
Mac/Linux :

bash
Copier
Modifier
./scripts/docker-start.sh
Ce script :

Construit et lance tous les services,

Configure automatiquement une base de données PostgreSQL.

Une fois lancé :

Backend : http://localhost:8080

Frontend : http://localhost:3000

Pour arrêter :

bash
Copier
Modifier
cd docker
docker-compose down
🧰 Scripts utiles

Action	Windows	Mac/Linux	Description
Vérifier les prérequis	.\scripts\check-prereqs.ps1	./scripts/check-prereqs.sh	Vérifie que Java, Node.js et Docker sont prêts.
Configurer le projet	.\scripts\setup.ps1	./scripts/setup.sh	Installe toutes les dépendances nécessaires.
Lancer localement	.\scripts\start.ps1	./scripts/start.sh	Démarre backend et frontend sur votre machine.
Lancer avec Docker	.\scripts\docker-start.ps1	./scripts/docker-start.sh	Lance tout via Docker (backend, frontend, BDD).
🐛 Que faire si ça ne marche pas ?
Erreur dans un script : Vérifiez les messages affichés, ils indiquent souvent ce qui manque.

Problème de port : Si 8080 ou 3000 est occupé, fermez d’autres applications ou contactez l’équipe.

Besoin d’aide : Consultez le dossier docs/ ou contactez l’équipe via [canal à insérer, ex. Slack].

🌈 Contribuer à Comptel
Envie d’ajouter une fonctionnalité ou corriger un bug ?

Créez une branche spécifique :

bash
Copier
Modifier
git checkout -b feature/ma-super-fonctionnalite
Poussez vos modifications :

bash
Copier
Modifier
git add .
git commit -m "Description claire du changement"
git push origin feature/ma-super-fonctionnalite
Créez une pull request sur GitHub.

📚 En savoir plus
Découvrez les guides dans docs/ :

Installation : Configurer l’environnement et lancer le projet.

Flux Git : Comment contribuer avec Git.

Architecture : Comprendre les composants techniques.

Scripts : Utiliser les scripts d’automatisation.




## Pour la partie authentification 
**User.java** : Fournit des getters/setters et constructeurs pour gérer les entités utilisateur dans la base de données.

**AccountCredentials.java** : Fournit des getters implicites pour les identifiants envoyés par le client.

**LoginUserController.java** : Gère l'authentification via /login et renvoie un token JWT.

**JwtService.java** : Crée et valide les tokens JWT pour sécuriser les requêtes.

**UseImpl.java**: Charge les détails des utilisateurs pour l'authentification.

**AuthentificationFilter.java** : Valide les tokens JWT dans les requêtes protégées.

**SecurityConfig.java** : Configure les règles de sécurité, CORS, et les composants d'authentification.

---

# Particularités de la branche frontend/khadija

Si la branche frontend/khadija apporte des instructions ou des spécificités pour le frontend (par exemple, configuration supplémentaire, dépendances, scripts, etc.), elles sont à intégrer ici. Sinon, la documentation principale ci-dessus reste valable pour tout le projet.
