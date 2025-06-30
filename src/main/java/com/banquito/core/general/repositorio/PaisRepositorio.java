package com.banquito.core.general.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.banquito.core.general.modelo.Pais;
import java.util.List;
 
public interface PaisRepositorio extends MongoRepository<Pais, String> {
    Pais findByCodigo(String codigo);
    List<Pais> findByEstado(String estado);
} 