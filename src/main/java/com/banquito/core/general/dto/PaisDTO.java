package com.banquito.core.general.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaisDTO {
    private String codigoPais;
    private String nombre;
    private String codigoTelefono;
    private String codigoMoneda;
} 