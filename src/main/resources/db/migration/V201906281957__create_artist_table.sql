CREATE EXTENSION IF NOT EXISTS moddatetime;

CREATE TABLE artist (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    name VARCHAR(500) NOT NULL
);

CREATE TRIGGER artist_updated_at
BEFORE UPDATE ON artist
FOR EACH ROW
EXECUTE PROCEDURE moddatetime (updated_at);

INSERT INTO artist (name)
VALUES ('Christine and the Queens'),
       ('Daft Punk'),
       ('Grizzly Bear'),
       ('Imogen Heap');
