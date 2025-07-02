-- Script d'initialisation des données par défaut
-- Cet utilisateur sera créé seulement si la table users est vide

INSERT INTO users (username, password, role) 
SELECT 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', true
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin'); 