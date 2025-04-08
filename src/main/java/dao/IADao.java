package dao;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import configuracion.ConexionBD;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IADao {
    private final ConexionBD conexionBD = new ConexionBD();
    private Connection conexion;
    private PreparedStatement sentenciaPreparada;
    private ResultSet resultadoConsulta;
    
    private static final Dotenv dotenv = Dotenv.load();
    private static final String API_KEY = dotenv.get("API_KEY");
    private static final String API_URL = 
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro:generateContent";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();
    
    /**
     * Obtiene resumen de ventas para análisis de IA
     * @return Texto con resumen de las ventas
     */
    public String obtenerResumenVentas() {
        StringBuilder resumen = new StringBuilder("Resumen de ventas:\n");
        
        try {
            conexion = conexionBD.getConexion();
            // Consulta para obtener el total de ventas por día
            String consulta = "SELECT DATE(fecha) AS dia, SUM(total) AS total_ventas, COUNT(*) AS num_ventas " +
                            "FROM ventas GROUP BY DATE(fecha) ORDER BY dia DESC LIMIT 10";
            
            sentenciaPreparada = conexion.prepareStatement(consulta);
            resultadoConsulta = sentenciaPreparada.executeQuery();
            
            resumen.append("Últimos 10 días con ventas:\n");
            while (resultadoConsulta.next()) {
                String fecha = resultadoConsulta.getString("dia");
                double total = resultadoConsulta.getDouble("total_ventas");
                int cantidad = resultadoConsulta.getInt("num_ventas");
                
                resumen.append("- ").append(fecha).append(": ")
                      .append(cantidad).append(" ventas por $")
                      .append(total).append("\n");
            }
            
            // Consulta para productos más vendidos
            // Usamos la tabla detalle en lugar de detalle_venta
            consulta = "SELECT p.nombre AS descripcion, SUM(d.cantidad) AS total_vendido " +
                      "FROM detalle d JOIN productos p ON d.codigo_producto = p.id " +
                      "GROUP BY p.id ORDER BY total_vendido DESC LIMIT 5";
            
            sentenciaPreparada = conexion.prepareStatement(consulta);
            resultadoConsulta = sentenciaPreparada.executeQuery();
            
            resumen.append("\nProductos más vendidos:\n");
            while (resultadoConsulta.next()) {
                String producto = resultadoConsulta.getString("descripcion");
                int cantidad = resultadoConsulta.getInt("total_vendido");
                
                resumen.append("- ").append(producto).append(": ")
                      .append(cantidad).append(" unidades\n");
            }
            
        } catch (SQLException e) {
            resumen.append("Error al obtener datos: ").append(e.getMessage());
        } finally {
            cerrarRecursos();
        }
        
        return resumen.toString();
    }
    
    /**
     * Obtiene información de clientes para análisis
     * @return Texto con información de clientes
     */
    public String obtenerInformacionClientes() {
        StringBuilder info = new StringBuilder("Información de clientes:\n");
        
        try {
            conexion = conexionBD.getConexion();
            // Obtener total de clientes
            String consulta = "SELECT COUNT(*) AS total FROM clientes";
            sentenciaPreparada = conexion.prepareStatement(consulta);
            resultadoConsulta = sentenciaPreparada.executeQuery();
            
            if (resultadoConsulta.next()) {
                int totalClientes = resultadoConsulta.getInt("total");
                info.append("Total de clientes: ").append(totalClientes).append("\n\n");
            }
            
            // Obtener los 5 mejores clientes por compras
            consulta = "SELECT c.nombre, COUNT(v.id) AS num_compras, SUM(v.total) AS total_gastado " +
                      "FROM clientes c JOIN ventas v ON c.nombre = v.cliente " +
                      "GROUP BY c.id ORDER BY total_gastado DESC LIMIT 5";
            
            sentenciaPreparada = conexion.prepareStatement(consulta);
            resultadoConsulta = sentenciaPreparada.executeQuery();
            
            info.append("Mejores clientes por compras:\n");
            while (resultadoConsulta.next()) {
                String nombre = resultadoConsulta.getString("nombre");
                int compras = resultadoConsulta.getInt("num_compras");
                double total = resultadoConsulta.getDouble("total_gastado");
                
                info.append("- ").append(nombre).append(": ")
                    .append(compras).append(" compras, $")
                    .append(total).append(" gastados\n");
            }
            
        } catch (SQLException e) {
            info.append("Error al obtener datos: ").append(e.getMessage());
        } finally {
            cerrarRecursos();
        }
        
        return info.toString();
    }
    
    /**
     * Obtiene información de productos para análisis
     * @return Texto con información de productos
     */
    public String obtenerInformacionProductos() {
        StringBuilder info = new StringBuilder("Información de productos:\n");
        
        try {
            conexion = conexionBD.getConexion();
            // Obtener total de productos
            String consulta = "SELECT COUNT(*) AS total FROM productos";
            sentenciaPreparada = conexion.prepareStatement(consulta);
            resultadoConsulta = sentenciaPreparada.executeQuery();
            
            if (resultadoConsulta.next()) {
                int totalProductos = resultadoConsulta.getInt("total");
                info.append("Total de productos: ").append(totalProductos).append("\n\n");
            }
            
            // Obtener productos con stock bajo
            consulta = "SELECT nombre AS descripcion, stock AS cantidad FROM productos WHERE stock < 10";
            sentenciaPreparada = conexion.prepareStatement(consulta);
            resultadoConsulta = sentenciaPreparada.executeQuery();
            
            info.append("Productos con stock bajo (menos de 10 unidades):\n");
            while (resultadoConsulta.next()) {
                String descripcion = resultadoConsulta.getString("descripcion");
                int cantidad = resultadoConsulta.getInt("cantidad");
                
                info.append("- ").append(descripcion).append(": ")
                    .append(cantidad).append(" unidades\n");
            }
            
        } catch (SQLException e) {
            info.append("Error al obtener datos: ").append(e.getMessage());
        } finally {
            cerrarRecursos();
        }
        
        return info.toString();
    }
    
    /**
     * Método alternativo para consultas simples sin mucho análisis
     * @return Texto con información básica del sistema
     */
    public String obtenerInformacionBasica() {
        StringBuilder info = new StringBuilder("Información básica del sistema:\n\n");
        
        try {
            conexion = conexionBD.getConexion();
            
            // Número de clientes
            String consulta = "SELECT COUNT(*) AS total FROM clientes";
            sentenciaPreparada = conexion.prepareStatement(consulta);
            resultadoConsulta = sentenciaPreparada.executeQuery();
            
            if (resultadoConsulta.next()) {
                int totalClientes = resultadoConsulta.getInt("total");
                info.append("Total de clientes registrados: ").append(totalClientes).append("\n");
            }
            
            // Último cliente registrado
            consulta = "SELECT nombre, fecha FROM clientes ORDER BY fecha DESC LIMIT 1";
            sentenciaPreparada = conexion.prepareStatement(consulta);
            resultadoConsulta = sentenciaPreparada.executeQuery();
            
            if (resultadoConsulta.next()) {
                String nombre = resultadoConsulta.getString("nombre");
                String fecha = resultadoConsulta.getString("fecha");
                info.append("Último cliente registrado: ").append(nombre)
                    .append(" (").append(fecha).append(")\n\n");
            }
            
            // Número de productos
            consulta = "SELECT COUNT(*) AS total FROM productos";
            sentenciaPreparada = conexion.prepareStatement(consulta);
            resultadoConsulta = sentenciaPreparada.executeQuery();
            
            if (resultadoConsulta.next()) {
                int totalProductos = resultadoConsulta.getInt("total");
                info.append("Total de productos en inventario: ").append(totalProductos).append("\n\n");
            }
            
            // Número de proveedores
            consulta = "SELECT COUNT(*) AS total FROM proveedor";
            sentenciaPreparada = conexion.prepareStatement(consulta);
            resultadoConsulta = sentenciaPreparada.executeQuery();
            
            if (resultadoConsulta.next()) {
                int totalProveedores = resultadoConsulta.getInt("total");
                info.append("Total de proveedores: ").append(totalProveedores).append("\n\n");
            }
            
            // Número de usuarios
            consulta = "SELECT COUNT(*) AS total FROM usuarios";
            sentenciaPreparada = conexion.prepareStatement(consulta);
            resultadoConsulta = sentenciaPreparada.executeQuery();
            
            if (resultadoConsulta.next()) {
                int totalUsuarios = resultadoConsulta.getInt("total");
                info.append("Total de usuarios del sistema: ").append(totalUsuarios).append("\n");
            }
            
        } catch (SQLException e) {
            info.append("Error al obtener datos: ").append(e.getMessage());
        } finally {
            cerrarRecursos();
        }
        
        return info.toString();
    }
    
    /**
     * Analiza los datos del sistema con IA
     * @param tipoAnalisis El tipo de análisis a realizar (ventas, clientes, productos, general)
     * @return Análisis generado por IA
     */
    public String analizarDatosConIA(String tipoAnalisis) {
        String datos;
        String prompt;
        
        switch (tipoAnalisis.toLowerCase()) {
            case "ventas":
                datos = obtenerResumenVentas();
                prompt = "Analiza los siguientes datos de ventas y proporciona conclusiones útiles, " +
                         "tendencias y recomendaciones de negocio:\n\n" + datos;
                break;
            case "clientes":
                datos = obtenerInformacionClientes();
                prompt = "Analiza los siguientes datos de clientes y proporciona insights sobre " +
                         "patrones de compra y recomendaciones para mejorar la retención y fidelización:\n\n" + datos;
                break;
            case "productos":
                datos = obtenerInformacionProductos();
                prompt = "Analiza los siguientes datos de productos y proporciona recomendaciones " +
                         "sobre gestión de inventario y oportunidades para mejorar el catálogo:\n\n" + datos;
                break;
            case "básico":
                datos = obtenerInformacionBasica();
                prompt = "Basándote en la siguiente información básica del sistema, proporciona algunas " +
                         "observaciones generales y recomendaciones iniciales para el negocio:\n\n" + datos;
                break;
            case "general":
            default:
                // En caso de que la base de datos esté en configuración inicial, usar info básica
                datos = obtenerInformacionBasica();
                prompt = "Basándote en la siguiente información del sistema de ventas, proporciona un análisis " +
                         "general del negocio con recomendaciones para mejorar las operaciones:\n\n" + datos;
                break;
        }
        
        return generarContenido(prompt);
    }
    
    /**
     * Genera recomendaciones de productos para un cliente
     * @param idCliente ID del cliente
     * @return Recomendaciones generadas por IA
     */
    public String generarRecomendacionesCliente(int idCliente) {
        StringBuilder datosCliente = new StringBuilder();
        
        try {
            conexion = conexionBD.getConexion();
            
            // Obtener información del cliente
            String consulta = "SELECT nombre FROM clientes WHERE id = ?";
            sentenciaPreparada = conexion.prepareStatement(consulta);
            sentenciaPreparada.setInt(1, idCliente);
            resultadoConsulta = sentenciaPreparada.executeQuery();
            
            if (resultadoConsulta.next()) {
                String nombreCliente = resultadoConsulta.getString("nombre");
                datosCliente.append("Cliente: ").append(nombreCliente).append("\n\n");
            } else {
                return "No se encontró el cliente con ID: " + idCliente;
            }
            
        } catch (SQLException e) {
            return "Error al obtener datos del cliente: " + e.getMessage();
        } finally {
            cerrarRecursos();
        }
        
        // Lista de productos disponibles
        try {
            conexion = conexionBD.getConexion();
            String consulta = "SELECT nombre, precio FROM productos LIMIT 10";
            sentenciaPreparada = conexion.prepareStatement(consulta);
            resultadoConsulta = sentenciaPreparada.executeQuery();
            
            datosCliente.append("Productos disponibles:\n");
            while (resultadoConsulta.next()) {
                String nombre = resultadoConsulta.getString("nombre");
                double precio = resultadoConsulta.getDouble("precio");
                
                datosCliente.append("- ").append(nombre)
                          .append(" ($").append(precio).append(")\n");
            }
            
        } catch (SQLException e) {
            return "Error al obtener productos: " + e.getMessage();
        } finally {
            cerrarRecursos();
        }
        
        String prompt = "Actúa como un asesor de ventas y genera recomendaciones personalizadas de productos " +
                      "para el siguiente cliente, basándote en los productos disponibles:\n\n" + 
                      datosCliente.toString();
        
        return generarContenido(prompt);
    }
    
    /**
     * Envía un prompt a la API de Gemini y retorna la respuesta
     * @param prompt El mensaje o instrucción para Gemini
     * @return Texto de respuesta generado por Gemini
     */
    private String generarContenido(String prompt) {
        JsonObject requestBody = new JsonObject();
        JsonObject content = new JsonObject();
        JsonObject part = new JsonObject();
        
        part.addProperty("text", prompt);
        content.add("parts", gson.toJsonTree(new JsonObject[]{part}));
        requestBody.add("contents", gson.toJsonTree(new JsonObject[]{content}));
        
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("x-goog-api-key", API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                HttpResponse.BodyHandlers.ofString());
            
            JSONObject jsonObj = new JSONObject(response.body());
            return jsonObj.getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text");
        } catch (IOException | InterruptedException e) {
            System.out.println("Error al generar contenido: " + e.getMessage());
            return "Error al generar contenido: " + e.getMessage();
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
        } catch (SQLException e) {
            System.err.println("Error al cerrar recursos: " + e.getMessage());
        }
    }
}