package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    private int id;
    private int dni;
    private String nombre;
    private int telefono;
    private String direccion;
    private String razonSocial;

}
