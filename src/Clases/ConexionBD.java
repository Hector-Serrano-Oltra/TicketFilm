package Clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConexionBD {

	// Cambia la IP por la de la máquina donde corre PostgreSQL
	private static final String HOST = "localhost";   // ej: "192.168.1.100"
	private static final String URL = "jdbc:postgresql://" + HOST + ":5432/TicketFilm";
	private static final String USER = "postgres";
	private static final String PASS = "Hecso02";

	private static Connection conexion = null;
	private static boolean intentado = false;
	private static boolean conectado = false;

	public static Connection getConexion() {
		try {
			if (conexion == null || conexion.isClosed()) {
				conectar();
			}
		} catch (SQLException e) {
			conectar();
		}
		return conexion;
	}

	public static boolean isConectado() {
		if (!intentado) getConexion();
		return conectado;
	}

	private static void conectar() {
		intentado = true;
		try {
			Class.forName("org.postgresql.Driver");
			conexion = DriverManager.getConnection(URL, USER, PASS);
			conectado = true;
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null,
				"No se encontro el driver PostgreSQL.\nAsegurate de que postgresql-42.7.5.jar esta en la carpeta lib/",
				"Error de conexion", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
				"No se pudo conectar a PostgreSQL.",
				"Error de conexion", JOptionPane.WARNING_MESSAGE);
		}
	}

	public static void cerrar() {
		try {
			if (conexion != null && !conexion.isClosed()) {
				conexion.close();
			}
		} catch (SQLException e) {
			System.err.println("Error al cerrar: " + e.getMessage());
		}
	}

}
