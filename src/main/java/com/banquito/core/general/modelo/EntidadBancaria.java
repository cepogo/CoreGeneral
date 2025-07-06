package com.banquito.core.general.modelo;

import com.banquito.core.general.dto.MonedaDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

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
    private List<MonedaDTO> monedas;
    private String estado;
    private Long version;

    public EntidadBancaria(String id) {
        this.id = id;
    }
}
