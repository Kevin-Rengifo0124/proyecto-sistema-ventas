/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author kevinrengifo
 */
public class TestConexion {
     public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3307/sistemaventa";
        String user = "root";
        String password = "";

        try {
            Connection conexion = DriverManager.getConnection(url, user, password);
            if (conexion != null) {
                System.out.println("¡Conexión exitosa a la base de datos!");
            }
        } catch (Exception e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
    }
}
