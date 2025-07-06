package com.banquito.core.general.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MonedaDTO {
    private String codigoMoneda;
    private String nombre;
    private String simbolo;
} 