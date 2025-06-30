package com.banquito.core.general.repositorio;

import com.banquito.core.general.modelo.Feriado;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeriadoRepositorio extends MongoRepository<Feriado, String> {

} 