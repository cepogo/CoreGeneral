package com.banquito.core.general.repositorio;

import com.banquito.core.general.modelo.Sucursal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SucursalRepositorio extends MongoRepository<Sucursal, String> {
    List<Sucursal> findByCodigoEntidadBancaria(String codigoEntidadBancaria);
    List<Sucursal> findByLocacionGeografica_CodigoLocacionAndEstado(String codigoLocacion, String estado);
    Optional<Sucursal> findByCodigoSucursal(String codigoSucursal);
}
