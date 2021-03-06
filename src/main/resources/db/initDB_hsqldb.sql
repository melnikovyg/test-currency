DROP TABLE IF EXISTS date_time CASCADE;
DROP TABLE IF EXISTS currency CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;

DROP SEQUENCE IF EXISTS global_seq CASCADE;

CREATE SEQUENCE global_seq
  AS INTEGER
  START WITH 1000;

CREATE TABLE users (
  id        INTEGER GENERATED BY DEFAULT AS SEQUENCE global_seq PRIMARY KEY,
  full_name VARCHAR(255),
  login     VARCHAR(255)          NOT NULL,
  password  VARCHAR(255)          NOT NULL,
  enabled   BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE TABLE roles (
  user_id INTEGER NOT NULL,
  role    VARCHAR(255),
  CONSTRAINT user_role_idx UNIQUE (user_id, role),
  FOREIGN KEY (user_id) REFERENCES users (id)
    ON DELETE CASCADE
);

CREATE TABLE date_time (
  id        INTEGER GENERATED BY DEFAULT AS SEQUENCE global_seq PRIMARY KEY,
  date_time DATE NOT NULL
);

CREATE TABLE currency (
  id        INTEGER GENERATED BY DEFAULT AS SEQUENCE global_seq PRIMARY KEY,
  vcode     VARCHAR(255) NOT NULL,
  vname     VARCHAR(255),
  vnom      VARCHAR(255),
  vchcode   VARCHAR(255),
  date_time INTEGER      NOT NULL,
  vcurs     NUMERIC DEFAULT 0,
  CONSTRAINT code_date_const UNIQUE (vcode, date_time),
  FOREIGN KEY (date_time) REFERENCES date_time (id)
    ON DELETE CASCADE
);
