package dao;

import configuracion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.Login;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Objeto de Acceso a Datos para operaciones de inicio de sesión
 *
 * @author kevinrengifo
 */
public class LoginDao {

    private Connection conexion;
    private PreparedStatement sentenciaPreparada;
    private ResultSet resultadoConsulta;
    private final ConexionBD conexionBD = new ConexionBD();

    /**
     * Autentica un usuario por correo y clave
     *
     * @param correo Correo del usuario
     * @param clave Clave del usuario
     * @return Objeto Login con datos del usuario si está autenticado, objeto
     * Login vacío si no
     */
    public Login autenticarUsuario(String correo, String clave) {
        Login datosUsuario = new Login();
        String consulta = "SELECT * FROM usuarios WHERE correo = ?";  // Ya no buscamos por clave

        try {
            conexion = conexionBD.getConexion();
            sentenciaPreparada = conexion.prepareStatement(consulta);
            sentenciaPreparada.setString(1, correo);
            resultadoConsulta = sentenciaPreparada.executeQuery();

            if (resultadoConsulta.next()) {
                // Verificar si la contraseña coincide con el hash almacenado
                String hashedPasswordFromDB = resultadoConsulta.getString("clave");

                if (BCrypt.checkpw(clave, hashedPasswordFromDB)) {
                    // La contraseña es correcta, cargar datos del usuario
                    datosUsuario.setId(resultadoConsulta.getInt("id"));
                    datosUsuario.setNombre(resultadoConsulta.getString("nombre"));
                    datosUsuario.setCorreo(resultadoConsulta.getString("correo"));
                    datosUsuario.setClave(resultadoConsulta.getString("clave"));
                    datosUsuario.setRol(resultadoConsulta.getString("rol"));
                }
            }
        } catch (SQLException excepcion) {
            System.err.println("Error de autenticación: " + excepcion.getMessage());
        } finally {
            cerrarRecursos();
        }

        return datosUsuario;
    }

    public boolean registrarUsuarios(Login login) {
        String registrar = "INSERT INTO usuarios (nombre, correo, clave, rol) VALUES (?,?,?,?)";

        try {
            conexion = conexionBD.getConexion();
            sentenciaPreparada = conexion.prepareStatement(registrar);
            sentenciaPreparada.setString(1, login.getNombre());
            sentenciaPreparada.setString(2, login.getCorreo());

            // Generar hash de la contraseña antes de guardarla
            String hashedPassword = BCrypt.hashpw(login.getClave(), BCrypt.gensalt());
            sentenciaPreparada.setString(3, hashedPassword);

            sentenciaPreparada.setString(4, login.getRol());
            sentenciaPreparada.execute();
            return true;
        } catch (Exception excepcion) {
            System.out.println(excepcion.toString());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Cierra los recursos de la base de datos para prevenir fugas de memoria
     */
    private void cerrarRecursos() {
        try {
            if (resultadoConsulta != null) {
                resultadoConsulta.close();
            }
            if (sentenciaPreparada != null) {
                sentenciaPreparada.close();
            }
            if (conexion != null) {
                conexion.close();
            }
        } catch (SQLException excepcion) {
            System.err.println("Error al cerrar recursos: " + excepcion.getMessage());
        }
    }
}
