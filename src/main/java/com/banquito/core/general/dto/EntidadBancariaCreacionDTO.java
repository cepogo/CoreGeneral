package com.banquito.core.general.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
public class EntidadBancariaCreacionDTO {
    @NotNull(message = "El código local no puede ser nulo")
    @Size(max = 6, message = "El código local debe tener máximo 6 caracteres")
    private String codigoLocal;

    @NotNull(message = "El nombre no puede ser nulo")
    @Size(max = 100, message = "El nombre debe tener máximo 100 caracteres")
    private String nombre;

    @NotNull(message = "El código internacional no puede ser nulo")
    @Size(max = 20, message = "El código internacional debe tener máximo 20 caracteres")
    private String codigoInternacional;
}
