📦 Installation de Comptel

Ce guide explique comment configurer et lancer Comptel de façon professionnelle, en privilégiant l'utilisation d'images Docker pré-construites pour le backend et le frontend, et l'image officielle pour la base de données.

🛠️ Prérequis
- **Docker Desktop** (recommandé)
- (Pour développement local : Java 21, Node.js 16+, Git)

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


▶️ Lancer Comptel avec Docker (recommandé)

1. Placez-vous dans le dossier `docker/` :
   ```sh
   cd docker
   ```
2. Téléchargez les images et démarrez la stack :
   ```sh
   docker-compose pull
   docker-compose up -d
   ```
3. Accédez à :
   - Backend : http://localhost:8080
   - Frontend : http://localhost:3000

Pour arrêter :
```sh
cd docker
docker-compose down
```

💡 **Pourquoi cette méthode ?**
- Plus besoin de builder localement : images prêtes à l'emploi.
- Images identiques pour tous (dev, CI, prod).
- Base de données sécurisée et maintenue (Postgres officielle).

▶️ Lancer localement (pour développement)
Voir la section "Développement local" ou docs/scripts.md pour les scripts d'automatisation.

🐛 Dépannage
- Problème Docker : Vérifiez que Docker Desktop est lancé.
- Port occupé : Fermez les applications utilisant 8080 ou 3000.
- Problème d'authentification : Consultez la documentation sur l'authentification JWT.

Pour toute question, contactez l'équipe ou ouvrez une issue sur GitHub.
