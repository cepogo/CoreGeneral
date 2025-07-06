package com.banquito.core.general.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EntidadBancariaDTO {
    private String codigoLocal;
    private String nombre;
    private String codigoInternacional;
    private List<MonedaDTO> monedas;
    private String estado;
}
