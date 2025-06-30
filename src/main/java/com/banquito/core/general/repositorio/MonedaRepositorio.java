package com.banquito.core.general.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.banquito.core.general.modelo.Moneda;
import java.util.List;
 
public interface MonedaRepositorio extends MongoRepository<Moneda, String> {
    Moneda findByCodigo(String codigo);
    List<Moneda> findByEstado(String estado);
} 