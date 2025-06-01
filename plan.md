# Plan de liaison backend ↔ frontend

---

**Suivi d'avancement (mise à jour continue)**

- ✅ Plan général rédigé
- ✅ Section 8.1 (authentification /login) détaillée
- 🟠 Section 8.2 (Inputs /api/inputs) : en cours de rédaction (aperçu disponible)
- ⏳ Prochaine étape : Exits (/api/exits)
- À suivre : Invoices (/api/invoices), Services (/api/services)
- 🟢 Le plan sera enrichi page par page, endpoint par endpoint, avec mapping frontend/backend et recommandations précises

*Travail en cours, plan mis à jour régulièrement. Vous pouvez demander un aperçu à tout moment.*

---

## 1. Analyse de l'existant

### Frontend
- React (Create React App)
- axios pour les requêtes HTTP
- API_URL défini dans src/config.js
- Structure actuelle : squelette de base, pas de pages personnalisées

### Backend
- Spring Boot (Java)
- Contrôleurs exposés :
  - /login (authentification, JWT)
  - /api/inputs (entrées)
  - /api/exits (sorties)
  - /api/invoices (factures)
  - /api/services (services)
- Réponses JSON, endpoints REST

---

## 2. Objectif
Permettre au frontend React de consommer les API REST du backend Spring Boot pour :
- Authentifier un utilisateur (JWT)
- Afficher, créer, modifier, supprimer des entrées, sorties, factures, services
- Gérer l'état connecté/déconnecté côté frontend

---

## 3. Étapes détaillées

### 3.1. Authentification (JWT)
- Créer une page de login côté frontend
- POST /login avec username/password
- Récupérer le JWT dans l'en-tête Authorization
- Stocker le token (localStorage ou memory)
- Ajouter le token dans l'en-tête Authorization pour chaque requête future

### 3.2. Pages principales à créer côté frontend
- LoginPage : formulaire d'authentification
- Dashboard : page d'accueil après connexion
- InputsPage : gestion des entrées (/api/inputs)
- ExitsPage : gestion des sorties (/api/exits)
- InvoicesPage : gestion des factures (/api/invoices)
- ServicesPage : gestion des services (/api/services)

### 3.3. Navigation
- Installer react-router-dom
- Protéger les routes (redirection vers login si non authentifié)

### 3.4. Connexion aux API
- Créer un utilitaire axios configuré avec API_URL et le header Authorization
- Créer des fonctions pour chaque ressource (getInputs, createInput, etc.)

### 3.5. Gestion des erreurs et de l'état
- Afficher les messages d'erreur backend côté frontend
- Gérer le loading, le succès, l'échec pour chaque action

### 3.6. Sécurité
- S'assurer que le token JWT est bien envoyé à chaque requête protégée
- Gérer l'expiration du token (déconnexion automatique si besoin)

### 3.7. Tests
- Vérifier la connexion/déconnexion
- Tester chaque page (CRUD complet)
- Vérifier la cohérence des données affichées

---

## 4. Plan d'action technique

1. Installer les dépendances manquantes côté frontend (ex : react-router-dom)
2. Créer la structure des pages et composants React
3. Développer la logique d'authentification (login, stockage du token, protection des routes)
4. Développer les appels API pour chaque ressource
5. Créer les formulaires et listes pour chaque entité (Input, Exit, Invoice, Service)
6. Tester l'ensemble du flux utilisateur
7. Documenter l'intégration dans le README

---

## 5. Proposition de structure de fichiers frontend

src/
  pages/
    LoginPage.js
    Dashboard.js
    InputsPage.js
    ExitsPage.js
    InvoicesPage.js
    ServicesPage.js
  components/
    InputForm.js
    ExitForm.js
    InvoiceForm.js
    ServiceForm.js
    Navbar.js
  api/
    axios.js
    inputs.js
    exits.js
    invoices.js
    services.js
  config.js
  App.js
  index.js

---

## 6. Exemple de flux utilisateur

1. L'utilisateur arrive sur /login
2. Il saisit ses identifiants, clique sur "Se connecter"
3. Le frontend envoie la requête à /login, récupère le JWT
4. Le JWT est stocké, l'utilisateur est redirigé vers /dashboard
5. Il peut naviguer vers les pages Entrées, Sorties, Factures, Services
6. Chaque page affiche les données en provenance du backend, permet d'ajouter/modifier/supprimer

---

## 7. Points de vigilance

- Bien gérer les erreurs d'authentification et d'API
- Toujours protéger les routes sensibles côté frontend
- Tester la cohérence des données entre frontend et backend

---

## 8. Analyse détaillée des endpoints backend

### 8.1 Authentification (/login)

- **Méthode** : POST
- **URL** : /login
- **Payload attendu** :
  ```json
  {
    "username": "...",
    "password": "..."
  }
  ```
- **Réponse** :
  - 200 OK, header `Authorization: Bearer <jwt>`
  - Pas de body, le token JWT est dans l'en-tête
- **Erreurs possibles** :
  - 401 Unauthorized si identifiants invalides
  - 400 Bad Request si payload mal formé
- **À faire côté frontend** :
  - Créer un formulaire de login
  - Envoyer la requête POST
  - Récupérer le JWT dans l'en-tête de la réponse
  - Stocker le JWT (localStorage ou memory)
  - Gérer les erreurs d'authentification

### 8.2 Entrées (/api/inputs)

- **GET /api/inputs**
  - Récupère la liste de toutes les entrées
  - Réponse : tableau d'objets Input
  - À faire côté frontend : afficher la liste (InputsPage)

- **GET /api/inputs/{id}**
  - Récupère une entrée par son ID
  - Réponse : objet Input
  - À faire côté frontend : afficher les détails, préremplir le formulaire d'édition

- **GET /api/inputs/by-date-range?start=...&end=...**
  - Récupère les entrées dans une plage de dates
  - Paramètres : start, end (format ISO)
  - Réponse : tableau d'objets Input
  - À faire côté frontend : filtrer la liste par date

- **GET /api/inputs/by-mode/{mode}**
  - Récupère les entrées par mode de paiement
  - Paramètre : mode (ex : CASH, CARD, etc.)
  - Réponse : tableau d'objets Input
  - À faire côté frontend : filtrer la liste par mode

- **GET /api/inputs/by-user/{userId}**
  - Récupère les entrées d'un utilisateur
  - Paramètre : userId
  - Réponse : tableau d'objets Input
  - À faire côté frontend : vue personnalisée utilisateur

- **POST /api/inputs**
  - Crée une nouvelle entrée
  - Payload :
    ```json
    {
      "titres": "...",
      "montants": 123.45,
      "modePaiement": "CASH",
      "saveBy": 1
    }
    ```
  - Réponse : succès + objet créé
  - À faire côté frontend : formulaire de création, affichage du succès/erreur

- **PUT /api/inputs/{id}**
  - Modifie une entrée existante
  - Payload :
    ```json
    {
      "titres": "...",
      "montants": 123.45,
      "modePaiement": "CARD"
    }
    ```
  - Réponse : succès + objet modifié
  - À faire côté frontend : formulaire d'édition

- **DELETE /api/inputs/{id}**
  - Supprime une entrée
  - Réponse : succès
  - À faire côté frontend : bouton de suppression, confirmation

- **Gestion des erreurs**
  - 400 Bad Request si données invalides
  - 404 Not Found si ID inexistant
  - À faire côté frontend : afficher les messages d'erreur

*Aperçu en cours, section à compléter avec exemples de données et mapping précis avec les composants React.* 