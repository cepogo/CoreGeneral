package com.banquito.core.general.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.banquito.core.general.dto.PaisDTO;

import org.springframework.data.mongodb.core.index.Indexed;

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
@Document(collection = "monedas")

public class Moneda {
    
    @Id
    private String id;

    @Indexed(unique = true)
    private String codigo;
    private String nombre;
    private String simbolo;

    @Indexed
    private PaisDTO pais;

    private String estado;
    private Long version;

    public Moneda(String id) {
        this.id = id;
    }
}
