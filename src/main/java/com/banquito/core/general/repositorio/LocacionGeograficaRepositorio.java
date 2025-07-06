package com.banquito.core.general.repositorio;

import com.banquito.core.general.modelo.LocacionGeografica;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocacionGeograficaRepositorio extends MongoRepository<LocacionGeografica, String> {
    Optional<LocacionGeografica> findByCodigoLocacion(String codigoLocacion);
    List<LocacionGeografica> findByEstado(String estado);
    
    // Métodos para filtrar por niveles geográficos
    @Aggregation(pipeline = {
        "{'$match': {'estado': ?0}}",
        "{'$group': {'_id': '$codigoProvincia', 'codigoLocacion': {'$first': '$codigoProvincia'}, 'codigoPais': {'$first': '$codigoPais'}, 'codigoProvincia': {'$first': '$codigoProvincia'}, 'provincia': {'$first': '$provincia'}, 'estado': {'$first': '$estado'}, 'version': {'$first': '$version'}}}",
        "{'$sort': {'codigoProvincia': 1}}"
    })
    List<LocacionGeografica> findByEstadoAndCodigoCantonIsNullAndCodigoParroquiaIsNull(String estado);
    
    @Aggregation(pipeline = {
        "{'$match': {'estado': ?0, 'codigoProvincia': ?1}}",
        "{'$group': {'_id': {'provincia': '$codigoProvincia', 'canton': '$codigoCanton'}, 'codigoLocacion': {'$first': {'$concat': ['$codigoProvincia', '$codigoCanton']}}, 'codigoPais': {'$first': '$codigoPais'}, 'codigoProvincia': {'$first': '$codigoProvincia'}, 'provincia': {'$first': '$provincia'}, 'codigoCanton': {'$first': '$codigoCanton'}, 'canton': {'$first': '$canton'}, 'estado': {'$first': '$estado'}, 'version': {'$first': '$version'}}}",
        "{'$sort': {'codigoCanton': 1}}"
    })
    List<LocacionGeografica> findByEstadoAndCodigoProvinciaAndCodigoParroquiaIsNull(String estado, String codigoProvincia);
    
    @Aggregation(pipeline = {
        "{'$match': {'estado': ?0, 'codigoProvincia': ?1, 'codigoCanton': ?2}}",
        "{'$sort': {'codigoParroquia': 1}}"
    })
    List<LocacionGeografica> findByEstadoAndCodigoProvinciaAndCodigoCanton(String estado, String codigoProvincia, String codigoCanton);

    List<LocacionGeografica> findByEstadoAndCodigoProvincia(String estado, String codigoProvincia);

    List<LocacionGeografica> findByCodigoProvinciaAndEstado(String codigoProvincia, String estado);
    List<LocacionGeografica> findByCodigoProvinciaAndCodigoCantonAndEstado(String codigoProvincia, String codigoCanton, String estado);
    List<LocacionGeografica> findByCodigoProvinciaAndCodigoCantonAndCodigoParroquiaAndEstado(String codigoProvincia, String codigoCanton, String codigoParroquia, String estado);
} 