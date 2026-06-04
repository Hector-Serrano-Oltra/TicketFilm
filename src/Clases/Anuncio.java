package Clases;

import java.io.Serializable;

public class Anuncio implements Serializable {

	private static final long serialVersionUID = 1L;
	private static int contador = 0;

	private Integer pk;
	private String fecha_expiracion;
	private String fecha_publicacion;
	private boolean activo;
	private int interacciones;
	private Integer pk_pelicula;
	private Integer pk_cine;

	public Anuncio(String fecha_expiracion, String fecha_publicacion, boolean activo, Pelicula pelicula, Cine cine) {
		this.pk = ++contador;
		this.interacciones = 0;
		this.fecha_expiracion = fecha_expiracion;
		this.fecha_publicacion = fecha_publicacion;
		this.activo = activo;
		this.pk_pelicula = pelicula.getPk();
		this.pk_cine = cine.getPk();
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

	public String getFecha_expiracion() {
		return fecha_expiracion;
	}

	public void setFecha_expiracion(String fecha_expiracion) {
		this.fecha_expiracion = fecha_expiracion;
	}

	public String getFecha_publicacion() {
		return fecha_publicacion;
	}

	public void setFecha_publicacion(String fecha_publicacion) {
		this.fecha_publicacion = fecha_publicacion;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public int getInteracciones() {
		return interacciones;
	}

	public void setInteracciones(int interacciones) {
		this.interacciones = interacciones;
	}

	public Integer getPk_pelicula() {
		return pk_pelicula;
	}

	public void setPk_pelicula(Integer pk_pelicula) {
		this.pk_pelicula = pk_pelicula;
	}

	public Integer getPk_cine() {
		return pk_cine;
	}

	public void setPk_cine(Integer pk_cine) {
		this.pk_cine = pk_cine;
	}

	public String toString() {
		return "Anuncio #" + pk;
	}

}
