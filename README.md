🌟 Bienvenue dans Comptel ! 🌟

Comptel est une application moderne de comptabilité conçue pour simplifier la gestion financière. Avec un backend robuste en Spring Boot et une interface utilisateur élégante en React, elle est facile à utiliser et à configurer. Ce guide vous accompagne pas à pas pour démarrer, même si vous n’êtes pas expert en informatique ! 🚀



🎯 À quoi sert ce projet ?

Comptel vous permet de gérer vos comptes, suivre vos transactions, et générer des rapports financiers. Que vous soyez développeur ou novice, ce guide rend l’installation et le lancement aussi simples qu’un clic ! 😊



📂 Structure du projet

Voici comment le projet est organisé :





backend/ : Le cœur de l’application (gère les données et la logique).



frontend/ : L’interface que vous voyez (formulaires, tableaux, etc.).



docker/ : Fichiers pour lancer l’application facilement avec Docker.



scripts/ : Scripts magiques pour automatiser tout ! ✨



docs/ : Guides détaillés pour en savoir plus.



🛠️ Prérequis : Ce dont vous avez besoin

Avant de commencer, assurez-vous d’avoir ces outils installés sur votre ordinateur :





Java 21 : Nécessaire pour le backend.





Téléchargez-le ici : Adoptium (choisissez JDK 21).



Node.js 16+ : Pour l’interface utilisateur.





Téléchargez-le ici : Node.js (version 16 ou plus).



Docker Desktop : Pour lancer tout en un clic.





Téléchargez-le ici : Docker.

Pas sûr d’avoir tout ? Pas de panique ! Nos scripts vérifieront pour vous. 😎



🚀 Démarrer en 3 étapes simples

1. 📥 Cloner le projet

Téléchargez le code sur votre ordinateur :





Ouvrez un terminal :





Windows : Cherchez "Git Bash" dans le menu Démarrer.



Mac/Linux : Ouvrez l’application "Terminal".



Tapez cette commande pour cloner le projet :

git clone git@github.com:pycrafted/comptel.git



Entrez dans le dossier du projet :

cd comptel

2. ⚙️ Configurer automatiquement

Nos scripts font tout le travail pour vous ! Exécutez le script correspondant à votre système :





Windows :

.\scripts\setup.ps1



Mac/Linux :

./scripts/setup.sh

Ce script :





Vérifie que Java, Node.js, et Docker sont installés.



Télécharge les dépendances pour le backend et le frontend.



Prépare les branches Git.

Si vous voyez un message "Configuration terminée !", tout est prêt ! 🎉

3. ▶️ Lancer l’application

Vous avez deux options pour lancer Comptel : localement ou avec Docker.

Option 1 : Lancer localement (backend + frontend)

Pour voir l’application sur votre ordinateur sans Docker :





Windows :

.\scripts\start.ps1



Mac/Linux :

./scripts/start.sh

Ce script lance :





Le backend sur http://localhost:8080.



Le frontend sur http://localhost:3000.

Ouvrez votre navigateur et allez à http://localhost:3000 pour voir l’interface ! 🌐

Pour arrêter, appuyez sur Ctrl+C dans le terminal.

Option 2 : Lancer avec Docker (recommandé)

Pour une expérience complète (backend, frontend, base de données) :





Windows :

.\scripts\docker-start.ps1



Mac/Linux :

./scripts/docker-start.sh

Ce script :





Construit et lance tous les services.



Configure une base de données PostgreSQL automatiquement.

Une fois lancé :





Backend : http://localhost:8080.



Frontend : http://localhost:3000.

Pour arrêter, appuyez sur Ctrl+C, puis tapez :

cd docker
docker-compose down



🧰 Scripts utiles

Les scripts dans le dossier scripts/ sont vos meilleurs amis pour travailler sur Comptel. Voici ce qu’ils font :







Script



Windows



Mac/Linux



Description





Vérifier les prérequis



.\scripts\check-prereqs.ps1



./scripts/check-prereqs.sh



Confirme que Java, Node.js, et Docker sont prêts.





Configurer le projet



.\scripts\setup.ps1



./scripts/setup.sh



Installe tout ce dont vous avez besoin.





Lancer localement



.\scripts\start.ps1



./scripts/start.sh



Démarre backend et frontend sur votre machine.





Lancer avec Docker



.\scripts\docker-start.ps1



./scripts/docker-start.sh



Lance tout avec Docker.



🐛 Que faire si ça ne marche pas ?





Erreur dans un script ? Vérifiez les messages affichés. Ils indiquent souvent quoi installer (Java, Node.js, Docker).



Problème de port ? Si 8080 ou 3000 est occupé, arrêtez d’autres applications ou contactez l’équipe.



Besoin d’aide ? Consultez docs/ pour des guides détaillés ou contactez l’équipe via [insérer canal, ex. Slack].



🌈 Contribuer à Comptel

Vous voulez ajouter une fonctionnalité ou corriger un bug ? Voici comment :





Travaillez sur une branche spécifique (ex. : feature/authentification-backend).



Poussez vos modifications avec :

git add .
git commit -m "Description claire de votre changement"
git push origin <votre-branche>



Créez une pull request sur GitHub pour que l’équipe valide.



📚 En savoir plus





Consultez docs/installation.md pour des détails techniques.



Lisez docs/git-workflow.md pour comprendre notre flux Git.



Posez vos questions à l’équipe !



Prêt à explorer Comptel ? Lancez les scripts et plongez dans l’aventure ! 🚀