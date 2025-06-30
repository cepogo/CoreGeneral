package com.banquito.core.general.modelo;


import lombok.Data;

@Data
public class LocacionGeograficaDTO {
    
    private String codigoLocacion;
    private String codigoProvincia;
    private String provincia;
    private String codigoCanton;
    private String canton;
    private String codigoParroquia;
    private String parroquia;

}