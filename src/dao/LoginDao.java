package dao;

import configuracion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.Login;

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
        String consulta = "SELECT * FROM usuarios WHERE correo = ? AND clave = ?";

        try {
            conexion = conexionBD.getConexion();
            sentenciaPreparada = conexion.prepareStatement(consulta);
            sentenciaPreparada.setString(1, correo);
            sentenciaPreparada.setString(2, clave);
            resultadoConsulta = sentenciaPreparada.executeQuery();

            if (resultadoConsulta.next()) {
                datosUsuario.setId(resultadoConsulta.getInt("id"));
                datosUsuario.setNombre(resultadoConsulta.getString("nombre"));
                datosUsuario.setCorreo(resultadoConsulta.getString("correo"));
                datosUsuario.setClave(resultadoConsulta.getString("clave"));
            }
        } catch (SQLException excepcion) {
            System.err.println("Error de autenticación: " + excepcion.getMessage());
        } finally {
            cerrarRecursos();
        }

        return datosUsuario;
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
