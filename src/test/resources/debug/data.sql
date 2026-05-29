
-- 🎮 Players
INSERT INTO player (id, nickname, fullname) VALUES
                                                (1, 'Ludovick', 'Ludovica'),
                                                (2, 'Capoz', 'Cristian'),
                                                (3, 'Vanja', 'Ivan');

-- 🏆 Championship
INSERT INTO championship (id, championship_name, starting_date, ending_date) VALUES
    (1, 'La casa di terraforming mars', '2025-06-01', '2026-08-31');

-- 🎲 Game
INSERT INTO game (id, location, game_date, championship_id) VALUES
    (1, 'The House', '2025-06-14', 1);

-- 🧮 Points per player
INSERT INTO points (
    id, terraforming_rating, greenery, city, milestones, awards, cards,
    other_categories, corporation, first_prelude, second_prelude,
    game, player_id
) VALUES
-- Ludovica
(1, 26, 6, 11, 5, 2, 25, '{"politics": "3"}', 'THORGATE', 'POWER_GENERATION', 'EARLY_SETTLEMENT', 1,  1),

-- Cristian
(2, 30, 12, 16, 5, 5, 27, '{"politics": "1"}', 'ECOLINE', 'MARTIAN_INDUSTRIES', 'GALILEAN_MINING', 1,  2),

-- Ivan
(3, 26, 5, 10, 5, 0, 13, '{}', 'HELION', 'MOHOLE_EXCAVATION', 'POLAR_INDUSTRIES', 1,  3);

insert into drafting (id, draftings, player_id,championship) values 
                                                                 (1, '
                             {"1":{"firstExpansion":"Venus Next","secondExpansion":"Prelude", "chosenExpansion": "Venus Next"}, 
                      
                             "2":{"firstExpansion":"Corporate Era","secondExpansion":"Prelude"}, 
                      
                             "3":{"firstExpansion":"Colonies","secondExpansion":"Prelude"},
                                                                     "4":{"firstExpansion":"Colonies","secondExpansion":"Prelude"}, 
                      
                             "5":{"firstExpansion":"Colonies","secondExpansion":"Prelude"}, 
                      
                             "6":{"firstExpansion":"Colonies","secondExpansion":"Prelude"}, 
                      
                             "7":{"firstExpansion":"Colonies","secondExpansion":"Prelude"}, 
                      
                             "8":{"firstExpansion":"Colonies","secondExpansion":"Prelude"}, 
                      
                             "9":{"firstExpansion":"Colonies","secondExpansion":"Prelude"}, 
                      
                             "10":{"firstExpansion":"Colonies","secondExpansion":"Prelude"}}',
                                                                  
                                                                  2, 1);
insert into drafting (id, draftings, player_id,championship) values
    (2, '
                             {"1":{"firstExpansion":"Venus Next","secondExpansion":"Prelude"}, 
                      
                             "2":{"firstExpansion":"Corporate Era","secondExpansion":"Prelude"}, 
                      
                             "3":{"firstExpansion":"Colonies","secondExpansion":"Prelude"},
                                                                     "4":{"firstExpansion":"Colonies","secondExpansion":"Prelude"}}',

     1, 1);