-- ============================================
-- DDL - TiketFilm Base de Datos
-- Fecha: 04/06/2026
-- ============================================

CREATE TABLE USUARIO (
    id NUMERIC(6) PRIMARY KEY,
    dni VARCHAR(9) NOT NULL UNIQUE,
    nombre VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    contraseña VARCHAR(30) NOT NULL,
    telefono NUMERIC(9),
    fecha_registro DATE NOT NULL,
    rol VARCHAR(10) CHECK (rol IN ('Admin', 'Comun')),
    registrado BOOLEAN DEFAULT TRUE
);

CREATE TABLE ADMIN (
    id_admin NUMERIC(6) PRIMARY KEY,
    FOREIGN KEY (id_admin) REFERENCES USUARIO(id) ON DELETE CASCADE
);

CREATE TABLE USUARIO_COMUN (
    id_usuario_comun NUMERIC(6) PRIMARY KEY,
    FOREIGN KEY (id_usuario_comun) REFERENCES USUARIO(id) ON DELETE CASCADE
);

CREATE TABLE CINE (
    id NUMERIC(6) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    ubicacion VARCHAR(100) NOT NULL,
    is_open BOOLEAN DEFAULT TRUE,
    aforo NUMERIC(5) DEFAULT 200
);

CREATE TABLE CATEGORIA (
    id NUMERIC(6) PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL,
    descripcion VARCHAR(200),
    id_admin NUMERIC(6),
    FOREIGN KEY (id_admin) REFERENCES ADMIN(id_admin) ON DELETE SET NULL
);

CREATE TABLE PELICULA (
    id NUMERIC(6) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500),
    precio NUMERIC(6,2) NOT NULL,
    fecha_creacion DATE NOT NULL,
    estado VARCHAR(15) DEFAULT 'Activo',
    url_imagen VARCHAR(500),
    id_cine NUMERIC(6) NOT NULL,
    id_categoria NUMERIC(6),
    FOREIGN KEY (id_cine) REFERENCES CINE(id) ON DELETE CASCADE,
    FOREIGN KEY (id_categoria) REFERENCES CATEGORIA(id) ON DELETE SET NULL
);

CREATE TABLE PELICULA_CATEGORIA (
    id_pelicula NUMERIC(6),
    id_categoria NUMERIC(6),
    PRIMARY KEY (id_pelicula, id_categoria),
    FOREIGN KEY (id_pelicula) REFERENCES PELICULA(id) ON DELETE CASCADE,
    FOREIGN KEY (id_categoria) REFERENCES CATEGORIA(id) ON DELETE CASCADE
);

CREATE TABLE ANUNCIO (
    id NUMERIC(6) PRIMARY KEY,
    fecha_publicacion DATE NOT NULL,
    fecha_expiracion DATE NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    interacciones NUMERIC(10) DEFAULT 0,
    id_pelicula NUMERIC(6) NOT NULL,
    id_cine NUMERIC(6) NOT NULL,
    FOREIGN KEY (id_pelicula) REFERENCES PELICULA(id) ON DELETE CASCADE,
    FOREIGN KEY (id_cine) REFERENCES CINE(id) ON DELETE CASCADE,
    CONSTRAINT chk_fechas CHECK (fecha_expiracion > fecha_publicacion)
);

CREATE TABLE RESERVA (
    id NUMERIC(6) PRIMARY KEY,
    fecha_reserva DATE NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    estado BOOLEAN DEFAULT TRUE,
    id_usuario NUMERIC(6) NOT NULL,
    id_pelicula NUMERIC(6) NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES USUARIO(id) ON DELETE CASCADE,
    FOREIGN KEY (id_pelicula) REFERENCES PELICULA(id) ON DELETE CASCADE,
    CONSTRAINT chk_reserva_fechas CHECK (fecha_fin > fecha_inicio)
);

CREATE TABLE RESEÑA (
    id NUMERIC(6) PRIMARY KEY,
    puntuacion NUMERIC(2) CHECK (puntuacion BETWEEN 1 AND 10),
    comentario VARCHAR(500),
    fecha DATE NOT NULL,
    id_usuario NUMERIC(6) NOT NULL,
    id_pelicula NUMERIC(6) NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES USUARIO(id) ON DELETE CASCADE,
    FOREIGN KEY (id_pelicula) REFERENCES PELICULA(id) ON DELETE CASCADE
);

