🧙‍♂️ Scripts d’automatisation
Les scripts dans scripts/ simplifient la configuration, le lancement, et les tests de Comptel. Ils sont conçus pour gagner du temps et éviter les erreurs, même pour les débutants ! 🚀

---

🛠️ Comment utiliser les scripts

Ouvrir un terminal :
Windows : "Git Bash" ou "PowerShell".
Mac/Linux : "Terminal".

Naviguer dans le projet :
cd chemin/vers/comptel

Exécuter un script :
Exemple (Windows) : .\scripts\setup.ps1
Exemple (Mac/Linux) : ./scripts/setup.sh

---

📋 Détails des scripts

1. Vérifier les prérequis
- `check-prereqs.ps1` (Windows) / `check-prereqs.sh` (Mac/Linux)
- Vérifie que Java, Node.js, npm, et Docker sont installés et fonctionnels.

2. Configurer le projet (installation des dépendances)
- `setup.ps1` (Windows) / `setup.sh` (Mac/Linux)
- Installe les dépendances Maven (backend/) et npm (frontend/), met à jour les branches Git.

3. Lancer localement (développement)
- `start.ps1` (Windows) / `start.sh` (Mac/Linux)
- Lance le backend Spring Boot (http://localhost:8080) et le frontend React (http://localhost:3000) sur votre machine.

4. Lancer avec Docker (recommandé pour test, prod, CI)
- `docker-start.ps1` (Windows) / `docker-start.sh` (Mac/Linux)
- Télécharge et lance les images Docker pré-construites (backend, frontend) depuis Docker Hub, utilise l'image officielle PostgreSQL.

5. Réinitialiser la base de données locale
- `reset-db.ps1` (Windows) / `reset-db.sh` (Mac/Linux)
- Supprime et recrée la base PostgreSQL locale (utile pour repartir de zéro en dev).

6. Générer des données de test
- `populate-db.ps1` (Windows) / `populate-db.sh` (Mac/Linux)
- Appelle l'endpoint `/api/dev/populate` pour insérer des données de test dans la base.

---

❌ **Script supprimé**
- `manage.ps1` : Ce script centralisait installation et lancement local, mais il est redondant avec `setup.ps1` et `start.ps1`, et n'est pas multiplateforme. Utilisez les scripts listés ci-dessus pour chaque étape.

---

💡 **Conseil**
- Pour un usage professionnel ou test rapide : privilégiez Docker (`docker-start.*`)
- Pour du développement local : utilisez `setup.*` puis `start.*`
- Pour la base de données : `reset-db.*` et `populate-db.*` pour repartir de zéro ou insérer des données de test.

---

🐛 Dépannage
- Script échoue : Vérifiez les messages d'erreur. Ils indiquent souvent un prérequis manquant.
- Permissions (Mac/Linux) : Rendez les scripts exécutables : chmod +x scripts/*.sh
- Problème Docker : Assurez-vous que Docker Desktop est en cours d'exécution.

Besoin d'aide ? Consultez docs/installation.md ou contactez l'équipe !
