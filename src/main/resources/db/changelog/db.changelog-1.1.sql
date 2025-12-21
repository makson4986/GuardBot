--liquibase formatted sql

--changeset makson:1
ALTER TABLE guardsmen
ADD COLUMN description TEXT;

--rollback ALTER TABLE guardsmen DROP COLUMN description;