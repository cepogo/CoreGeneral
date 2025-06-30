package com.banquito.core.general.modelo;

import com.banquito.core.general.enums.EstadoSucursalesEnum;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Document(collection = "sucursales")
public class Sucursal {

    @Id
    private String id;
    private String entidadBancariaId;
    private String locacionId;
    private String codigo;
    private String nombre;
    private LocalDate fechaCreacion;
    private String correoElectronico;
    private String telefono;
    private String direccionLinea1;
    private String direccionLinea2;
    private BigDecimal Latitud;
    private BigDecimal Longitud;
    private EstadoSucursalesEnum estado;
    private Long version;

    public Sucursal(String id) {
        this.id = id;
    }
}
