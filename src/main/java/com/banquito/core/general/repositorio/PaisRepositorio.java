package com.banquito.core.general.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.banquito.core.general.modelo.Pais;
 
public interface PaisRepositorio extends MongoRepository<Pais, String> {
    Pais findByCodigo(String codigo);
} 