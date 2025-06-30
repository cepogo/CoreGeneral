package com.banquito.core.general.dto;

import com.banquito.core.general.enums.EstadoGeneralEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EntidadBancariaDTO {
    private String id;
    private String codigoLocal;
    private String nombre;
    private String codigoInternacional;
    private EstadoGeneralEnum estado;
}
