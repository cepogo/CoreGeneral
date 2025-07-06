package com.banquito.core.general.modelo;

import java.time.LocalDate;

import com.banquito.core.general.dto.LocacionGeograficaDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "feriados")
public class Feriado {

    @Id
    private String id;
    
    @Indexed(unique = true)
    private String codigoFeriado;
    
    private String nombre;
    private String descripcion;
    private LocalDate fecha;
    private String tipo;
    private LocacionGeograficaDTO locacion;
    private String estado;
    private Long version;

} 