package com.banquito.core.general.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.banquito.core.general.dto.MonedaDTO;

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
@Document(collection = "paises")

public class Pais {

    @Id
    private String id;

    @Indexed(unique = true)
    private String codigoPais;

    private String nombre;
    private String codigoTelefono;
    private MonedaDTO moneda;
    private String estado;
    private Long version;

    public Pais(String id) {
        this.id = id;
    }
}