package Clases;

public class Usuario_Comun extends Usuario {

	private static final long serialVersionUID = 1L;

	public Usuario_Comun(String dni, String nombre, String email, String contraseña, int telefono, int fecha_registro) {
		super(dni, nombre, email, contraseña, telefono, fecha_registro);
		super.setRol("Comun");
	}

}
