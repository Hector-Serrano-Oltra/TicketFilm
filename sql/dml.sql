-- ============================================
-- DML - Datos semilla TiketFilm
-- Fecha actual: 04/06/2026
-- ============================================

-- USUARIOS
INSERT INTO USUARIO VALUES (1, '12345678A', 'Raul',   'raul@email.com',  '1234', 600123456, '2026-01-10', 'Admin', TRUE);
INSERT INTO USUARIO VALUES (2, '12345678B', 'Hector', 'hector@email.com','1234', 600987654, '2026-01-15', 'Comun', TRUE);


INSERT INTO ADMIN VALUES (1);
INSERT INTO USUARIO_COMUN VALUES (2);


-- CINES
INSERT INTO CINE VALUES (1, 'Cinesa Valencia', 'Valencia', TRUE, 250);
INSERT INTO CINE VALUES (2, 'Yelmo Madrid',    'Madrid',   TRUE, 300);

-- CATEGORIAS
INSERT INTO CATEGORIA VALUES (1, 'Vacaciones', 'Alojamientos vacacionales', 1);
INSERT INTO CATEGORIA VALUES (2, 'Estrenos',   'Peliculas de estreno',      1);

-- PELICULAS (url_imagen: URL directa, se carga sin descargar localmente)
INSERT INTO PELICULA VALUES (1, 'Apartamento Playa', 'Apartamento en Valencia frente al mar espectacular',          80, '2026-03-01', 'Activo', 'https://picsum.photos/seed/playa/400/500',        1, 1);
INSERT INTO PELICULA VALUES (2, 'Avengers',          'Los Vengadores se reunen de nuevo para salvar el universo',   12, '2026-03-15', 'Activo', 'https://picsum.photos/seed/avengers/400/500',     2, 2);
INSERT INTO PELICULA VALUES (3, 'Casa Rural',        'Casa rural en la sierra de Madrid con vistas increibles',     60, '2026-03-20', 'Activo', 'https://picsum.photos/seed/rural/400/500',        2, 1);
INSERT INTO PELICULA VALUES (4, 'Interestelar',      'Un viaje a traves de un agujero de gusano para salvar la humanidad', 10, '2026-04-10', 'Activo', 'https://picsum.photos/seed/interestelar/400/500', 1, 2);
INSERT INTO PELICULA VALUES (5, 'El Padrino',        'La historia de la familia Corleone en la mafia italoamericana',      8,  '2026-04-15', 'Activo', 'https://picsum.photos/seed/padrino/400/500',      2, 2);
INSERT INTO PELICULA VALUES (6, 'Jurassic Park',     'Un parque tematico con dinosaurios cobra vida de forma aterradora', 14, '2026-05-01', 'Activo', 'https://picsum.photos/seed/jurassic/400/500',     1, 2);
INSERT INTO PELICULA VALUES (7, 'Titanic',           'Una historia de amor imposible a bordo del transatlantico mas famoso', 9, '2026-05-10', 'Activo', 'https://picsum.photos/seed/titanic/400/500',      2, 1);
INSERT INTO PELICULA VALUES (8, 'Matrix',            'Un hacker descubre que la realidad es una simulacion creada por maquinas', 11, '2026-05-20', 'Activo', 'https://picsum.photos/seed/matrix/400/500',       1, 2);

-- PELICULA_CATEGORIA
INSERT INTO PELICULA_CATEGORIA VALUES (1, 1);
INSERT INTO PELICULA_CATEGORIA VALUES (2, 2);
INSERT INTO PELICULA_CATEGORIA VALUES (3, 1);
INSERT INTO PELICULA_CATEGORIA VALUES (4, 2);
INSERT INTO PELICULA_CATEGORIA VALUES (5, 2);
INSERT INTO PELICULA_CATEGORIA VALUES (6, 2);
INSERT INTO PELICULA_CATEGORIA VALUES (7, 1);
INSERT INTO PELICULA_CATEGORIA VALUES (8, 2);

-- ANUNCIOS (fecha_publicacion <= hoy, fecha_expiracion > hoy)
INSERT INTO ANUNCIO VALUES (1, '2026-05-25', '2026-08-15', TRUE, 0, 1, 1);
INSERT INTO ANUNCIO VALUES (2, '2026-05-28', '2026-07-30', TRUE, 0, 2, 2);
INSERT INTO ANUNCIO VALUES (3, '2026-06-01', '2026-09-15', TRUE, 0, 4, 1);
INSERT INTO ANUNCIO VALUES (4, '2026-06-02', '2026-08-30', TRUE, 0, 7, 2);
INSERT INTO ANUNCIO VALUES (5, '2026-06-03', '2026-12-31', TRUE, 0, 8, 1);

-- RESERVAS
INSERT INTO RESERVA VALUES (1, '2026-05-30', '2026-06-10', '2026-06-15', TRUE, 2, 1);

-- RESEÑAS
INSERT INTO RESEÑA VALUES (1, 8, 'Muy buen apartamento, vistas espectaculares', '2026-06-01', 2, 1);
