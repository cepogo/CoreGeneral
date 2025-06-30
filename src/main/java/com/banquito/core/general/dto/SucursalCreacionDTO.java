package com.banquito.core.general.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SucursalCreacionDTO {

    @NotNull(message = "El ID de la locación no puede ser nulo")
    private String locacionId;

    @NotNull(message = "El código no puede ser nulo")
    @Size(max = 10, message = "El código debe tener máximo 10 caracteres")
    private String codigo;

    @NotNull(message = "El nombre no puede ser nulo")
    @Size(max = 30, message = "El nombre debe tener máximo 30 caracteres")
    private String nombre;

    @NotNull(message = "El correo electrónico no puede ser nulo")
    @Size(max = 40, message = "El correo electrónico debe tener máximo 40 caracteres")
    private String correoElectronico;

    @NotNull(message = "El teléfono no puede ser nulo")
    @Size(max = 10, message = "El teléfono debe tener máximo 10 caracteres")
    private String telefono;

    @NotNull(message = "La dirección línea 1 no puede ser nula")
    @Size(max = 150, message = "La dirección línea 1 debe tener máximo 150 caracteres")
    private String direccionLinea1;

    @Size(max = 150, message = "La dirección línea 2 debe tener máximo 150 caracteres")
    private String direccionLinea2;

    @NotNull(message = "La latitud no puede ser nula")
    private BigDecimal latitud;

    @NotNull(message = "La longitud no puede ser nula")
    private BigDecimal longitud;
}
