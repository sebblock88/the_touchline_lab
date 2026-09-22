CREATE TABLE players (
                         id BIGSERIAL PRIMARY KEY,
                         fpl_id BIGINT UNIQUE,
                         first_name VARCHAR(100),
                         second_name VARCHAR(100),
                         web_name VARCHAR(100),
                         team VARCHAR(100),
                         position_id VARCHAR(50),
                         now_cost INT,
                         total_points INT,
                         bonus INT,
                         goals_scored INT,
                         assists INT,
                         clean_sheets INT,
                         saves INT,
                         starts INT,
                         minutes INT,
                         yellow_cards INT,
                         red_cards INT,
                         defensive_contribution_per_90 DOUBLE PRECISION,
                         expected_goal_involvements_per_90 DOUBLE PRECISION,
                         expected_assists_per_90 DOUBLE PRECISION,
                         season VARCHAR(10) DEFAULT '2026-27',
                         is_current BOOLEAN DEFAULT TRUE,
                         games_hit_def_con INT,
                         games_with_bonus INT
);

-- Index for fast lookups when querying live vs historical seasons
CREATE INDEX idx_players_fpl_id ON players(fpl_id);
CREATE INDEX idx_players_season ON players(season);