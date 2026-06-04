package Clases;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class DatosApp {

	private static ArrayList<Usuario> usuarios = new ArrayList<>();
	private static ArrayList<Pelicula> peliculas = new ArrayList<>();
	private static ArrayList<Cine> cines = new ArrayList<>();
	private static ArrayList<Categoria> categorias = new ArrayList<>();
	private static ArrayList<Anuncio> anuncios = new ArrayList<>();
	private static Usuario usuarioLogueado = null;

	// ============ GETTERS ============

	public static ArrayList<Usuario> getUsuarios() { return usuarios; }
	public static ArrayList<Pelicula> getPeliculas() { return peliculas; }
	public static ArrayList<Cine> getCines() { return cines; }
	public static ArrayList<Categoria> getCategorias() { return categorias; }
	public static ArrayList<Anuncio> getAnuncios() { return anuncios; }
	public static Usuario getUsuarioLogueado() { return usuarioLogueado; }
	public static void setUsuarioLogueado(Usuario u) { usuarioLogueado = u; }

	// ============ BÚSQUEDA ============

	public static Cine buscarCinePorPk(Integer pk) { for (Cine c : cines) if (c.getPk().equals(pk)) return c; return null; }
	public static Categoria buscarCategoriaPorNombre(String n) { for (Categoria c : categorias) if (c.getNombre().equals(n)) return c; return null; }
	public static Pelicula buscarPeliculaPorPk(Integer pk) { for (Pelicula p : peliculas) if (p.getPk().equals(pk)) return p; return null; }
	public static Anuncio buscarAnuncioPorPk(Integer pk) { for (Anuncio a : anuncios) if (a.getPk().equals(pk)) return a; return null; }

	// ============ UTILIDADES FECHA ============

	private static int leerFecha(ResultSet rs, String col) throws Exception {
		String s = rs.getString(col);
		return s == null ? 0 : Integer.parseInt(s.replace("-", ""));
	}
	private static String escribirFecha(int f) {
		String s = String.valueOf(f);
		return s.substring(0, 4) + "-" + s.substring(4, 6) + "-" + s.substring(6);
	}

	// ============ LOGIN ============

	public static Usuario login(String dni, String pass) {
		Connection c = ConexionBD.getConexion();
		if (c == null) { JOptionPane.showMessageDialog(null, "No hay conexion a la BD.", "Error", JOptionPane.ERROR_MESSAGE); return null; }
		try {
			PreparedStatement ps = c.prepareStatement(
				"SELECT * FROM USUARIO WHERE dni = ? AND contraseña = ?");
			ps.setString(1, dni); ps.setString(2, pass);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int fr = leerFecha(rs, "fecha_registro");
				Usuario u = rs.getString("rol").equals("Admin")
					? new Admin(rs.getString("dni"), rs.getString("nombre"), rs.getString("email"),
							rs.getString("contraseña"), rs.getInt("telefono"), fr)
					: new Usuario_Comun(rs.getString("dni"), rs.getString("nombre"), rs.getString("email"),
							rs.getString("contraseña"), rs.getInt("telefono"), fr);
				u.setPk(rs.getInt("id")); rs.close(); ps.close(); return u;
			}
			rs.close(); ps.close();
		} catch (Exception e) { JOptionPane.showMessageDialog(null, "Error BD:\n" + e.getClass().getSimpleName() + ": " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
		return null;
	}

	// ============ CARGA DESDE BD ============

	public static void cargarDatos() {
		Connection c = ConexionBD.getConexion();
		if (c == null) { inicializarDatos(); return; }
		try {
			cargarUsuarios(c); cargarCines(c); cargarCategorias(c);
			cargarPeliculas(c); cargarAnuncios(c);
			if (usuarios.isEmpty()) { inicializarDatos(); guardarTodo(c); }
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al cargar. Se usan datos iniciales.\n" + e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
			inicializarDatos(); guardarTodo(c);
		}
	}

	private static void cargarUsuarios(Connection c) throws Exception {
		Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT * FROM USUARIO ORDER BY id");
		while (rs.next()) {
			int fr = leerFecha(rs, "fecha_registro");
			Usuario u = rs.getString("rol").equals("Admin")
				? new Admin(rs.getString("dni"), rs.getString("nombre"), rs.getString("email"),
						rs.getString("contraseña"), rs.getInt("telefono"), fr)
				: new Usuario_Comun(rs.getString("dni"), rs.getString("nombre"), rs.getString("email"),
						rs.getString("contraseña"), rs.getInt("telefono"), fr);
			u.setPk(rs.getInt("id")); u.setRol(rs.getString("rol")); usuarios.add(u);
		}
		rs.close(); s.close();
		int m = 0; for (Usuario u : usuarios) if (u.getPk() > m) m = u.getPk(); Usuario.setContador(m);
	}

	private static void cargarCines(Connection c) throws Exception {
		Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT * FROM CINE ORDER BY id");
		while (rs.next()) {
			Cine ci = new Cine(rs.getString("nombre"), rs.getString("ubicacion"),
				rs.getBoolean("is_open"), rs.getInt("aforo"));
			ci.setPk(rs.getInt("id")); cines.add(ci);
		}
		rs.close(); s.close();
		int m = 0; for (Cine ci : cines) if (ci.getPk() > m) m = ci.getPk(); Cine.setContador(m);
	}

	private static void cargarCategorias(Connection c) throws Exception {
		Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT * FROM CATEGORIA ORDER BY id");
		while (rs.next()) {
			Categoria ca = new Categoria(rs.getString("nombre"), rs.getString("descripcion"),
				(Admin) buscarUsPorPk(rs.getInt("id_admin")));
			ca.setPk(rs.getInt("id")); categorias.add(ca);
		}
		rs.close(); s.close();
		int m = 0; for (Categoria ca : categorias) if (ca.getPk() > m) m = ca.getPk(); Categoria.setContador(m);
	}

	private static void cargarPeliculas(Connection c) throws Exception {
		Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT * FROM PELICULA ORDER BY id");
		while (rs.next()) {
			Cine ci = buscarCinePorPkLocal(rs.getInt("id_cine"));
			Categoria ca = buscarCatPorPkLocal(rs.getInt("id_categoria"));
			Pelicula p = new Pelicula(rs.getString("nombre"), rs.getString("descripcion"),
				rs.getInt("precio"), rs.getString("fecha_creacion"), rs.getString("estado"),
				ci != null ? ci : cines.get(0), ca != null ? ca : categorias.get(0),
				rs.getString("url_imagen"));
			p.setPk(rs.getInt("id")); peliculas.add(p);
		}
		rs.close(); s.close();
		int m = 0; for (Pelicula p : peliculas) if (p.getPk() > m) m = p.getPk(); Pelicula.setContador(m);
	}

	private static void cargarAnuncios(Connection c) throws Exception {
		Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT * FROM ANUNCIO ORDER BY id");
		while (rs.next()) {
			Pelicula p = buscarPeliPorPkLocal(rs.getInt("id_pelicula"));
			Cine ci = buscarCinePorPkLocal(rs.getInt("id_cine"));
			Anuncio a = new Anuncio(rs.getString("fecha_expiracion"), rs.getString("fecha_publicacion"),
				rs.getBoolean("activo"), p != null ? p : peliculas.get(0), ci != null ? ci : cines.get(0));
			a.setPk(rs.getInt("id")); a.setInteracciones(rs.getInt("interacciones")); anuncios.add(a);
		}
		rs.close(); s.close();
		int m = 0; for (Anuncio a : anuncios) if (a.getPk() > m) m = a.getPk(); Anuncio.setContador(m);
	}

	private static Cine buscarCinePorPkLocal(int pk) { for (Cine c : cines) if (c.getPk() == pk) return c; return null; }
	private static Pelicula buscarPeliPorPkLocal(int pk) { for (Pelicula p : peliculas) if (p.getPk() == pk) return p; return null; }
	private static Categoria buscarCatPorPkLocal(int pk) { for (Categoria c : categorias) if (c.getPk() == pk) return c; return null; }
	private static Usuario buscarUsPorPk(int pk) { for (Usuario u : usuarios) if (u.getPk() == pk) return u; return null; }

	// ============ GUARDADO EN BD ============

	public static void guardarDatos() {
		Connection c = ConexionBD.getConexion();
		if (c != null) guardarTodo(c);
		ConexionBD.cerrar();
	}

	private static void guardarTodo(Connection c) {
		try {
			for (Usuario u : usuarios) if (!existe(c, "USUARIO", u.getPk())) insertarUsuario(c, u);
			for (Cine ci : cines) if (!existe(c, "CINE", ci.getPk())) insertarCine(c, ci);
			for (Categoria ca : categorias) if (!existe(c, "CATEGORIA", ca.getPk())) insertarCategoria(c, ca);
			for (Pelicula p : peliculas) if (!existe(c, "PELICULA", p.getPk())) insertarPelicula(c, p);
			for (Anuncio a : anuncios) if (!existe(c, "ANUNCIO", a.getPk())) insertarAnuncio(c, a);
		} catch (Exception e) { JOptionPane.showMessageDialog(null, "Error guardando BD: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
	}

	private static boolean existe(Connection c, String tabla, int pk) throws Exception {
		PreparedStatement ps = c.prepareStatement("SELECT 1 FROM " + tabla + " WHERE id = ?");
		ps.setInt(1, pk); ResultSet rs = ps.executeQuery(); boolean r = rs.next(); rs.close(); ps.close(); return r;
	}

	// ============ INSERTS PRIVADOS ============

	private static void insertarUsuario(Connection c, Usuario u) throws Exception {
		PreparedStatement ps = c.prepareStatement(
			"INSERT INTO USUARIO (id, dni, nombre, email, contraseña, telefono, fecha_registro, rol, registrado) VALUES (?,?,?,?,?,?,?,?,?)");
		ps.setInt(1, u.getPk()); ps.setString(2, u.getDni()); ps.setString(3, u.getNombre());
		ps.setString(4, u.getEmail()); ps.setString(5, u.getContraseña()); ps.setInt(6, u.getTelefono());
		ps.setDate(7, Date.valueOf(escribirFecha(u.getFecha_registro()))); ps.setString(8, u.getRol());
		ps.setBoolean(9, u.isRegistrado()); ps.executeUpdate(); ps.close();
		if (u instanceof Admin) {
			ps = c.prepareStatement("INSERT INTO ADMIN (id_admin) VALUES (?)");
			ps.setInt(1, u.getPk()); ps.executeUpdate(); ps.close();
		} else {
			ps = c.prepareStatement("INSERT INTO USUARIO_COMUN (id_usuario_comun) VALUES (?)");
			ps.setInt(1, u.getPk()); ps.executeUpdate(); ps.close();
		}
	}
	private static void insertarCine(Connection c, Cine ci) throws Exception {
		PreparedStatement ps = c.prepareStatement("INSERT INTO CINE (id, nombre, ubicacion, is_open, aforo) VALUES (?,?,?,?,?)");
		ps.setInt(1, ci.getPk()); ps.setString(2, ci.getNombre()); ps.setString(3, ci.getUbicacion());
		ps.setBoolean(4, ci.isIs_open()); ps.setInt(5, ci.getAforo()); ps.executeUpdate(); ps.close();
	}
	private static void insertarCategoria(Connection c, Categoria ca) throws Exception {
		PreparedStatement ps = c.prepareStatement("INSERT INTO CATEGORIA (id, nombre, descripcion, id_admin) VALUES (?,?,?,?)");
		ps.setInt(1, ca.getPk()); ps.setString(2, ca.getNombre()); ps.setString(3, ca.getDescripcion());
		ps.setInt(4, ca.getPk_admin()); ps.executeUpdate(); ps.close();
	}
	private static void insertarPelicula(Connection c, Pelicula p) throws Exception {
		PreparedStatement ps = c.prepareStatement(
			"INSERT INTO PELICULA (id, nombre, descripcion, precio, fecha_creacion, estado, url_imagen, id_cine, id_categoria) VALUES (?,?,?,?,?,?,?,?,?)");
		ps.setInt(1, p.getPk()); ps.setString(2, p.getNombre()); ps.setString(3, p.getDescripcion());
		ps.setInt(4, p.getPrecio()); ps.setDate(5, Date.valueOf(p.getFecha_creacion())); ps.setString(6, p.getEstado());
		ps.setString(7, p.getUrlImagen()); ps.setInt(8, p.getPk_cine());
		Categoria cat = buscarCategoriaPorNombre(p.getNombre_categoria());
		if (cat != null) ps.setInt(9, cat.getPk()); else ps.setNull(9, java.sql.Types.INTEGER);
		ps.executeUpdate(); ps.close();
	}
	private static void insertarAnuncio(Connection c, Anuncio a) throws Exception {
		PreparedStatement ps = c.prepareStatement(
			"INSERT INTO ANUNCIO (id, fecha_expiracion, fecha_publicacion, activo, interacciones, id_pelicula, id_cine) VALUES (?,?,?,?,?,?,?)");
		ps.setInt(1, a.getPk()); ps.setDate(2, Date.valueOf(a.getFecha_expiracion()));
		ps.setDate(3, Date.valueOf(a.getFecha_publicacion())); ps.setBoolean(4, a.isActivo());
		ps.setInt(5, a.getInteracciones()); ps.setInt(6, a.getPk_pelicula()); ps.setInt(7, a.getPk_cine());
		ps.executeUpdate(); ps.close();
	}

	// ============ CRUD PUBLICOS (devuelven boolean) ============

	public static boolean insertarPeliculaBD(Pelicula p) {
		peliculas.add(p);
		try { Connection c = ConexionBD.getConexion(); if (c != null) insertarPelicula(c, p); return true; }
		catch (Exception e) { peliculas.remove(p); JOptionPane.showMessageDialog(null, "Error BD:\n" + e.getClass().getSimpleName() + ": " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); return false; }
	}
	public static boolean actualizarPeliculaBD(Pelicula p) {
		try { Connection c = ConexionBD.getConexion(); if (c == null) return false;
			PreparedStatement ps = c.prepareStatement(
				"UPDATE PELICULA SET nombre=?,descripcion=?,precio=?,fecha_creacion=?,estado=?,url_imagen=?,id_cine=?,id_categoria=? WHERE id=?");
			ps.setString(1, p.getNombre()); ps.setString(2, p.getDescripcion()); ps.setInt(3, p.getPrecio());
			ps.setDate(4, Date.valueOf(p.getFecha_creacion())); ps.setString(5, p.getEstado());
			ps.setString(6, p.getUrlImagen()); ps.setInt(7, p.getPk_cine());
			Categoria cat = buscarCategoriaPorNombre(p.getNombre_categoria());
			if (cat != null) ps.setInt(8, cat.getPk()); else ps.setNull(8, java.sql.Types.INTEGER);
			ps.setInt(9, p.getPk()); ps.executeUpdate(); ps.close(); return true;
		} catch (Exception e) { JOptionPane.showMessageDialog(null, "Error BD:\n" + e.getClass().getSimpleName() + ": " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); return false; }
	}
	public static boolean eliminarPeliculaBD(Pelicula p) {
		peliculas.remove(p);
		try { Connection c = ConexionBD.getConexion(); if (c == null) return false;
			PreparedStatement ps = c.prepareStatement("DELETE FROM PELICULA WHERE id=?"); ps.setInt(1, p.getPk());
			ps.executeUpdate(); ps.close(); return true;
		} catch (Exception e) { peliculas.add(p); JOptionPane.showMessageDialog(null, "Error BD:\n" + e.getClass().getSimpleName() + ": " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); return false; }
	}

	public static boolean insertarCineBD(Cine ci) {
		cines.add(ci);
		try { Connection c = ConexionBD.getConexion(); if (c != null) insertarCine(c, ci); return true; }
		catch (Exception e) { cines.remove(ci); JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + ": " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE); return false; }
	}
	public static boolean toggleCineBD(Cine ci) {
		try { Connection c = ConexionBD.getConexion(); if (c == null) return false;
			PreparedStatement ps = c.prepareStatement("UPDATE CINE SET is_open=? WHERE id=?");
			ps.setBoolean(1, ci.isIs_open()); ps.setInt(2, ci.getPk()); ps.executeUpdate(); ps.close(); return true;
		} catch (Exception e) { JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + ": " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE); return false; }
	}
	public static boolean eliminarCineBD(Cine ci) {
		cines.remove(ci);
		try { Connection c = ConexionBD.getConexion(); if (c == null) return false;
			PreparedStatement ps = c.prepareStatement("DELETE FROM CINE WHERE id=?"); ps.setInt(1, ci.getPk());
			ps.executeUpdate(); ps.close(); return true;
		} catch (Exception e) { cines.add(ci); JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + ": " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE); return false; }
	}

	public static boolean insertarCategoriaBD(Categoria ca) {
		categorias.add(ca);
		try { Connection c = ConexionBD.getConexion(); if (c != null) insertarCategoria(c, ca); return true; }
		catch (Exception e) { categorias.remove(ca); JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + ": " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE); return false; }
	}
	public static boolean actualizarCategoriaBD(Categoria ca) {
		try { Connection c = ConexionBD.getConexion(); if (c == null) return false;
			PreparedStatement ps = c.prepareStatement("UPDATE CATEGORIA SET nombre=?,descripcion=? WHERE id=?");
			ps.setString(1, ca.getNombre()); ps.setString(2, ca.getDescripcion()); ps.setInt(3, ca.getPk());
			ps.executeUpdate(); ps.close(); return true;
		} catch (Exception e) { JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + ": " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE); return false; }
	}
	public static boolean eliminarCategoriaBD(Categoria ca) {
		categorias.remove(ca);
		try { Connection c = ConexionBD.getConexion(); if (c == null) return false;
			PreparedStatement ps = c.prepareStatement("DELETE FROM CATEGORIA WHERE id=?"); ps.setInt(1, ca.getPk());
			ps.executeUpdate(); ps.close(); return true;
		} catch (Exception e) { categorias.add(ca); JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + ": " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE); return false; }
	}

	public static boolean insertarAnuncioBD(Anuncio a) {
		anuncios.add(a);
		try { Connection c = ConexionBD.getConexion(); if (c != null) insertarAnuncio(c, a); return true; }
		catch (Exception e) { anuncios.remove(a); JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + ": " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE); return false; }
	}
	public static boolean toggleAnuncioBD(Anuncio a) {
		try { Connection c = ConexionBD.getConexion(); if (c == null) return false;
			PreparedStatement ps = c.prepareStatement("UPDATE ANUNCIO SET activo=? WHERE id=?");
			ps.setBoolean(1, a.isActivo()); ps.setInt(2, a.getPk()); ps.executeUpdate(); ps.close(); return true;
		} catch (Exception e) { JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + ": " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE); return false; }
	}
	public static boolean eliminarAnuncioBD(Anuncio a) {
		anuncios.remove(a);
		try { Connection c = ConexionBD.getConexion(); if (c == null) return false;
			PreparedStatement ps = c.prepareStatement("DELETE FROM ANUNCIO WHERE id=?"); ps.setInt(1, a.getPk());
			ps.executeUpdate(); ps.close(); return true;
		} catch (Exception e) { anuncios.add(a); JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + ": " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE); return false; }
	}

	// ============ DATOS INICIALES ============

	public static void inicializarDatos() {
		Admin admin = new Admin("12345678A", "Raul", "raul@email.com", "1234", 600123456, 20260110);
		usuarios.add(admin);
		cines.add(new Cine("Cinesa Valencia", "Valencia", true, 250));
		cines.add(new Cine("Yelmo Madrid", "Madrid", true, 300));
		categorias.add(new Categoria("Vacaciones", "Alojamientos vacacionales", admin));
		categorias.add(new Categoria("Estrenos", "Peliculas de estreno", admin));
	}

}
