/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import configuracion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import modelo.Cliente;

/**
 *
 * @author kevinrengifo
 */
public class ClienteDao {

    private final ConexionBD conexionBD = new ConexionBD();
    private Connection conexion;
    private PreparedStatement sentenciaPreparada;

    public boolean registrarCliente(Cliente cliente) {
        String insertar = "INSERT INTO clientes (dni, nombre, telefono, direccion, razon) VALUES (?,?,?,?,?)";
        try {
            conexion = conexionBD.getConexion();
            sentenciaPreparada = conexion.prepareStatement(insertar);
            sentenciaPreparada.setInt(1, cliente.getDni());
            sentenciaPreparada.setString(2, cliente.getNombre());
            sentenciaPreparada.setInt(3, cliente.getTelefono());
            sentenciaPreparada.setString(4, cliente.getDireccion());
            sentenciaPreparada.setString(5, cliente.getRazonSocial());
            sentenciaPreparada.execute();
            return true;

        } catch (SQLException excepcion) {
            JOptionPane.showMessageDialog(null, excepcion.toString());
            return false;
        } finally {
            try {
                conexion.close();
            } catch (SQLException excepcion) {
                System.out.println(excepcion.toString());
            }
        }
    }

}
