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
	public ArrayList<Editorial> getAllEditoriales() throws BBDDException {
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
	 * Método que devuelve una Editorial dado el codEditorial pasado como parámetro
	 * @param codEditorial codEditorial a buscar
	 * @return Editorial con los datos de la editorial buscado si se ha encontrado, o 
	 * null si la editorial no existe
	 * @throws CantidadDebeSerPositivaException 
	 * @throws BBDDException 
	 */
	public Editorial getEditorial(int codEditorial) throws BBDDException {
		Editorial ed = null;
		
		String consulta = "select * from editoriales where codEditorial = "+codEditorial;
		
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
				String nombre = resultado.getString("nombre");
				int anio = resultado.getInt("anio");
				//no hace falta recoger el codEditorial
				
				
				// Instanciamos el objeto de tipo Libro
				ed = new Editorial(nombre, codEditorial, anio);
				
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
		return ed;
	}
	
	/**
	 * Método que inserta una editorial en la base de datos
	 * @param e Editorial a insertar
	 * 
	 */
	public void insertarEditorial(Editorial ed) throws BBDDException {
		try {
			con=this.conexion.getConexion();
			String consulta= "insert into editoriales (nombre,anio)"
					+ "values(?, ?)";
			
			sentenciaPrep=con.prepareStatement(consulta);
			
			// incializamos la sentencia preparada indicando por qué valor debe sustituir las interrogaciones
			sentenciaPrep.setString(1, ed.getNombre());		
			sentenciaPrep.setInt(2, ed.getAnio());
			
			sentenciaPrep.executeUpdate();
			
			
		} catch (SQLException e1) {
			System.out.println("Error al insertar "+e1.getMessage()+e1.getErrorCode());
			// controlamos si se ha el duplicado la calve primaria
			if (e1.getErrorCode()==1062) {
				throw new BBDDException("Error insertando. Clave duplicada");
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
	 * Método de la clase que edita una editorial en la base de datos
	 * @param e Editorial a insertar
	 * @throws ErrorBBDDException en caso de que no se haya podido editar
	 */
	public void editarEditorial (Editorial ed) throws BBDDException {
		try {
			con=this.conexion.getConexion();
			String consulta= "update editorial set nombre=?, codEditorial=?, anio=?, num_pags=? where codEditorial=?";
			
			sentenciaPrep=con.prepareStatement(consulta);
			
			// incializamos la sentencia preparada indicando por qué valor debe sustituir las interrogaciones
			
			sentenciaPrep.setString(1, ed.getNombre());
			sentenciaPrep.setInt(2, ed.getCodEditorial());
			sentenciaPrep.setInt(3, ed.getAnio());
			
			sentenciaPrep.executeUpdate();
			
			
		} catch (SQLException e1) {
			System.out.println("Error al insertar "+e1.getMessage()+e1.getErrorCode());

			throw new BBDDException("Error editando el codEditorial.");
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
	 * Método que elimina una editorial de la tabla de la base de datos
	 * @param codEditorial int con el codEditorial de la editorial a borrar
	 * @return true en caso de borrado satisfactorio y false en caso contrario
	 * @throws BBDDException 
	 */
	public boolean eliminarEditorial(String codEditorial) throws BBDDException {
		boolean res=false;
		try {
			con=this.conexion.getConexion();
			String consulta= "delete from editoriales where codEditorial=?";
			
			sentenciaPrep=con.prepareStatement(consulta);
			
			// incializamos la sentencia preparada indicando porque valor debe sustituir las interrogaciones
			sentenciaPrep.setString(1, codEditorial);

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
