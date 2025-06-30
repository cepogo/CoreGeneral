package com.banquito.core.general.dto;

import com.banquito.core.general.enums.EstadoGeneralEnum;
import com.banquito.core.general.enums.TipoFeriadosEnum;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeriadoDTO {
    private String id;

    @NotNull(message = "La fecha no puede ser nula")
    @FutureOrPresent(message = "La fecha debe ser actual o futura")
    private LocalDate fecha;

    @NotNull(message = "El nombre no puede ser nulo")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotNull(message = "El tipo no puede ser nulo")
    private TipoFeriadosEnum tipo;

    @NotNull(message = "El estado no puede ser nulo")
    private EstadoGeneralEnum estado;

    private LocacionGeograficaDTO locacion;

    private Long version;
} 