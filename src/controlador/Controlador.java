package controlador;

import java.util.ArrayList;

import dao.EditorialDAO;
import dao.LibrosDAO;
import excepciones.BBDDException;
import modelo.Editorial;
import modelo.Libro;
import vistas.NuevaEditorialDialog;
import vistas.NuevoLibroDialog;
import vistas.VentanaPpal;

public class Controlador {
	
	// Lista de Libros y editoriales
	private ArrayList<Libro> listaLibros;
	private ArrayList<Editorial> listaEditoriales;
	
	// Referencias a las ventanas de la aplicación
	private VentanaPpal vPrincipal;
	private NuevoLibroDialog vNuevoLibro;
	private NuevaEditorialDialog vNuevaEditorial;
	
	// Definimos los objetos de acceso a datos (DAO)
	private LibrosDAO daoLibro;
	private EditorialDAO daoEditorial;
	
	public Controlador() {
		
		// Instanciamos las ventanas/cuadro de diálogo
		this.vPrincipal = new VentanaPpal();
		this.vNuevoLibro = new NuevoLibroDialog();
		this.vNuevaEditorial = new NuevaEditorialDialog();
		
		// Pasamos una copia del controlador a las vistas.
		this.vPrincipal.setControlador(this);
		this.vNuevoLibro.setControlador(this);
		this.vNuevaEditorial.setControlador(this);
		
		// Instanciamos el DAO del Libro
		this.daoLibro = new LibrosDAO();
		this.daoEditorial = new EditorialDAO();
	}
	
	public void iniciarPrograma() {
		this.vPrincipal.setVisible(true);
	}

	public void mostrarInsertarLibro() {
		this.vNuevoLibro.setModal(true);
		this.vNuevoLibro.setVisible(true);
	}

	public void insertarLibro(Libro l) throws BBDDException {
		this.daoLibro.insertarLibro(l);
		this.vNuevoLibro.setVisible(false);
		
	}

	public void mostrarInsertarEditorial() {
		this.vNuevaEditorial.setModal(true);
		this.vNuevaEditorial.setVisible(true);
		
	}

	public void insertarEditorial(Editorial e) throws BBDDException {
		this.daoEditorial.insertarEditorial(e);
		this.vNuevaEditorial.setVisible(false);
		}
}
