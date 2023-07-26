package modelo;

import java.util.Objects;

import excepciones.CantidadDebeSerPositivaException;

public class Editorial {
	private String nombre;
	private int codEditorial; //integer para añadir numeros nulos
	private int anio;

	public Editorial(String nombre, int codEditorial, int anio) {
		super();
		this.codEditorial = codEditorial;
		this.nombre=nombre;
		this.anio=anio;

	}
	
	public Editorial(String nombre, int anio) {
		super();
		this.nombre=nombre;
		this.anio=anio;

	}
	

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getCodEditorial() {
		return codEditorial;
	}

	public void setCodEditorial(int codEditorial) {
		this.codEditorial = codEditorial;
	}

	public int getAnio() {
		return anio;
	}

	public void setAnio(int anio) {
		this.anio = anio;
	}

	@Override
	public String toString() {
		return "Editorial [nombre=" + nombre + ", codEditorial=" + codEditorial + ", anio=" + anio + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(codEditorial);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Editorial other = (Editorial) obj;
		return codEditorial == other.codEditorial;
	}
}