CREATE TABLE album (
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    artist_id INTEGER REFERENCES artist (id) NOT NULL,
    name TEXT NOT NULL
);

CREATE TRIGGER album_updated_at
BEFORE UPDATE ON album
FOR EACH ROW
EXECUTE PROCEDURE moddatetime (updated_at);

INSERT INTO album (artist_id, name)
VALUES (1, 'Chaleur humaine'),
       (1, 'Chris'),
       (2, 'Homework'),
       (2, 'Discovery'),
       (2, 'Human After All'),
       (2, 'Random Access Memories'),
       (3, 'Horn of Plenty'),
       (3, 'Yellow House'),
       (3, 'Veckatimest'),
       (3, 'Shields'),
       (3, 'Painted Ruins'),
       (4, 'iMegaphone'),
       (4, 'Speak for Yourself'),
       (4, 'Ellipse'),
       (4, 'Sparks');
