/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package configuracion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase que maneja la conexión a la base de datos MySQL del sistema de venta.
 * Establece una conexión única que puede ser utilizada por los objetos DAO.
 *
 * @author kevinrengifo
 * @version 1.0
 */
public class ConexionBD {

    /**
     * Objeto de conexión a la base de datos
     */
    private Connection conexion;

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
     * Constructor que establece la conexión con la base de datos. La conexión
     * se establece automáticamente al instanciar la clase.
     */
    public ConexionBD() {
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("Conexión establecida correctamente");
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    /**
     * Devuelve el objeto Connection para ser utilizado en operaciones de base
     * de datos.
     *
     * @return Objeto Connection que representa la conexión a la base de datos
     */
    public Connection getConexion() {
        return conexion;
    }

    /**
     * Cierra la conexión con la base de datos si está abierta. Este método debe
     * ser llamado cuando ya no se necesite la conexión para liberar recursos
     * del sistema.
     */
    public void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión cerrada correctamente");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}
