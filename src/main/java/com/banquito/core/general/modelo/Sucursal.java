package com.banquito.core.general.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.banquito.core.general.dto.LocacionGeograficaDTO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Document(collection = "sucursales")
public class Sucursal {

    @Id
    private String id;
    private String codigoEntidadBancaria;

    @Indexed(unique = true)
    private String codigoSucursal;

    private String nombre;
    private LocacionGeograficaDTO locacionGeografica;

    private LocalDate fechaCreacion;
    private String correoElectronico;
    private String telefono;
    private String direccionLinea1;
    private String direccionLinea2;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private String estado;
    private Long version;

    public Sucursal(String id) {
        this.id = id;
    }
}