package com.banquito.core.general.repositorio;

import com.banquito.core.general.enums.EstadoLocacionesGeograficasEnum;
import com.banquito.core.general.modelo.LocacionGeografica;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocacionGeograficaRepositorio extends MongoRepository<LocacionGeografica, String> {
    List<LocacionGeografica> findByEstado(EstadoLocacionesGeograficasEnum estado);
} 