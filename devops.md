# Bonnes pratiques DevOps pour le projet Comptel

---

## Plan d'action priorisé DevOps

1. **Renforcer la CI (Intégration Continue)**
   - Ajouter le build/test du frontend dans GitHub Actions.
   - Générer un rapport de couverture de tests (backend et frontend).
   - Ajouter des checks de lint (eslint pour le frontend, checkstyle/spotbugs pour le backend).

2. **Sécuriser la configuration et les secrets**
   - Mettre en place des fichiers `.env` pour chaque environnement.
   - Documenter toutes les variables d'environnement nécessaires.
   - S'assurer qu'aucun secret n'est versionné.

3. **Améliorer la conteneurisation**
   - Ajouter des healthchecks dans docker-compose.
   - Optimiser les Dockerfile (multi-stage, .dockerignore).
   - Utiliser des variables d'environnement dans docker-compose.

4. **Documentation et automatisation**
   - Générer automatiquement la documentation API (Swagger/OpenAPI).
   - Ajouter des scripts pour le reset de la base, la génération de données de test.
   - Ajouter des templates de PR et d'issues.

5. **Observabilité et sécurité**
   - Scanner automatiquement les dépendances (npm audit, Snyk, Dependabot).
   - Ajouter des logs structurés et prévoir la centralisation.
   - Préparer l'intégration d'une stack de monitoring (Prometheus, Grafana, ELK).

---

## 1. Intégration Continue (CI)
- Utiliser GitHub Actions pour lancer automatiquement les builds et les tests à chaque push/pull request sur develop et main.
- Exécuter les tests unitaires et d'intégration backend (Maven + PostgreSQL en service CI).
- Cacher les dépendances Maven pour accélérer les builds.
- Refuser les merges sur develop/main si les tests échouent.

**Améliorations recommandées :**
- Ajouter le build/test du frontend (npm run build, npm test) dans la CI.
- Générer un rapport de couverture de tests (backend et frontend) et l'afficher dans les PR.
- Ajouter des checks de lint (eslint pour le frontend, checkstyle/spotbugs pour le backend).
- Notifier sur Slack/Teams en cas d'échec de build.

## 2. Livraison Continue (CD)
- Prévoir un pipeline de déploiement automatique (Docker, scripts ou GitHub Actions) pour staging/production.
- Générer et publier des images Docker pour backend et frontend à chaque release.
- Utiliser docker-compose pour l'orchestration locale et en CI.

**Améliorations recommandées :**
- Ajouter un pipeline de déploiement automatique pour staging/prod.
- Publier les images Docker sur un registre (Docker Hub, GitHub Packages).
- Déployer automatiquement sur un environnement de test à chaque merge sur develop.

## 3. Conteneurisation
- Utiliser des Dockerfile multi-étapes pour backend (Java) et frontend (React).
- Centraliser la configuration multi-service dans docker-compose.yml (backend, frontend, db).
- Versionner les images et utiliser des tags clairs.
- Stocker les données PostgreSQL dans un volume Docker dédié.

**Améliorations recommandées :**
- Ajouter des healthchecks dans docker-compose pour chaque service.
- Utiliser des variables d'environnement pour la configuration (pas de valeurs en dur).
- Optimiser la taille des images (multi-stage, .dockerignore).

## 4. Gestion de la configuration
- Ne jamais versionner les secrets (utiliser des variables d'environnement ou un vault).
- Utiliser des fichiers .env pour la configuration locale (et les exclure du git).
- Documenter toutes les variables d'environnement nécessaires dans le README ou devops.md.

**Améliorations recommandées :**
- Utiliser des fichiers `.env` pour chaque environnement (local, CI, prod).
- Intégrer un gestionnaire de secrets (Vault, GitHub Secrets, etc.).
- Documenter toutes les variables dans le README/devops.md.

## 5. Qualité et tests
- Écrire des tests unitaires et d'intégration pour le backend (JUnit, Spring Boot).
- Écrire des tests de composants et d'intégration pour le frontend (React Testing Library, Jest).
- Exiger une couverture minimale de tests (>80%) pour merger sur develop/main.
- Lancer les tests automatiquement en CI.

**Améliorations recommandées :**
- Ajouter des tests d'intégration et de composants côté frontend.
- Exiger une couverture minimale (fail build si <80%).
- Générer des badges de build et de couverture dans le README.

## 6. Documentation
- Maintenir une documentation technique à jour (architecture, installation, scripts, workflow Git).
- Documenter le workflow Git (branches, PR, conventions de commit).
- Documenter les endpoints API (Swagger ou markdown).

**Améliorations recommandées :**
- Générer automatiquement la doc API (Swagger/OpenAPI pour le backend).
- Ajouter des exemples d'utilisation d'API (curl, Postman).
- Maintenir un changelog automatique (conventional commits + release notes).

## 7. Workflow Git
- Utiliser un flux Git basé sur les branches (main, develop, feature/*, bugfix/*, hotfix/*).
- Interdire les commits directs sur main et develop (protection GitHub).
- Exiger une PR avec revue et tests verts pour merger.
- Supprimer les branches après merge.

**Améliorations recommandées :**
- Automatiser le formatage des messages de commit (commitlint, husky).
- Générer un changelog à chaque release.
- Ajouter des templates de PR et d'issues.

## 8. Sécurité
- Protéger les endpoints API par JWT ou Basic Auth.
- Ne jamais exposer de secrets dans le code ou les logs.
- Mettre à jour régulièrement les dépendances (npm audit, mvn versions:display-dependency-updates).

**Améliorations recommandées :**
- Scanner automatiquement les dépendances (npm audit, Snyk, Dependabot).
- Ajouter des tests de sécurité (OWASP ZAP, SonarQube).
- Forcer HTTPS en production.

## 9. Observabilité
- Ajouter des logs structurés côté backend (niveau INFO/WARN/ERROR).
- Prévoir l'intégration future d'une stack de monitoring (Prometheus, Grafana, ELK).

**Améliorations recommandées :**
- Centraliser les logs (ELK, Grafana Loki).
- Ajouter des métriques (Prometheus, Actuator).
- Mettre en place des alertes sur erreurs critiques.

## 10. Automatisation & scripts
- Fournir des scripts pour le setup, le lancement local, les tests, le build Docker.
- Vérifier les prérequis automatiquement (Java, Node, Docker, etc.).

**Améliorations recommandées :**
- Ajouter des scripts pour le reset de la base, la génération de données de test.
- Rendre les scripts multiplateformes (Windows/Mac/Linux).
- Ajouter des scripts pour le build/push Docker.

---

**À mettre en place ou à renforcer dans ce projet :**
- Ajout de tests frontend plus poussés (composants, intégration).
- Génération automatique de la documentation API (Swagger ou équivalent).
- Pipeline de déploiement continu (CD) pour staging/production.
- Surveillance automatisée des dépendances et des vulnérabilités.
- Centralisation des logs et monitoring (à planifier).

---

*Ce fichier sera enrichi au fil de l'évolution du projet et des besoins DevOps. Priorisez les améliorations selon les besoins de l'équipe et l'impact sur la qualité et la productivité.*

---

## Gestion des secrets et variables d'environnement

- Utilisez des fichiers `.env` pour stocker les variables sensibles (mots de passe, clés, URLs, etc.)
- Ne versionnez jamais vos fichiers `.env` (ils sont ignorés par git)
- Exemple pour le backend (`backend/.env.example`) :
  ```env
  SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/comptel
  SPRING_DATASOURCE_USERNAME=postgres
  SPRING_DATASOURCE_PASSWORD=changeme
  JWT_SECRET=changeme
  ```
- Exemple pour le frontend (`frontend/.env.example`) :
  ```env
  REACT_APP_API_URL=http://localhost:8080/api
  ```
- Copiez le fichier `.env.example` en `.env` et adaptez les valeurs à votre environnement local ou CI.
- En production, utilisez un gestionnaire de secrets (Vault, GitHub Secrets, etc.)

## Intégration de la couverture de tests avec Codecov

- La CI upload automatiquement les rapports de couverture (backend et frontend) vers Codecov à chaque build.
- Pour activer Codecov :
  1. Crée un compte gratuit sur https://codecov.io et connecte ton repo GitHub.
  2. Récupère le token `CODECOV_TOKEN` fourni par Codecov.
  3. Ajoute ce token dans les secrets GitHub du repo (`Settings > Secrets and variables > Actions > New repository secret`).
- Le badge de couverture est affiché en haut du README :
  ```markdown
  [![codecov](https://codecov.io/gh/pycrafted/comptel/branch/develop/graph/badge.svg)](https://codecov.io/gh/pycrafted/comptel)
  ```
- Le badge se met à jour automatiquement à chaque build.
- Tu peux consulter les détails de la couverture sur https://codecov.io/gh/pycrafted/comptel 