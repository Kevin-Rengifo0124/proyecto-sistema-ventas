/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import configuracion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.Proveedor;

/**
 *
 * @author kevinrengifo
 */
public class ProveedorDao {

    private final ConexionBD conexionBD = new ConexionBD();
    private Connection conexion;
    private PreparedStatement sentenciaPreparada;
    private ResultSet resultadoConsulta;

    public boolean registrarProveedor(Proveedor proveedor) {
        String insertar = "INSERT INTO proveedor (ruc, nombre, telefono, direccion, razon) VALUES (?,?,?,?,?)";
        try {
            conexion = conexionBD.getConexion();
            sentenciaPreparada = conexion.prepareStatement(insertar);
            sentenciaPreparada.setInt(1, proveedor.getRuc());
            sentenciaPreparada.setString(2, proveedor.getNombre());
            sentenciaPreparada.setInt(3, proveedor.getTelefono());
            sentenciaPreparada.setString(4, proveedor.getDireccion());
            sentenciaPreparada.setString(5, proveedor.getRazonSocial());
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

    public List listarProveedores() {
        List<Proveedor> listaProveedores = new ArrayList<>();
        String listar = "SELECT * FROM proveedor";

        try {
            conexion = conexionBD.getConexion();
            sentenciaPreparada = conexion.prepareStatement(listar);
            resultadoConsulta = sentenciaPreparada.executeQuery();

            while (resultadoConsulta.next()) {
                Proveedor proveedor = new Proveedor();
                proveedor.setId(resultadoConsulta.getInt("id"));
                proveedor.setRuc(resultadoConsulta.getInt("ruc"));
                proveedor.setNombre(resultadoConsulta.getString("nombre"));
                proveedor.setTelefono(resultadoConsulta.getInt("telefono"));
                proveedor.setDireccion(resultadoConsulta.getString("direccion"));
                proveedor.setRazonSocial(resultadoConsulta.getString("razon"));
                listaProveedores.add(proveedor);
            }
        } catch (SQLException excepcion) {
            System.out.println(excepcion.toString());
        }
        return listaProveedores;
    }

    public boolean eliminarProveedor(int id) {
        String eliminar = "DELETE FROM proveedor WHERE id = ?";
        try {
            conexion = conexionBD.getConexion();
            sentenciaPreparada = conexion.prepareStatement(eliminar);
            sentenciaPreparada.setInt(1, id);
            sentenciaPreparada.execute();
            return true;
        } catch (SQLException excepcion) {
            System.out.println(excepcion.toString());
            return false;
        } finally {
            try {
                conexion.close();
            } catch (SQLException excepcion) {
                System.out.println(excepcion.toString());
            }
        }
    }

    public boolean modificarProveedor(Proveedor proveedor) {
        String modificar = "UPDATE proveedor SET ruc=?, nombre=?, telefono=?, direccion=?, razon=? WHERE id=?";
        try {
            conexion = conexionBD.getConexion();
            sentenciaPreparada = conexion.prepareStatement(modificar);
            sentenciaPreparada.setInt(1, proveedor.getRuc());
            sentenciaPreparada.setString(2, proveedor.getNombre());
            sentenciaPreparada.setInt(3, proveedor.getTelefono());
            sentenciaPreparada.setString(4, proveedor.getDireccion());
            sentenciaPreparada.setString(5, proveedor.getRazonSocial());
            sentenciaPreparada.setInt(6, proveedor.getId());
            sentenciaPreparada.execute();
            return true;
        } catch (SQLException excepcion) {
            System.out.println(excepcion.toString());
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
