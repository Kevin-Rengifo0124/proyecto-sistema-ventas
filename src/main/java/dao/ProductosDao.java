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
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import modelo.Productos;

/**
 *
 * @author kevinrengifo
 */
public class ProductosDao {

    private final ConexionBD conexionBD = new ConexionBD();
    private Connection conexion;
    private PreparedStatement sentenciaPreparada;
    private ResultSet resultadoConsulta;

    public boolean registrarProducto(Productos producto) {
        String insertar = "INSERT INTO productos (codigo, nombre, proveedor, stock, precio) VALUES (?,?,?,?,?)";
        try {
            conexion = conexionBD.getConexion();
            sentenciaPreparada = conexion.prepareStatement(insertar);
            sentenciaPreparada.setString(1, producto.getCodigo());
            sentenciaPreparada.setString(2, producto.getNombre());
            sentenciaPreparada.setString(3, producto.getProveedor());
            sentenciaPreparada.setInt(4, producto.getStock());
            sentenciaPreparada.setDouble(5, producto.getPrecio());
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

    public void consultarProveedor(JComboBox proveedor) {
        String llenarComboBox = "SELECT nombre FROM proveedor";

        try {
            conexion = conexionBD.getConexion();
            sentenciaPreparada = conexion.prepareStatement(llenarComboBox);
            resultadoConsulta = sentenciaPreparada.executeQuery();

            while (resultadoConsulta.next()) {

                proveedor.addItem(resultadoConsulta.getString("nombre"));

            }
        } catch (SQLException excepcion) {

            System.out.println(excepcion.toString());
        }

    }

    public List listarProductos() {
        List<Productos> listaProductos = new ArrayList<>();
        String listar = "SELECT * FROM productos";

        try {
            conexion = conexionBD.getConexion();
            sentenciaPreparada = conexion.prepareStatement(listar);
            resultadoConsulta = sentenciaPreparada.executeQuery();

            while (resultadoConsulta.next()) {
                Productos productos = new Productos();
                productos.setId(resultadoConsulta.getInt("id"));
                productos.setCodigo(resultadoConsulta.getString("codigo"));
                productos.setNombre(resultadoConsulta.getString("nombre"));
                productos.setProveedor(resultadoConsulta.getString("proveedor"));
                productos.setStock(resultadoConsulta.getInt("stock"));
                productos.setPrecio(resultadoConsulta.getDouble("precio"));
                listaProductos.add(productos);
            }
        } catch (SQLException excepcion) {
            System.out.println(excepcion.toString());
        }
        return listaProductos;
    }

    public boolean eliminarProducto(int id) {
        String eliminar = "DELETE FROM productos WHERE id = ?";
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

    public boolean modificarProducto(Productos producto) {
        String modificar = "UPDATE productos SET codigo=?, nombre=?, proveedor=?, stock=?, precio=? WHERE id=?";
        try {
            conexion = conexionBD.getConexion();
            sentenciaPreparada = conexion.prepareStatement(modificar);
            sentenciaPreparada.setString(1, producto.getCodigo());
            sentenciaPreparada.setString(2, producto.getNombre());
            sentenciaPreparada.setString(3, producto.getProveedor());
            sentenciaPreparada.setInt(4, producto.getStock());
            sentenciaPreparada.setDouble(5, producto.getPrecio());
            sentenciaPreparada.setInt(6, producto.getId());
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
