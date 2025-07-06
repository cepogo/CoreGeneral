package com.banquito.core.general.repositorio;

import com.banquito.core.general.modelo.EntidadBancaria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntidadBancariaRepositorio extends MongoRepository<EntidadBancaria, String> {
    Optional<EntidadBancaria> findFirstByEstado(String estado);
    Optional<EntidadBancaria> findByCodigoLocal(String codigoLocal);


}
