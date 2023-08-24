DROP TABLE IF EXISTS STATS CASCADE;

CREATE TABLE IF NOT EXISTS stats (
    id        BIGINT        NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    app       VARCHAR(255)  NOT NULL,
    uri       VARCHAR(2048) NOT NULL,
    ip        VARCHAR(45)   NOT NULL,
    timestamp TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    CONSTRAINT pk_stat PRIMARY KEY (id)
);
