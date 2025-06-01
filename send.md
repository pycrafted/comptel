## 🟨 Actions réalisées

## Actions à faire

- Réactiver et fiabiliser tous les pipelines CI/CD (tests, build, release, déploiement)
- Ajouter la CI pour le frontend (tests, lint, build)
- Générer automatiquement la documentation API (Swagger/OpenAPI)
- Centraliser les logs et la supervision (Prometheus, Grafana, ELK)
- Sécuriser la gestion des secrets (fichiers .env, GitHub Secrets)
- Maintenir la documentation à jour à chaque évolution

## Actions terminées

- Correction de la connexion du backend à PostgreSQL dans application.properties (`localhost` → `db`)
- Ajout de l'accès public à `/actuator/health` dans SecurityConfig.java pour le healthcheck Docker
- Rebuild et push de l'image Docker backend après chaque modification critique
- Vérification de la copie correcte de application.properties dans l'image Docker
- Documentation de toutes les images Docker utilisées
- Vérification et documentation de l'orchestration Docker Compose
- Mise à jour de la documentation technique
- Build et push des images Docker backend et frontend sur Docker Hub (`abdoulayelah/comptel-backend:latest`, `abdoulayelah/comptel-frontend:latest`)
- Mise en place et documentation du pipeline Backend Security Scan (OWASP ZAP)

## Actions en cours

- Création pipeline Backend CI : Lint, build, tests, couverture, analyse statique, scan dépendances, notification échec
- Création pipeline Backend Tests : Build et tests Maven avec PostgreSQL en service
- Création pipeline Backend Docker Image : Build/push image Docker backend sur Docker Hub, scan Trivy
- Création pipeline Backend Deploy : Déploiement automatique du backend sur serveur distant via SSH et Docker
- Création pipeline Release Please Changelog : Génération automatique du changelog et gestion des releases
- Génération et upload de rapports JaCoCo (couverture), JUnit (tests)
- Intégration Codecov pour la couverture de tests
- Analyse statique SpotBugs
- Linting Checkstyle
- Scan de sécurité Trivy sur les dépendances et images Docker
- Notification par mail en cas d'échec de build
- Gestion des secrets via GitHub Secrets dans les pipelines
---

**Ce format est prêt à être copié/collé dans Trello, chaque section correspondant à une colonne, chaque point à une carte.** 