package Clases;

import java.io.Serializable;

public class Pelicula implements Serializable {

	private static final long serialVersionUID = 1L;
	private static int contador = 0;

	private Integer pk;
	private String nombre;
	private String descripcion;
	private int precio;
	private String fecha_creacion;
	private String estado;
	private Integer pk_cine;
	private String nombre_categoria;
	private String urlImagen;

	public Pelicula(String nombre, String descripcion, int precio, String fecha_creacion, String estado, Cine cine, Categoria categoria) {
		this(nombre, descripcion, precio, fecha_creacion, estado, cine, categoria, "");
	}

	public Pelicula(String nombre, String descripcion, int precio, String fecha_creacion, String estado, Cine cine, Categoria categoria, String urlImagen) {
		this.pk = ++contador;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.fecha_creacion = fecha_creacion;
		this.estado = estado;
		this.pk_cine = cine.getPk();
		cine.setPk_pelicula(this.pk);
		this.nombre_categoria = categoria.getNombre();
		this.urlImagen = urlImagen;
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

	public int getPrecio() {
		return precio;
	}

	public void setPrecio(int precio) {
		this.precio = precio;
	}

	public String getFecha_creacion() {
		return fecha_creacion;
	}

	public void setFecha_creacion(String fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Integer getPk_cine() {
		return pk_cine;
	}

	public void setPk_cine(Integer pk_cine) {
		this.pk_cine = pk_cine;
	}

	public String getNombre_categoria() {
		return nombre_categoria;
	}

	public void setNombre_categoria(String nombre_categoria) {
		this.nombre_categoria = nombre_categoria;
	}

	public String getUrlImagen() {
		return urlImagen;
	}

	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}

	public String toString() {
		return nombre;
	}

}
