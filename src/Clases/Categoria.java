package Clases;

import java.io.Serializable;

public class Categoria implements Serializable {

	private static final long serialVersionUID = 1L;
	private static int contador = 0;

	private Integer pk;
	private String nombre;
	private String descripcion;
	private Integer pk_admin;

	public Categoria(String nombre, String descripcion, Admin admin) {
		this.pk = ++contador;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.pk_admin = admin.getPk();
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getPk_admin() {
		return pk_admin;
	}

	public void setPk_admin(Integer pk_admin) {
		this.pk_admin = pk_admin;
	}

	public String toString() {
		return nombre;
	}

}
