DROP TABLE IF EXISTS users;

CREATE TABLE users (
        id bigserial PRIMARY KEY,
        email varchar(50) NOT NULL UNIQUE,
        enabled boolean NOT NULL,
        first_name varchar(50),
        last_name varchar(50),
        fpl_team_id INTEGER UNIQUE
);