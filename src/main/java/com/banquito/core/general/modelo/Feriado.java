package com.banquito.core.general.modelo;

import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.banquito.core.general.enums.EstadoGeneralEnum;
import com.banquito.core.general.enums.TipoFeriadosEnum;

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
@Document(collection = "feriados")
public class Feriado {

    @Id
    private String id;
    private LocalDate fecha;
    private String nombre;
    private TipoFeriadosEnum tipo;
    private EstadoGeneralEnum estado;
    private LocacionGeografica locacion;
    private Long version;

} 