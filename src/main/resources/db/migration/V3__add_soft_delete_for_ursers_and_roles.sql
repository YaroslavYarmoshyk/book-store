ALTER TABLE users ADD is_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE roles ADD is_deleted BOOLEAN NOT NULL DEFAULT FALSE;
