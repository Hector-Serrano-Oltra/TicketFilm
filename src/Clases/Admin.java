package Clases;

public class Admin extends Usuario {

	private static final long serialVersionUID = 1L;

	public Admin(String dni, String nombre, String email, String contraseña, int telefono, int fecha_registro) {
		super(dni, nombre, email, contraseña, telefono, fecha_registro);
		super.setRol("Admin");
	}

}
