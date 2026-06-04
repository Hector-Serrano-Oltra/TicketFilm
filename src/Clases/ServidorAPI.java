package Clases;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import com.sun.net.httpserver.*;

public class ServidorAPI {

	private static final String WEB_ROOT = "WEB";
	private static final int PUERTO = 8080;

	public static void main(String[] args) throws Exception {
		DatosApp.cargarDatos();
		HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", PUERTO), 0);

		server.createContext("/api/peliculas", new ApiPeliculas());
		server.createContext("/api/anuncios", new ApiAnuncios());
		server.createContext("/api/cines", new ApiCines());
		server.createContext("/api/categorias", new ApiCategorias());
		server.createContext("/api/login", new ApiLogin());
		server.createContext("/api/registro", new ApiRegistro());
		server.createContext("/api/comprar", new ApiComprar());
		server.createContext("/", new Estaticos());

		server.setExecutor(java.util.concurrent.Executors.newFixedThreadPool(4));
		server.start();
		System.out.println("Servidor web iniciado en http://localhost:" + PUERTO);
	}

	// ============ HELPERS ============

	private static void cors(HttpExchange t) {
		t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
		t.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
		t.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
	}

	private static void json(HttpExchange t, String body) throws IOException {
		cors(t);
		t.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
		byte[] b = body.getBytes("UTF-8");
		t.sendResponseHeaders(200, b.length);
		OutputStream os = t.getResponseBody();
		os.write(b); os.close();
	}

	private static String leerBody(HttpExchange t) throws IOException {
		return new String(t.getRequestBody().readAllBytes(), "UTF-8");
	}

	private static String[] splitJson(String body) {
		// Extrae valores entre comillas de un JSON simple: {"clave":"valor",...}
		body = body.replace("{", "").replace("}", "").replace("\"", "");
		String[] partes = body.split(",");
		String[] res = new String[partes.length * 2];
		int idx = 0;
		for (String p : partes) {
			String[] kv = p.split(":", 2);
			res[idx++] = kv.length > 0 ? kv[0].trim() : "";
			res[idx++] = kv.length > 1 ? kv[1].trim() : "";
		}
		return res;
	}

	private static String valor(String[] arr, String clave) {
		for (int i = 0; i < arr.length - 1; i += 2) {
			if (arr[i].equals(clave)) return arr[i + 1];
		}
		return "";
	}

	// ============ ENDPOINTS API ============

	static class ApiPeliculas implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			if ("OPTIONS".equals(t.getRequestMethod())) { cors(t); t.sendResponseHeaders(204, -1); return; }
			StringBuilder sb = new StringBuilder("[");
			Connection c = ConexionBD.getConexion();
			try {
				Statement s = c.createStatement();
				ResultSet rs = s.executeQuery(
					"SELECT p.*, ci.nombre AS cine_nombre, ci.ubicacion AS cine_ubi, ca.nombre AS cat_nombre, ci.aforo " +
					"FROM PELICULA p JOIN CINE ci ON p.id_cine = ci.id LEFT JOIN CATEGORIA ca ON p.id_categoria = ca.id ORDER BY p.id");
				boolean first = true;
				while (rs.next()) {
					if (!first) sb.append(","); first = false;
					sb.append("{");
					sb.append("\"pk\":").append(rs.getInt("id")).append(",");
					sb.append("\"nombre\":\"").append(esc(rs.getString("nombre"))).append("\",");
					sb.append("\"descripcion\":\"").append(esc(rs.getString("descripcion"))).append("\",");
					sb.append("\"precio\":").append(rs.getInt("precio")).append(",");
					sb.append("\"fecha_creacion\":\"").append(rs.getString("fecha_creacion")).append("\",");
					sb.append("\"estado\":\"").append(esc(rs.getString("estado"))).append("\",");
					sb.append("\"urlImagen\":\"").append(esc(rs.getString("url_imagen") != null ? rs.getString("url_imagen") : "")).append("\",");
					sb.append("\"pk_cine\":").append(rs.getInt("id_cine")).append(",");
					sb.append("\"cine_nombre\":\"").append(esc(rs.getString("cine_nombre"))).append("\",");
					sb.append("\"cine_ubicacion\":\"").append(esc(rs.getString("cine_ubi"))).append("\",");
				sb.append("\"nombre_categoria\":\"").append(esc(rs.getString("cat_nombre") != null ? rs.getString("cat_nombre") : "")).append("\",");
				sb.append("\"entradas_vendidas\":").append(rs.getInt("entradas_vendidas")).append(",");
				sb.append("\"aforo\":").append(rs.getInt("aforo"));
				sb.append("}");
				}
				rs.close(); s.close();
			} catch (Exception e) { sb.append("{}"); }
			sb.append("]");
			json(t, sb.toString());
		}
	}

	static class ApiAnuncios implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			if ("OPTIONS".equals(t.getRequestMethod())) { cors(t); t.sendResponseHeaders(204, -1); return; }
			StringBuilder sb = new StringBuilder("[");
			Connection c = ConexionBD.getConexion();
			try {
				Statement s = c.createStatement();
				ResultSet rs = s.executeQuery(
					"SELECT a.*, p.nombre AS peli_nombre, p.url_imagen AS peli_url " +
					"FROM ANUNCIO a JOIN PELICULA p ON a.id_pelicula = p.id ORDER BY a.id");
				boolean first = true;
				while (rs.next()) {
					if (!first) sb.append(","); first = false;
					sb.append("{");
					sb.append("\"pk\":").append(rs.getInt("id")).append(",");
					sb.append("\"fecha_publicacion\":\"").append(rs.getString("fecha_publicacion")).append("\",");
					sb.append("\"fecha_expiracion\":\"").append(rs.getString("fecha_expiracion")).append("\",");
					sb.append("\"activo\":").append(rs.getBoolean("activo")).append(",");
					sb.append("\"interacciones\":").append(rs.getInt("interacciones")).append(",");
					sb.append("\"pk_pelicula\":").append(rs.getInt("id_pelicula")).append(",");
					sb.append("\"pk_cine\":").append(rs.getInt("id_cine")).append(",");
					sb.append("\"peli_nombre\":\"").append(esc(rs.getString("peli_nombre"))).append("\",");
					sb.append("\"urlImagen\":\"").append(esc(rs.getString("peli_url") != null ? rs.getString("peli_url") : "")).append("\"");
					sb.append("}");
				}
				rs.close(); s.close();
			} catch (Exception e) { sb.append("{}"); }
			sb.append("]");
			json(t, sb.toString());
		}
	}

	static class ApiCines implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			if ("OPTIONS".equals(t.getRequestMethod())) { cors(t); t.sendResponseHeaders(204, -1); return; }
			StringBuilder sb = new StringBuilder("[");
			Connection c = ConexionBD.getConexion();
			try {
				Statement s = c.createStatement();
				ResultSet rs = s.executeQuery("SELECT * FROM CINE ORDER BY id");
				boolean first = true;
				while (rs.next()) {
					if (!first) sb.append(","); first = false;
					sb.append("{");
					sb.append("\"pk\":").append(rs.getInt("id")).append(",");
					sb.append("\"nombre\":\"").append(esc(rs.getString("nombre"))).append("\",");
					sb.append("\"ubicacion\":\"").append(esc(rs.getString("ubicacion"))).append("\",");
					sb.append("\"is_open\":").append(rs.getBoolean("is_open")).append(",");
					sb.append("\"aforo\":").append(rs.getInt("aforo"));
					sb.append("}");
				}
				rs.close(); s.close();
			} catch (Exception e) { sb.append("{}"); }
			sb.append("]");
			json(t, sb.toString());
		}
	}

	static class ApiCategorias implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			if ("OPTIONS".equals(t.getRequestMethod())) { cors(t); t.sendResponseHeaders(204, -1); return; }
			StringBuilder sb = new StringBuilder("[");
			Connection c = ConexionBD.getConexion();
			try {
				Statement s = c.createStatement();
				ResultSet rs = s.executeQuery("SELECT * FROM CATEGORIA ORDER BY id");
				boolean first = true;
				while (rs.next()) {
					if (!first) sb.append(","); first = false;
					sb.append("{\"pk\":").append(rs.getInt("id")).append(",");
					sb.append("\"nombre\":\"").append(esc(rs.getString("nombre"))).append("\",");
					sb.append("\"descripcion\":\"").append(esc(rs.getString("descripcion"))).append("\"}");
				}
				rs.close(); s.close();
			} catch (Exception e) { sb.append("{}"); }
			sb.append("]");
			json(t, sb.toString());
		}
	}

	static class ApiLogin implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			if ("OPTIONS".equals(t.getRequestMethod())) { cors(t); t.sendResponseHeaders(204, -1); return; }
			String body = leerBody(t);
			String[] arr = splitJson(body);
			String dni = valor(arr, "dni");
			String pass = valor(arr, "pass");

			Connection c = ConexionBD.getConexion();
			try {
				PreparedStatement ps = c.prepareStatement(
					"SELECT * FROM USUARIO WHERE dni = ? AND contraseña = ?");
				ps.setString(1, dni); ps.setString(2, pass);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					String resp = "{\"pk\":" + rs.getInt("id") + "," +
						"\"dni\":\"" + rs.getString("dni") + "\"," +
						"\"nombre\":\"" + esc(rs.getString("nombre")) + "\"," +
						"\"email\":\"" + esc(rs.getString("email")) + "\"," +
						"\"telefono\":" + rs.getInt("telefono") + "," +
						"\"rol\":\"" + rs.getString("rol") + "\"}";
					rs.close(); ps.close();
					json(t, resp);
					return;
				}
				rs.close(); ps.close();
			} catch (Exception e) { /* error */ }
			cors(t);
			t.sendResponseHeaders(401, -1);
		}
	}

	static class ApiRegistro implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			if ("OPTIONS".equals(t.getRequestMethod())) { cors(t); t.sendResponseHeaders(204, -1); return; }
			String body = leerBody(t);
			String[] arr = splitJson(body);
			String dni = valor(arr, "dni");
			String nombre = valor(arr, "nombre");
			String email = valor(arr, "email");
			String pass = valor(arr, "pass");
			String tel = valor(arr, "telefono");

			Connection c = ConexionBD.getConexion();
			try {
				// Verificar si DNI ya existe
				PreparedStatement check = c.prepareStatement("SELECT 1 FROM USUARIO WHERE dni = ?");
				check.setString(1, dni);
				ResultSet crs = check.executeQuery();
				if (crs.next()) { crs.close(); check.close(); cors(t); t.sendResponseHeaders(409, -1); return; }
				crs.close(); check.close();

				// Obtener siguiente id
				Statement ms = c.createStatement();
				ResultSet mrs = ms.executeQuery("SELECT COALESCE(MAX(id), 0) + 1 FROM USUARIO");
				mrs.next(); int nid = mrs.getInt(1); mrs.close(); ms.close();

				// Insertar usuario
				PreparedStatement ps = c.prepareStatement(
					"INSERT INTO USUARIO (id, dni, nombre, email, contraseña, telefono, fecha_registro, rol, registrado) VALUES (?,?,?,?,?,?,CURRENT_DATE,'Comun',TRUE)");
				ps.setInt(1, nid); ps.setString(2, dni); ps.setString(3, nombre);
				ps.setString(4, email); ps.setString(5, pass);
				ps.setInt(6, tel.isEmpty() ? 0 : Integer.parseInt(tel));
				ps.executeUpdate(); ps.close();

				ps = c.prepareStatement("INSERT INTO USUARIO_COMUN (id_usuario_comun) VALUES (?)");
				ps.setInt(1, nid); ps.executeUpdate(); ps.close();

				String resp = "{\"pk\":" + nid + ",\"dni\":\"" + dni + "\",\"nombre\":\"" + esc(nombre) + "\",\"email\":\"" + esc(email) + "\",\"rol\":\"Comun\"}";
				json(t, resp);
			} catch (Exception e) {
				cors(t);
				t.sendResponseHeaders(500, -1);
			}
		}
	}

	// ============ COMPRAR ENTRADAS ============

	static class ApiComprar implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			if ("OPTIONS".equals(t.getRequestMethod())) { cors(t); t.sendResponseHeaders(204, -1); return; }
			String body = leerBody(t);
			String[] arr = splitJson(body);
			int peliId = Integer.parseInt(valor(arr, "pelicula_id"));
			int cantidad = Integer.parseInt(valor(arr, "cantidad"));
			if (cantidad < 1) { cors(t); t.sendResponseHeaders(400, -1); return; }

			Connection c = ConexionBD.getConexion();
			try {
				// Ver aforo y entradas vendidas
				PreparedStatement ps = c.prepareStatement(
					"SELECT p.entradas_vendidas, ci.aforo FROM PELICULA p JOIN CINE ci ON p.id_cine = ci.id WHERE p.id = ?");
				ps.setInt(1, peliId);
				ResultSet rs = ps.executeQuery();
				if (!rs.next()) { rs.close(); ps.close(); cors(t); t.sendResponseHeaders(404, -1); return; }
				int vendidas = rs.getInt("entradas_vendidas");
				int aforo = rs.getInt("aforo");
				rs.close(); ps.close();

				if (vendidas + cantidad > aforo) {
					int quedan = aforo - vendidas;
					String resp = "{\"error\":\"Aforo completo. Solo quedan " + quedan + " entradas.\",\"disponibles\":" + quedan + "}";
					cors(t); t.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
					byte[] b = resp.getBytes("UTF-8");
					t.sendResponseHeaders(409, b.length);
					t.getResponseBody().write(b); t.close();
					return;
				}

				// Actualizar entradas vendidas
				ps = c.prepareStatement("UPDATE PELICULA SET entradas_vendidas = entradas_vendidas + ? WHERE id = ?");
				ps.setInt(1, cantidad); ps.setInt(2, peliId);
				ps.executeUpdate(); ps.close();

				String resp = "{\"ok\":true,\"total\":" + (vendidas + cantidad) + ",\"precio_total\":" + (cantidad * obtenerPrecio(c, peliId)) + "}";
				json(t, resp);
			} catch (Exception e) {
				cors(t); t.sendResponseHeaders(500, -1);
			}
		}

		private int obtenerPrecio(Connection c, int peliId) throws Exception {
			PreparedStatement ps = c.prepareStatement("SELECT precio FROM PELICULA WHERE id = ?");
			ps.setInt(1, peliId);
			ResultSet rs = ps.executeQuery();
			int p = rs.next() ? rs.getInt("precio") : 0;
			rs.close(); ps.close();
			return p;
		}
	}

	// ============ ARCHIVOS ESTATICOS ============

	static class Estaticos implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			String path = t.getRequestURI().getPath();
			if (path.equals("/")) path = "/index.html";
			if (path.startsWith("/")) path = path.substring(1);

			Path file = Paths.get(WEB_ROOT, path);
			if (!Files.exists(file) || Files.isDirectory(file)) {
				cors(t); t.sendResponseHeaders(404, -1); return;
			}

			String ct = "application/octet-stream";
			if (path.endsWith(".html")) ct = "text/html; charset=UTF-8";
			else if (path.endsWith(".css")) ct = "text/css; charset=UTF-8";
			else if (path.endsWith(".js")) ct = "application/javascript; charset=UTF-8";
			else if (path.endsWith(".png")) ct = "image/png";
			else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) ct = "image/jpeg";
			else if (path.endsWith(".ico")) ct = "image/x-icon";

			cors(t);
			t.getResponseHeaders().set("Content-Type", ct);
			byte[] bytes = Files.readAllBytes(file);
			t.sendResponseHeaders(200, bytes.length);
			OutputStream os = t.getResponseBody();
			os.write(bytes); os.close();
		}
	}

	private static String esc(String s) {
		if (s == null) return "";
		return s.replace("\\", "\\\\").replace("\"", "\\\"")
				.replace("\n", "\\n").replace("\r", "").replace("\t", "\\t");
	}
}
