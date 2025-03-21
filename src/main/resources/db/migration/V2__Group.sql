CREATE TABLE IF NOT EXISTS GROUPS (
           id uuid NOT NULL primary key,
           name varchar(30) not null,
           description varchar(256) not null,
           created_at  TIMESTAMP DEFAULT NOW(),
           updated_at TIMESTAMP DEFAULT NOW()
);