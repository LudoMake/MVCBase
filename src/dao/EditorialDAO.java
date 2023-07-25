package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import excepciones.BBDDException;
import excepciones.CantidadDebeSerPositivaException;
import modelo.Editorial;
import modelo.Libro;
import utilidades.ConexionBD;

public class EditorialDAO {
	//esto debe aparecer si o si para que funcione
	private ConexionBD conexion;
	private Statement sentencia;
	private PreparedStatement sentenciaPrep;
	private ResultSet resultado;
	private Connection con;
	//hasta aqui
	
	//metodo constructor
	public EditorialDAO() {
		this.conexion = new ConexionBD();
	}
	
	/**
	 * Método de la clase que devuelve todas las editoriales de la tabla editoriales
	 * @return Arraylist<editoriales> con las editoriales o un ArrayList vacío en caso de que 
	 * no devuelva resultados. 
	 * @throws CantidadDebeSerPositivaException cuando se recogen valores negativos en cantidad
	 * @throws BBDDException se produce error en la base de datos
	 */
	public ArrayList<Editorial> getAllLibros() {
		// instanciamos la lista
		ArrayList<Editorial> lista = new ArrayList<Editorial>();
		
		
		String consulta = "select * from editoriales";
		
		try {
			// Conectamos con la base de datos
			con = this.conexion.getConexion();
			// Crea el objeto Statement con el que se pueden lanzar consultas
			sentencia = con.createStatement();
			// Se ejecuta la consulta y se recoge el ResultSet (resultado)
			resultado = sentencia.executeQuery(consulta);
			
			// Hacemos un bucle para recorrer el cursor con los resultados
			// next devuelve true mientras haya datos, false en caso contrario
			while(resultado.next()) {
				
				// recogemos todos los datos invocando a los método  getters correspondientes
				String nombre = resultado.getString("nombre");
				int codEditorial = resultado.getInt("codEditorial");
				int anio = resultado.getInt("anio");
				
				// Instanciamos el objeto de tipo Editorial
				Editorial e = new Editorial(nombre, codEditorial, anio);
				
				
				lista.add(e);
			}
		} catch (SQLException e) {
			System.out.println("Error en la consulta "+e.getMessage());
			throw new BBDDException("Error al realizar la consulta. Consulte con el administrador");
		} finally {
			try {
				resultado.close();
				sentencia.close();
				conexion.desconectar();
			} catch (SQLException e) {
				System.out.println("error liberando recursos. " + e.getMessage());
			}
			
		}
		return lista;
	}
	
	/**
	 * Método que devuelve un Libro dado el isbn pasado como parámetro
	 * @param isbn isbn a buscar
	 * @return Libro con los datos del libro buscado si se ha encontrado, o 
	 * null si el libro no existe
	 * @throws CantidadDebeSerPositivaException 
	 * @throws BBDDException 
	 */
	public Libro getLibro(String isbn) throws CantidadDebeSerPositivaException, BBDDException {
		Libro l = null;
		
		String consulta = "select * from libros where isbn = "+isbn;
		
		try {
			// Conectamos con la base de datos
			con = this.conexion.getConexion();
			// Crea el objeto Statement con el que se pueden lanzar consultas
			sentencia = con.createStatement();
			// Se ejecuta la consulta y se recoge el ResultSet (resultado)
			resultado = sentencia.executeQuery(consulta);
			
			// Hacemos un bucle para recorrer el cursor con los resultados
			// next devuelve true mientras haya datos, false en caso contrario
			if(resultado.next()) {
				
				// recogemos todos los datos invocando a los método  getters correspondientes
				String titulo = resultado.getString("titulo");
				int codEditorial = resultado.getInt("CodEditorial");
				int anio = resultado.getInt("anio");
				int numPags = resultado.getInt("num_pags");
				double precio = resultado.getDouble("precio");
				int cantidad = resultado.getInt("cantidad");
				double precioCD= resultado.getDouble("precioCD");
				
				// Instanciamos el objeto de tipo Libro
				l = new Libro(isbn, titulo, codEditorial, anio, numPags, precio, 
						cantidad, precioCD);
				
			}
		} catch (SQLException e) {
			System.out.println("Error en la consulta "+e.getMessage());
			throw new BBDDException("Error al realizar la consulta. Consulte con el administrador");
		} finally {
			try {
				resultado.close();
				sentencia.close();
				conexion.desconectar();
			} catch (SQLException e) {
				System.out.println("error liberando recursos. " + e.getMessage());
			}
			
		}
		return l;
	}
	
	/**
	 * Método que inserta un libro en la base de datos
	 * @param l Libro a insertar
	 * 
	 */
	public void insertarLibro(Libro l) throws BBDDException {
		try {
			con=this.conexion.getConexion();
			String consulta= "insert into libros "
					+ "values(?, ?, ?, ?, ?, ?, ?, ? )";
			
			sentenciaPrep=con.prepareStatement(consulta);
			
			// incializamos la sentencia preparada indicando porque valor debe sustituir 
			// las interrogaciones
			sentenciaPrep.setString(1, l.getIsbn());
			sentenciaPrep.setString(2, l.getTitulo());
			sentenciaPrep.setInt(3, l.getCodEditorial());
			sentenciaPrep.setInt(4, l.getAnio());
			sentenciaPrep.setInt(5, l.getNumPags());
			sentenciaPrep.setDouble(6, l.getPrecio());
			sentenciaPrep.setInt(7, l.getCantidad());
			sentenciaPrep.setDouble(8, l.getPrecioCD());
			
			sentenciaPrep.executeUpdate();
			
			
		} catch (SQLException e1) {
			System.out.println("Error al insertar "+e1.getMessage()+e1.getErrorCode());
			// controlamos si se ha el duplicado la calve primaria
			if (e1.getErrorCode()==1062) {
				throw new BBDDException("Error insertando. Clave duplicado");
			} else if (e1.getErrorCode()==1216) {
				throw new BBDDException("Código Editorial no existe");
			}
			throw new BBDDException("Error al insertar");
		} finally {
			try {
				sentenciaPrep.close();
				conexion.desconectar();
			} catch (SQLException e) {
				// TODO Bloque catch generado automáticamente
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Método de la clase que edita un libro en la base de datos
	 * @param l Libro a insertar
	 * @throws ErrorBBDDException en caso de que no se haya podido editar
	 */
	public void editarLibro (Libro l) throws BBDDException {
		try {
			con=this.conexion.getConexion();
			String consulta= "update libros set titulo=?, codEditorial=?, anio=?, num_pags=?,"
					+ " precio = ?, cantidad=?, precioCD=?"
					+ " where isbn=?";
			
			sentenciaPrep=con.prepareStatement(consulta);
			
			// incializamos la sentencia preparada indicando porque valor debe sustituir 
			// las interrogaciones
			
			sentenciaPrep.setString(1, l.getTitulo());
			sentenciaPrep.setInt(2, l.getCodEditorial());
			sentenciaPrep.setInt(3, l.getAnio());
			sentenciaPrep.setInt(4, l.getNumPags());
			sentenciaPrep.setDouble(5, l.getPrecio());
			sentenciaPrep.setInt(6, l.getCantidad());
			sentenciaPrep.setDouble(7, l.getPrecioCD());
			sentenciaPrep.setString(8, l.getIsbn());
			
			sentenciaPrep.executeUpdate();
			
			
		} catch (SQLException e1) {
			System.out.println("Error al insertar "+e1.getMessage()+e1.getErrorCode());

			throw new BBDDException("Error editando el empleado.");
		} finally {
			try {
				sentenciaPrep.close();
				conexion.desconectar();
			} catch (SQLException e) {
				// TODO Bloque catch generado automáticamente
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Método que elimina un libro de la tabla de la base de datos
	 * @param isbn String con el isbn del libro a borrar
	 * @return true en caso de borrado satisfactorio y false en caso contrario
	 * @throws BBDDException 
	 */
	public boolean eliminarLibro(String isbn) throws BBDDException {
		boolean res=false;
		try {
			con=this.conexion.getConexion();
			String consulta= "delete from libros where isbn=?";
			
			sentenciaPrep=con.prepareStatement(consulta);
			
			// incializamos la sentencia preparada indicando porque valor debe sustituir 
			// las interrogaciones
			sentenciaPrep.setString(1, isbn);

			sentenciaPrep.executeUpdate();
			res=true;
	
		} catch (SQLException e1) {
			System.out.println("Error al eliminar "+e1.getMessage());
			throw new BBDDException("Error al eliminar el libro");
		} finally {
			try {
				sentenciaPrep.close();
			} catch (SQLException e) {
				// TODO Bloque catch generado automáticamente
				e.printStackTrace();
			}
			conexion.desconectar();
		}
		return res;
	}
}

}
