package com.banquito.core.general.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.banquito.core.general.enums.EstadoLocacionesGeograficasEnum;

import org.springframework.data.mongodb.core.index.CompoundIndex;
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
@Document(collection = "locaciones_geograficas")
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
    private EstadoLocacionesGeograficasEnum estado;
    private Long version;

    public LocacionGeografica(String id) {
        this.id = id;
    }

}
