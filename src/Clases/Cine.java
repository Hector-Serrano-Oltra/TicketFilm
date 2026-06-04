package Clases;

import java.io.Serializable;
import java.util.ArrayList;

public class Cine implements Serializable {

	private static final long serialVersionUID = 1L;
	private static int contador = 0;

	private Integer pk;
	private String nombre;
	private String ubicacion;
	private boolean is_open;
	private int aforo;
	private ArrayList<Integer> pk_pelicula = new ArrayList<Integer>();

	public Cine(String nombre, String ubicacion, boolean is_open, int aforo) {
		this.pk = ++contador;
		this.nombre = nombre;
		this.ubicacion = ubicacion;
		this.is_open = is_open;
		this.aforo = aforo;
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

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public boolean isIs_open() {
		return is_open;
	}

	public void setIs_open(boolean is_open) {
		this.is_open = is_open;
	}

	public int getAforo() {
		return aforo;
	}

	public void setAforo(int aforo) {
		this.aforo = aforo;
	}

	public Integer getPk_pelicula(int n) {
		if (n >= 0 && n < pk_pelicula.size()) {
			return pk_pelicula.get(n);
		}
		return -1;
	}

	public ArrayList<Integer> getPk_pelicula() {
		return pk_pelicula;
	}

	public void setPk_pelicula(Integer pk_pelicula) {
		this.pk_pelicula.add(pk_pelicula);
	}

	public String toString() {
		return nombre;
	}

}
