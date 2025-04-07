/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author kevinrengifo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Login {

    private int id;
    private String nombre;
    private String correo;
    private String clave;

}
