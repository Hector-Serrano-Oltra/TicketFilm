TIKETFILM - Proyecto Intermodular
==================================

ESTRUCTURA
----------
TiketFilm_Presentacion/
├── src/Clases/         → Modelo + Controlador + BD (Java)
├── src/Ventanas/       → Vista (Java)
├── WEB/                → Web (HTML + CSS + JS)
├── lib/                → Driver PostgreSQL
├── sql/                → DDL y DML
├── .classpath          → Config Eclipse
└── .project            → Config Eclipse


PASOS PARA EL COMPAÑERO (Windows)
----------------------------------

1) ABRIR EN ECLIPSE (paso a paso)

   a) Abre Eclipse
   b) File → Import...
   c) General → Existing Projects into Workspace → Next
   d) En "Select root directory" pulsa Browse...
   e) Selecciona la carpeta TiketFilm_Presentacion
   f) Asegúrate de que "TiketFilm" aparece marcado en la lista
   g) Pulsa Finish

   Si el proyecto sale con errores:
   - Click derecho en el proyecto → Build Path → Configure Build Path
   - Pestaña Libraries → Add JARs → selecciona lib/postgresql-42.7.5.jar → OK
   - Project → Clean → limpia TiketFilm

2) CONFIGURAR CONEXIÓN A POSTGRESQL
   Editar src/Clases/ConexionBD.java línea 11:
   - HOST: cambiar "localhost" por la IP donde corre PostgreSQL (ej: "192.168.1.10")
   - PASS: cambiar "Hecso02" por la contraseña real de PostgreSQL

3) BASE DE DATOS
   El compañero que tiene PostgreSQL debe ejecutar sql/ddl.sql y sql/dml.sql

4) EJECUTAR LA APP JAVA (AdminPanel)
   Click derecho en src/Ventanas/CuentaScreen.java → Run As → Java Application
   Login: DNI = 12345678A, contraseña = 1234

5) EJECUTAR EL SERVIDOR WEB
   Click derecho en src/Clases/ServidorAPI.java → Run As → Java Application
   Abrir navegador en: http://localhost:8080

6) PARA LA PRESENTACIÓN EN RED
   - PostgreSQL en máquina del compañero (IP x.x.x.x)
   - Java app y servidor web en la máquina de la presentación
   - El servidor web escucha en 0.0.0.0:8080 (accesible desde otras máquinas)
   - En la VM o navegador: abrir http://<IP-DE-LA-MÁQUINA>:8080


NOTAS
-----
- Las contraseñas en ConexionBD.java son las del compañero que tenga PostgreSQL
- Si PostgreSQL no está en localhost, cambiar HOST en ConexionBD.java
- El servidor web usa el mismo ConexionBD.java → un solo cambio para todo
- La app de escritorio (AdminPanel) y la web comparten la misma BD
