--liquibase formatted sql

--changeset makson:1
CREATE TABLE IF NOT EXISTS ranks
(
    id                  SERIAL PRIMARY KEY,
    name                VARCHAR(32) UNIQUE NOT NULL,
    max_points          INT                NOT NULL,
    max_special_reports INT                NOT NULL,
    position            INT                NOT NULL
);
--rollback DROP TABLE ranks;

--changeset makson:2
CREATE TABLE IF NOT EXISTS guardsmen
(
    id             SERIAL PRIMARY KEY,
    name           VARCHAR(64) UNIQUE NOT NULL,
    rank_id        INT DEFAULT 1,
    points         INT DEFAULT 0,
    special_report INT DEFAULT 0,
    last_report    DATE,
    description TEXT,
    FOREIGN KEY (rank_id) REFERENCES ranks (id)
);
--rollback DROP TABLE guardsman;

--changeset makson:3
CREATE TABLE IF NOT EXISTS prisoners
(
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(64) NOT NULL,
    conclusion_date DATE DEFAULT CURRENT_TIMESTAMP,
    release_date    DATE        NOT NULL,
    prison_cell     int         NOT NULL,
    reason          TEXT        NOT NULL
);
--rollback DROP TABLE prison;

--changeset makson:4
CREATE TABLE IF NOT EXISTS departments
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL
);
--rollback DROP TABLE departments;

--changeset makson:5
CREATE TABLE IF NOT EXISTS department_members
(
    id            SERIAL PRIMARY KEY,
    guardsman_id  INT,
    department_id INT,
    role          VARCHAR(32),
    FOREIGN KEY (guardsman_id) REFERENCES guardsmen (id) ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES departments (id),
    CONSTRAINT uq_department_members UNIQUE (guardsman_id, department_id, role)
);
--rollback DROP TABLE department_members;



