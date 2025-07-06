package com.banquito.core.general.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.banquito.core.general.modelo.Pais;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaisRepositorio extends MongoRepository<Pais, String> {
    Pais findByCodigoPais(String codigoPais);
    List<Pais> findByEstado(String estado);
} 