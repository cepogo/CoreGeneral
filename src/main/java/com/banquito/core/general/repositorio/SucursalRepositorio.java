package com.banquito.core.general.repositorio;

import com.banquito.core.general.enums.EstadoSucursalesEnum;
import com.banquito.core.general.modelo.EntidadBancaria;
import com.banquito.core.general.modelo.Sucursal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SucursalRepositorio extends MongoRepository<Sucursal, String> {
    List<Sucursal> findByEntidadBancaria(EntidadBancaria entidadBancaria);
    List<Sucursal> findByLocacionIdAndEstado(String locacionId, EstadoSucursalesEnum estado);

}
