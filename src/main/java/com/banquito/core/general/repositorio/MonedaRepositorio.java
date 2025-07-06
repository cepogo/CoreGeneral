package com.banquito.core.general.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.banquito.core.general.modelo.Moneda;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonedaRepositorio extends MongoRepository<Moneda, String> {
    Moneda findByCodigoMoneda(String codigoMoneda);
    List<Moneda> findByEstado(String estado);
}