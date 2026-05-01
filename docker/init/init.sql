CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE voters (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                        first_name VARCHAR(100) NOT NULL,
                        last_name VARCHAR(100) NOT NULL,
                        searchable VARCHAR(201) GENERATED ALWAYS AS (lower(first_name || ' ' || last_name)) STORED,

                        pesel VARCHAR(11) NOT NULL,
                        birth_date DATE NOT NULL,

                        blocked BOOLEAN NOT NULL DEFAULT FALSE,

                        created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                        updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),

                        CONSTRAINT voters_pesel_format_chk
                            CHECK (pesel ~ '^[0-9]{11}$'),

                        CONSTRAINT uk_voters_pesel UNIQUE (pesel),

    CONSTRAINT voters_first_name_length_chk
        CHECK (length(trim(first_name)) >= 2),

    CONSTRAINT voters_last_name_length_chk
        CHECK (length(trim(last_name)) >= 2)
);

CREATE INDEX idx_voters_last_name
    ON voters (last_name);

CREATE INDEX idx_voters_birth_date
    ON voters (birth_date);

CREATE INDEX idx_voters_blocked
    ON voters (blocked);

CREATE INDEX idx_voters_first_name_last_name
    ON voters (first_name, last_name);

CREATE TABLE elections (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                           name VARCHAR(200) NOT NULL,
                           description VARCHAR(1000),
                           searchable VARCHAR(200) GENERATED ALWAYS AS (lower(name)) STORED,


                           start_date TIMESTAMPTZ NOT NULL,
                           end_date TIMESTAMPTZ NOT NULL,

                            options_size INTEGER NOT NULL DEFAULT 0,

                           created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                           updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),

                           CONSTRAINT elections_name_length_chk
                               CHECK (length(trim(name)) >= 3),

                           CONSTRAINT elections_dates_chk
                               CHECK (end_date > start_date)
);

CREATE INDEX idx_elections_name
    ON elections (name);

CREATE TABLE election_options (
                                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                                  election_id UUID NOT NULL,

                                  name VARCHAR(200) NOT NULL,
                                  description VARCHAR(1000),

                                  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                                  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),

                                  CONSTRAINT fk_election_options_election
                                      FOREIGN KEY (election_id)
                                          REFERENCES elections (id)
                                          ON DELETE RESTRICT,

                                  CONSTRAINT election_options_name_length_chk
                                      CHECK (length(trim(name)) >= 2),

                                  CONSTRAINT election_options_unique_election_id_id
                                      UNIQUE (election_id, id)
);

CREATE INDEX idx_election_options_election_id
    ON election_options (election_id);

CREATE TABLE votes (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                       election_id UUID NOT NULL,
                       voter_id UUID NOT NULL,
                       option_id UUID NOT NULL,

                       voted_at TIMESTAMPTZ NOT NULL DEFAULT now(),

                       CONSTRAINT fk_votes_election
                           FOREIGN KEY (election_id)
                               REFERENCES elections (id)
                               ON DELETE RESTRICT,

                       CONSTRAINT fk_votes_voter
                           FOREIGN KEY (voter_id)
                               REFERENCES voters (id)
                               ON DELETE RESTRICT,

                       CONSTRAINT fk_votes_option
                           FOREIGN KEY (option_id)
                               REFERENCES election_options (id)
                               ON DELETE RESTRICT,

                       CONSTRAINT fk_votes_election_option
                           FOREIGN KEY (election_id, option_id)
                               REFERENCES election_options (election_id, id)
                               ON DELETE RESTRICT,

                       CONSTRAINT votes_unique_voter_per_election
                           UNIQUE (election_id, voter_id)
);

CREATE INDEX idx_votes_election_id
    ON votes (election_id);

CREATE INDEX idx_votes_election_option
    ON votes (election_id, option_id);
