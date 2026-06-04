package Clases;

import java.io.Serializable;

public abstract class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;
	private static int contador = 0;
	
	private Integer pk;
	private String dni;
	private String nombre;
	private String email;
	private String contraseña;
	private int telefono;
	private int fecha_registro;
	private String rol;
	private boolean registrado = false;

	public Usuario(String dni, String nombre, String email, String contraseña, int telefono, int fecha_registro) {
		this.pk = ++contador;
		this.dni = dni;
		this.nombre = nombre;
		this.email = email;
		this.contraseña = contraseña;
		this.telefono = telefono;
		this.fecha_registro = fecha_registro;
		this.registrado = true;
	}

	public static void setContador(int max) {
		contador = max;
	}

	public static int getContador() {
		return contador;
	}

	public Integer getPk() {
		return pk;
	}

	public void setPk(Integer pk) {
		this.pk = pk;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public int getTelefono() {
		return telefono;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}

	public int getFecha_registro() {
		return fecha_registro;
	}

	public void setFecha_registro(int fecha_registro) {
		this.fecha_registro = fecha_registro;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public boolean isRegistrado() {
		return registrado;
	}

	public void setRegistrado(boolean registrado) {
		this.registrado = registrado;
	}

}
