package com.banquito.core.general.modelo;

import com.banquito.core.general.enums.EstadoGeneralEnum;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Document(collection = "entidades_bancarias")
public class EntidadBancaria {
    @Id
    private String id;
    private String codigoLocal;
    private String nombre;
    private String codigoInternacional;
    private EstadoGeneralEnum estado;
    private Long version;

    public EntidadBancaria(String id) {
        this.id = id;
    }
}
