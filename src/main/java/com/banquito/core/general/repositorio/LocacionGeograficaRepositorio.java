package com.banquito.core.general.repositorio;

import com.banquito.core.general.modelo.LocacionGeografica;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocacionGeograficaRepositorio extends MongoRepository<LocacionGeografica, String> {

} 