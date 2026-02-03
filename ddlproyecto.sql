CREATE TABLE USUARIO(
id NUMERIC(6) PRIMARY KEY,
dni VARCHAR(9),
nombre VARCHAR(20),
email VARCHAR(30),
contraseña VARCHAR(20),
telefono NUMERIC(9),
fecha_registro DATE,
rol VARCHAR(10),
estado VARCHAR(10)
);

CREATE TABLE ADMIN (
    id_admin NUMERIC(6) PRIMARY KEY,
    permisos_admin BOOLEAN,
    FOREIGN KEY (id_admin) REFERENCES USUARIO(id)
);

CREATE TABLE USUARIO_COMUN (
    id_usuario_comun NUMERIC(6) PRIMARY KEY,
    permisos_admin BOOLEAN,
    FOREIGN KEY (id_usuario_comun) REFERENCES USUARIO(id)
);

CREATE TABLE PELICULA(
id NUMERIC(6) PRIMARY KEY,
nombre VARCHAR(20),
descripcion VARCHAR(200),
ubicacion VARCHAR(100),
precio NUMERIC(100),
fecha_creacion DATE,
estado VARCHAR(10)
);

CREATE TABLE CATEGORIA(
id NUMERIC(6) PRIMARY KEY,
nombre VARCHAR(20),
descripcion VARCHAR(200),
id_admin NUMERIC(6)
);

CREATE TABLE RESEÑA (
id NUMERIC(6) PRIMARY KEY,
puntuacion NUMERIC(2),
comentario VARCHAR(200),
fecha DATE
);

CREATE TABLE ANUNCIO(
id NUMERIC(6) PRIMARY KEY,
fecha_expiracion DATE,
fecha_publicacion DATE,
activo BOOLEAN,
visitas NUMERIC(1000)
);

CREATE TABLE PROMOCION(
id NUMERIC(6) PRIMARY KEY,
fecha_incio DATE,
fecha_fin DATE,
importe NUMERIC(10),
metodo_pago VARCHAR(20)
);


CREATE TABLE RESERVA(
id numeric(6) PRIMARY KEY,
fecha_reserva DATE,
fecha_inicio DATE,
fecha_fin DATE,
estado BOOLEAN
);
