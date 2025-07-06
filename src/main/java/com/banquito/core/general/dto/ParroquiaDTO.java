package com.banquito.core.general.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParroquiaDTO {
    @NotNull(message = "El código de locación no puede ser nulo")
    @Size(min = 1, max = 10, message = "El código de locación debe tener entre 1 y 10 caracteres")
    private String codigoLocacion;

    @NotNull(message = "El código de país no puede ser nulo")
    @Size(min = 2, max = 2, message = "El código de país debe tener 2 caracteres")
    private String codigoPais;

    @NotNull(message = "El código de provincia no puede ser nulo")
    @Size(min = 1, max = 4, message = "El código de provincia debe tener entre 1 y 4 caracteres")
    private String codigoProvincia;

    @NotNull(message = "El nombre de la provincia no puede ser nulo")
    @Size(min = 1, max = 100, message = "El nombre de la provincia debe tener entre 1 y 100 caracteres")
    private String provincia;

    @NotNull(message = "El código de cantón no puede ser nulo")
    @Size(min = 1, max = 4, message = "El código de cantón debe tener entre 1 y 4 caracteres")
    private String codigoCanton;

    @NotNull(message = "El nombre del cantón no puede ser nulo")
    @Size(min = 1, max = 100, message = "El nombre del cantón debe tener entre 1 y 100 caracteres")
    private String canton;

    @NotNull(message = "El código de parroquia no puede ser nulo")
    @Size(min = 1, max = 4, message = "El código de parroquia debe tener entre 1 y 4 caracteres")
    private String codigoParroquia;

    @NotNull(message = "El nombre de la parroquia no puede ser nulo")
    @Size(min = 1, max = 100, message = "El nombre de la parroquia debe tener entre 1 y 100 caracteres")
    private String parroquia;
} 