CREATE EXTENSION IF NOT EXISTS moddatetime;

CREATE TABLE artist (
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    name TEXT NOT NULL
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
