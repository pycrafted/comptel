🏗️ Architecture de Comptel
Ce document présente l’architecture technique de Comptel, pour aider les développeurs à comprendre les composants et leur interaction. C’est une vue d’ensemble simple pour démarrer ! 😊

🌟 Aperçu
Comptel est une application web avec :

Backend : API REST en Spring Boot (Java 21).
Frontend : Interface utilisateur en React.
Base de données : PostgreSQL (via Docker).
Orchestration : Docker Compose (avec images pré-construites depuis Docker Hub).


🛠️ Composants
1. Backend (Spring Boot)

Rôle : Gère la logique métier, les données, et les API.
Technologies :
Java 21, Spring Boot, Spring Security, Spring Data JPA.
Dépendance JJWT pour l’authentification (à venir).
Image Docker : `abdoulayelah/comptel-backend:latest` (Docker Hub).


Dossier : backend/.
Configuration : backend/src/main/resources/application.properties.
Port : 8080.
Endpoints : À venir (ex. : /api/login, /api/register).

2. Frontend (React)

Rôle : Affiche l’interface utilisateur et communique avec le backend via API.
Technologies :
React, Axios (pour les requêtes HTTP).
Image Docker : `abdoulayelah/comptel-frontend:latest` (Docker Hub).


Dossier : frontend/.
Configuration : frontend/src/config.js (URL API).
Port : 3000 (local), 80 (dans le conteneur).
Composants : À venir (ex. : Login.js, Register.js).

3. Base de données (PostgreSQL)

Rôle : Stocke les données (ex. : utilisateurs, transactions).
Technologie : PostgreSQL 15.
Configuration : Définie dans docker/docker-compose.yml.
Connexion :
URL : jdbc:postgresql://db:5432/comptel.
Utilisateur : `${POSTGRES_USER}`.
Mot de passe : `${POSTGRES_PASSWORD}`.
Image Docker : `postgres:15` (officielle).
Volume Docker : `pgdata` pour la persistance.



4. Docker Compose

Rôle : Orchestre backend, frontend, et base de données.
Fichiers :
docker/docker-compose.yml : Configuration des services.
Utilise les images publiées sur Docker Hub pour le backend et le frontend.
Utilise l'image officielle pour la base de données.


Commande :.\scripts\docker-start.ps1  # Windows
./scripts/docker-start.sh   # Mac/Linux




🔗 Interaction des composants

L’utilisateur accède au frontend (http://localhost:3000).
Le frontend envoie des requêtes HTTP (via Axios) au backend (http://localhost:8080/api).
Le backend traite les requêtes, interagit avec PostgreSQL, et renvoie des réponses.
Docker Compose lance tous les services en réseau, avec PostgreSQL accessible via db:5432.


📈 Prochaines étapes

Authentification :
Backend : Implémenter Spring Security avec JWT.
Frontend : Créer des formulaires de connexion/inscription.


Tests : Ajouter des tests unitaires (backend/frontend).
CI/CD : Automatiser le build/push des images Docker.
Monitoring et logs centralisés.


🐛 Dépannage

Backend ne se connecte pas à la DB : Vérifiez les variables d’environnement dans docker-compose.yml.
Frontend ne charge pas : Vérifiez les logs Docker :
```sh
cd docker
docker-compose logs
```



Besoin d’aide ? Consultez l’équipe ou docs/installation.md.

🔗 **Schéma d’architecture**

```
[Utilisateur]
    |
    v
[Frontend (React, port 3000)] <--- Docker image: abdoulayelah/comptel-frontend:latest
    |
    v
[Backend (Spring Boot, port 8080)] <--- Docker image: abdoulayelah/comptel-backend:latest
    |
    v
[Base de données (PostgreSQL, port 5432)] <--- Docker image: postgres:15 (officielle)
```

Tous les services sont orchestrés via Docker Compose et communiquent sur un réseau interne Docker.
