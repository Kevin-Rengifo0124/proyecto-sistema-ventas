package dao;

import configuracion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.Producto;

/**
 * Objeto de Acceso a Datos para operaciones de productos
 * 
 * @author [tu nombre]
 */
public class ProductoDao {
    private final ConexionBD conexionBD = new ConexionBD();
    private Connection conexion;
    private PreparedStatement sentenciaPreparada;
    private ResultSet resultadoConsulta;
    
    /**
     * Registra un nuevo producto en la base de datos
     * 
     * @param producto El objeto Producto a registrar
     * @return true si el registro fue exitoso, false en caso contrario
     */
    public boolean registrarProducto(Producto producto) {
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
            JOptionPane.showMessageDialog(null, "Error al registrar producto: " + excepcion.toString());
            return false;
        } finally {
            cerrarRecursos();
        }
    }
    
    /**
     * Obtiene la lista de todos los productos
     * 
     * @return Lista de objetos Producto
     */
    public List<Producto> listarProductos() {
        List<Producto> listaProductos = new ArrayList<>();
        String consulta = "SELECT * FROM productos";
        
        try {
            conexion = conexionBD.getConexion();
            sentenciaPreparada = conexion.prepareStatement(consulta);
            resultadoConsulta = sentenciaPreparada.executeQuery();
            
            while (resultadoConsulta.next()) {
                Producto producto = new Producto();
                producto.setId(resultadoConsulta.getInt("id"));
                producto.setCodigo(resultadoConsulta.getString("codigo"));
                producto.setNombre(resultadoConsulta.getString("nombre"));
                producto.setProveedor(resultadoConsulta.getString("proveedor"));
                producto.setStock(resultadoConsulta.getInt("stock"));
                producto.setPrecio(resultadoConsulta.getDouble("precio"));
                listaProductos.add(producto);
            }
        } catch (SQLException excepcion) {
            System.out.println("Error al listar productos: " + excepcion.toString());
        } finally {
            cerrarRecursos();
        }
        
        return listaProductos;
    }
    
    /**
     * Busca un producto por su código
     * 
     * @param codigo El código del producto a buscar
     * @return Objeto Producto si se encuentra, null si no
     */
    public Producto buscarProducto(String codigo) {
        Producto producto = null;
        String consulta = "SELECT * FROM productos WHERE codigo = ?";
        
        try {
            conexion = conexionBD.getConexion();
            sentenciaPreparada = conexion.prepareStatement(consulta);
            sentenciaPreparada.setString(1, codigo);
            resultadoConsulta = sentenciaPreparada.executeQuery();
            
            if (resultadoConsulta.next()) {
                producto = new Producto();
                producto.setId(resultadoConsulta.getInt("id"));
                producto.setCodigo(resultadoConsulta.getString("codigo"));
                producto.setNombre(resultadoConsulta.getString("nombre"));
                producto.setProveedor(resultadoConsulta.getString("proveedor"));
                producto.setStock(resultadoConsulta.getInt("stock"));
                producto.setPrecio(resultadoConsulta.getDouble("precio"));
            }
        } catch (SQLException excepcion) {
            System.out.println("Error al buscar producto: " + excepcion.toString());
        } finally {
            cerrarRecursos();
        }
        
        return producto;
    }
    
    /**
     * Actualiza un producto existente
     * 
     * @param producto El objeto Producto con los datos actualizados
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizarProducto(Producto producto) {
        String actualizar = "UPDATE productos SET codigo = ?, nombre = ?, proveedor = ?, stock = ?, precio = ? WHERE id = ?";
        
        try {
            conexion = conexionBD.getConexion();
            sentenciaPreparada = conexion.prepareStatement(actualizar);
            sentenciaPreparada.setString(1, producto.getCodigo());
            sentenciaPreparada.setString(2, producto.getNombre());
            sentenciaPreparada.setString(3, producto.getProveedor());
            sentenciaPreparada.setInt(4, producto.getStock());
            sentenciaPreparada.setDouble(5, producto.getPrecio());
            sentenciaPreparada.setInt(6, producto.getId());
            sentenciaPreparada.execute();
            return true;
        } catch (SQLException excepcion) {
            System.out.println("Error al actualizar producto: " + excepcion.toString());
            return false;
        } finally {
            cerrarRecursos();
        }
    }
    
    /**
     * Elimina un producto por su ID
     * 
     * @param id El ID del producto a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean eliminarProducto(int id) {
        String eliminar = "DELETE FROM productos WHERE id = ?";
        
        try {
            conexion = conexionBD.getConexion();
            sentenciaPreparada = conexion.prepareStatement(eliminar);
            sentenciaPreparada.setInt(1, id);
            sentenciaPreparada.execute();
            return true;
        } catch (SQLException excepcion) {
            System.out.println("Error al eliminar producto: " + excepcion.toString());
            return false;
        } finally {
            cerrarRecursos();
        }
    }
    
    /**
     * Actualiza el stock de un producto
     * 
     * @param id El ID del producto
     * @param cantidad La cantidad a añadir o restar del stock actual
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizarStock(int id, int cantidad) {
        String actualizar = "UPDATE productos SET stock = stock + ? WHERE id = ?";
        
        try {
            conexion = conexionBD.getConexion();
            sentenciaPreparada = conexion.prepareStatement(actualizar);
            sentenciaPreparada.setInt(1, cantidad);
            sentenciaPreparada.setInt(2, id);
            sentenciaPreparada.execute();
            return true;
        } catch (SQLException excepcion) {
            System.out.println("Error al actualizar stock: " + excepcion.toString());
            return false;
        } finally {
            cerrarRecursos();
        }
    }
    
    /**
     * Cierra los recursos de la base de datos
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