-- Suppression des données existantes
DELETE FROM users;

-- Insertion d'un utilisateur administrateur avec mot de passe haché
INSERT INTO users (username, password, role) 
VALUES ('admin', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW', true); 