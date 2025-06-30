package com.banquito.core.general.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SucursalUpdateDTO {
    @Size(max = 30, message = "El nombre debe tener máximo 30 caracteres")
    private String nombre;

    @Size(max = 40, message = "El correo electrónico debe tener máximo 40 caracteres")
    private String correoElectronico;

    @Size(max = 10, message = "El teléfono debe tener máximo 10 caracteres")
    private String telefono;

    @Size(max = 150, message = "La dirección línea 1 debe tener máximo 150 caracteres")
    private String direccionLinea1;

    @Size(max = 150, message = "La dirección línea 2 debe tener máximo 150 caracteres")
    private String direccionLinea2;

    private BigDecimal latitud;
    private BigDecimal longitud;
}
