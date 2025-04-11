package configuracion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase que maneja la conexión a la base de datos MySQL del sistema de venta.
 * Establece una conexión bajo demanda que puede ser utilizada por los objetos DAO.
 *
 * @author kevinrengifo
 * @version 1.1
 */
public class ConexionBD {
    /**
     * URL de conexión a la base de datos MySQL
     */
    private static final String URL = "jdbc:mysql://localhost:3307/sistemaventa?serverTimezone=UTC";
    /**
     * Nombre de usuario para la conexión a la base de datos
     */
    private static final String USUARIO = "root";
    /**
     * Contraseña para la conexión a la base de datos
     */
    private static final String PASSWORD = "";
    
    /**
     * Devuelve una nueva conexión a la base de datos cada vez que se llama.
     *
     * @return Objeto Connection que representa la conexión a la base de datos
     */
    public Connection getConexion() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("Conexión establecida correctamente");
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return conexion;
    }
}