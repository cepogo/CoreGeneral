package com.banquito.core.general.dto;

import lombok.Data;

@Data
public class MonedaDTO {
    private String id;
    private String codigo;
    private String nombre;
    private String simbolo;
    private PaisDTO pais;
} 