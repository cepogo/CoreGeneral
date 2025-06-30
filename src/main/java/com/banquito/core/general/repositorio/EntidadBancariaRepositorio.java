package com.banquito.core.general.repositorio;

import com.banquito.core.general.enums.EstadoGeneralEnum;
import com.banquito.core.general.modelo.EntidadBancaria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntidadBancariaRepositorio extends MongoRepository<EntidadBancaria, String> {
    Optional<EntidadBancaria> findFirstByEstado(EstadoGeneralEnum estado);
    Optional<EntidadBancaria> findByCodigoLocal(String codigoLocal);


}
