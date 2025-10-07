-- Sequenza usata da Hibernate
CREATE SEQUENCE hibernate_sequence START WITH 1;

-- Tabella dei campionati
CREATE TABLE championship
(
    id                BIGINT PRIMARY KEY,
    championship_name VARCHAR(255),
    starting_date     TIMESTAMP,
    ending_date       TIMESTAMP,
    CONSTRAINT uc_championship_name UNIQUE (championship_name)
);

-- Tabella delle partite
CREATE TABLE game
(
    id              BIGINT PRIMARY KEY,
    location        VARCHAR(255),
    game_date       TIMESTAMP,
    championship_id BIGINT NOT NULL,
    CONSTRAINT fk_game_championship FOREIGN KEY (championship_id) REFERENCES championship (id)
);

-- Tabella dei giocatori
CREATE TABLE player
(
    id       BIGINT       NOT NULL,
    nickname VARCHAR(255) NOT NULL,
    fullname VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

-- Tabella dei punteggi
CREATE TABLE points
(
    id                  BIGINT PRIMARY KEY,
    terraforming_rating INT NOT NULL DEFAULT 0,
    greenery            INT NOT NULL DEFAULT 0,
    city                INT NOT NULL DEFAULT 0,
    milestones          INT NOT NULL DEFAULT 0,
    awards              INT NOT NULL DEFAULT 0,
    cards               INT NOT NULL DEFAULT 0,
    other_categories    VARCHAR(255) default '{}',
    corporation         VARCHAR(255),
    first_prelude       VARCHAR(255),
    second_prelude      VARCHAR(255),
    game                BIGINT,
    player_id           BIGINT,
    CONSTRAINT fk_points_game FOREIGN KEY (game) REFERENCES game (id),
    CONSTRAINT fk_points_player FOREIGN KEY (player_id) REFERENCES player (id)
);

-- Tabella dei drafting
CREATE TABLE drafting
(
    id              BIGINT PRIMARY KEY,
    draftings       TEXT,
    player_id       BIGINT,
    championship    BIGINT,
    CONSTRAINT fk_drafting_championship FOREIGN KEY (championship) REFERENCES championship (id),
    CONSTRAINT fk_drafting_player FOREIGN KEY (player_id) REFERENCES player (id)
);
