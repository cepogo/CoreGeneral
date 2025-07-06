package com.banquito.core.general.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "locacion_geografica")
@CompoundIndex(name = "idxLocacionGeografica_provinciacanton", def = "{'codigoProvincia': 1, 'codigoCanton': 1}")
public class LocacionGeografica {

    @Id
    private String id;

    @Indexed(name = "idxuLocacionGeografica_codigoLocacion", unique = true)
    private String codigoLocacion;

    private String codigoPais;

    @Indexed
    private String codigoProvincia;
    private String provincia;
    private String codigoCanton;
    private String canton;
    private String codigoParroquia;
    private String parroquia;
    private String estado;
    private Long version;

    public LocacionGeografica(String id) {
        this.id = id;
    }

}
