-- Initialisation de la base de données Comptel
-- Ce fichier est exécuté automatiquement lors du premier démarrage du conteneur PostgreSQL

-- Vérifier si la table users existe avant d'essayer d'y insérer des données
DO $$
BEGIN
    IF EXISTS (SELECT FROM information_schema.tables WHERE table_name = 'users') THEN
        -- Suppression des données existantes (si elles existent)
        DELETE FROM users WHERE username = 'admin';
        
        -- Insertion d'un utilisateur administrateur avec mot de passe haché
        -- Mot de passe: admin123 (haché avec BCrypt)
        INSERT INTO users (username, password, role) 
        VALUES ('admin', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW', true);
        
        RAISE NOTICE 'Utilisateur admin inséré avec succès';
    ELSE
        RAISE NOTICE 'Table users non trouvée - sera créée par Hibernate lors du démarrage du backend';
    END IF;
END $$;

-- Affichage de confirmation
SELECT 'Base de données Comptel initialisée avec succès' as status; 