package com.banquito.core.general.modelo;

import java.time.LocalDate;

import com.banquito.core.general.dto.LocacionGeograficaDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


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
    private String tipo;
    private String estado;
    private LocacionGeograficaDTO locacion;
    private Long version;

} 