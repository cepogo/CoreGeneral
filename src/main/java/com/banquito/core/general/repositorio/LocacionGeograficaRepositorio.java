package com.banquito.core.general.repositorio;

import com.banquito.core.general.modelo.LocacionGeografica;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocacionGeograficaRepositorio extends MongoRepository<LocacionGeografica, String> {
    List<LocacionGeografica> findByEstado(String estado);
    Optional<LocacionGeografica> findByCodigoLocacion(String codigoLocacion);
} 