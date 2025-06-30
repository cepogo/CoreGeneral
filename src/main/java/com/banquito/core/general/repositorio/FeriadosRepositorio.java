package com.banquito.core.general.repositorio;

import com.banquito.core.general.enums.EstadoGeneralEnum;
import com.banquito.core.general.enums.TipoFeriadosEnum;
import com.banquito.core.general.modelo.Feriado;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FeriadosRepositorio extends MongoRepository<Feriado, String> {

    List<Feriado> findByEstadoAndFechaBetween(EstadoGeneralEnum estado, LocalDate fechaInicio, LocalDate fechaFin);

    List<Feriado> findByEstadoAndTipoAndFechaBetween(EstadoGeneralEnum estado, TipoFeriadosEnum tipo, LocalDate fechaInicio, LocalDate fechaFin);
    
} 