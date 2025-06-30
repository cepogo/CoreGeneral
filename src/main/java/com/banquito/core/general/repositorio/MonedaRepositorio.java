package com.banquito.core.general.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.banquito.core.general.modelo.Moneda;

public interface MonedaRepositorio extends MongoRepository<Moneda, String> {
    Moneda findByCodigo(String codigo);
} 