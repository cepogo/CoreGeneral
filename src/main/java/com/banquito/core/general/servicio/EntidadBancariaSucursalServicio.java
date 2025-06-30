package com.banquito.core.general.servicio;

import com.banquito.core.general.dto.*;
import com.banquito.core.general.enums.EstadoGeneralEnum;
import com.banquito.core.general.enums.EstadoLocacionesGeograficasEnum;
import com.banquito.core.general.enums.EstadoSucursalesEnum;
import com.banquito.core.general.excepcion.ActualizarEntidadException;
import com.banquito.core.general.excepcion.CrearEntidadException;
import com.banquito.core.general.excepcion.EntidadNoEncontradaException;
import com.banquito.core.general.mapper.EntidadBancariaMapper;
import com.banquito.core.general.mapper.SucursalMapper;
import com.banquito.core.general.modelo.EntidadBancaria;
import com.banquito.core.general.modelo.LocacionGeografica;
import com.banquito.core.general.modelo.Sucursal;
import com.banquito.core.general.repositorio.EntidadBancariaRepositorio;
import com.banquito.core.general.repositorio.LocacionGeograficaRepositorio;
import com.banquito.core.general.repositorio.SucursalRepositorio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class EntidadBancariaSucursalServicio {

    private final EntidadBancariaRepositorio entidadBancariaRepositorio;
    private final SucursalRepositorio sucursalRepositorio;
    private final LocacionGeograficaRepositorio locacionGeograficaRepositorio;

    private final EntidadBancariaMapper entidadBancariaMapper;
    private final SucursalMapper sucursalMapper;

    public EntidadBancariaSucursalServicio(EntidadBancariaRepositorio entidadBancariaRepositorio, SucursalRepositorio sucursalRepositorio, LocacionGeograficaRepositorio locacionRepositorio, EntidadBancariaMapper entidadBancariaMapper, SucursalMapper sucursalMapper) {
        this.entidadBancariaRepositorio = entidadBancariaRepositorio;
        this.sucursalRepositorio = sucursalRepositorio;
        this.locacionGeograficaRepositorio = locacionRepositorio;
        this.entidadBancariaMapper = entidadBancariaMapper;
        this.sucursalMapper = sucursalMapper;
    }

    // ================= ENTIDAD BANCARIA =================

    @Transactional
    public EntidadBancaria crearEntidad(EntidadBancariaCreacionDTO dto) {
        EntidadBancaria entidad = entidadBancariaMapper.toEntity(dto);
        entidad.setEstado(EstadoGeneralEnum.ACTIVO);
        entidad.setVersion(1L);
        return entidadBancariaRepositorio.save(entidad);
    }

    public EntidadBancariaDTO obtenerEntidadPorId(String id) {
        log.info("Obteniendo entidad bancaria con ID: {}", id);
        EntidadBancaria entidad = entidadBancariaRepositorio.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la entidad bancaria con ID: " + id, 2, "EntidadBancaria"));
        return entidadBancariaMapper.toDTO(entidad);
    }

    public EntidadBancariaDTO obtenerPrimeraEntidadBancariaActiva() {
        log.info("Obteniendo la primera entidad bancaria activa");
        EntidadBancaria entidad = entidadBancariaRepositorio.findFirstByEstado(EstadoGeneralEnum.ACTIVO)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontraron entidades bancarias activas.", 2, "EntidadBancaria"));
        return entidadBancariaMapper.toDTO(entidad);
    }

    @Transactional
    public EntidadBancariaDTO cambiarEstadoEntidadBancaria(String id, EstadoGeneralEnum nuevoEstado) {
        log.info("Cambiando estado de la entidad bancaria con ID: {} a {}", id, nuevoEstado);
        EntidadBancaria entidad = entidadBancariaRepositorio.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la entidad bancaria con ID: " + id, 2, "EntidadBancaria"));
        entidad.setEstado(nuevoEstado);
        entidad.setVersion(entidad.getVersion() + 1);

        if (nuevoEstado == EstadoGeneralEnum.INACTIVO) {
            List<Sucursal> sucursalesAsociadas = sucursalRepositorio.findByEntidadBancariaId(entidad.getId());
            if (!sucursalesAsociadas.isEmpty()) {
                for (Sucursal sucursal : sucursalesAsociadas) {
                    sucursal.setEstado(EstadoSucursalesEnum.INACTIVO);
                    sucursal.setVersion(sucursal.getVersion() + 1);
                    sucursalRepositorio.save(sucursal);
                }
                log.info("Todas las sucursales asociadas a la entidad bancaria con ID: {} han sido desactivadas", id);
            }
        }
        log.info("Estado de la entidad bancaria actualizado a {} para la entidad con ID: {}", nuevoEstado, id);
        return entidadBancariaMapper.toDTO(entidadBancariaRepositorio.save(entidad));
    }

    // ================= SUCURSAL =================

    public Sucursal obtenerSucursalPorId(String id) {
        return sucursalRepositorio.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Sucursal no encontrada con ID: " + id, 2, "Sucursal"));
    }

    @Transactional
    public Sucursal crearSucursal(String entidadId, SucursalCreacionDTO dto) {
        log.info("Iniciando creación de sucursal con código: {}", dto.getCodigo());
        try {
            obtenerEntidadPorId(entidadId);
            locacionGeograficaRepositorio.findById(dto.getLocacionId())
                    .orElseThrow(() -> new EntidadNoEncontradaException("Locación no encontrada con ID: " + dto.getLocacionId(), 1, "Locacion"));

            Sucursal sucursal = sucursalMapper.toEntity(dto);
            sucursal.setEntidadBancariaId(entidadId);
            sucursal.setFechaCreacion(LocalDate.now());
            sucursal.setEstado(EstadoSucursalesEnum.ACTIVO);
            sucursal.setVersion(1L);
            Sucursal savedEntity = sucursalRepositorio.save(sucursal);
            log.info("Sucursal con código {} creada exitosamente.", savedEntity.getCodigo());
            return savedEntity;
        } catch (Exception e) {
            log.error("Error al crear sucursal: {}", e.getMessage(), e);
            throw new CrearEntidadException("Sucursal", "Error al crear la sucursal: " + e.getMessage());
        }
    }

    @Transactional
    public Sucursal modificarSucursal(String id, SucursalUpdateDTO dto) {
        log.info("Iniciando modificación de sucursal con ID: {}", id);
        Sucursal sucursal = obtenerSucursalPorId(id);
        try {
            sucursalMapper.updateFromDTO(dto, sucursal);
            sucursal.setVersion(sucursal.getVersion() + 1);
            Sucursal savedEntity = sucursalRepositorio.save(sucursal);
            log.info("Sucursal con ID {} modificada exitosamente.", savedEntity.getId());
            return savedEntity;
        } catch (Exception e) {
            log.error("Error al modificar sucursal con ID {}: {}", id, e.getMessage(), e);
            throw new ActualizarEntidadException("Sucursal", "Error al modificar la sucursal: " + e.getMessage());
        }
    }

    @Transactional
    public void cambiarEstadoSucursal(String id, EstadoSucursalesEnum nuevoEstado) {
        log.info("Cambiando estado de sucursal {} a {}", id, nuevoEstado.name());
        Sucursal sucursal = obtenerSucursalPorId(id);
        try {
            if (nuevoEstado == EstadoSucursalesEnum.ACTIVO) {
                EntidadBancariaDTO entidad = obtenerEntidadPorId(sucursal.getEntidadBancariaId());
                if (entidad.getEstado() != EstadoGeneralEnum.ACTIVO) {
                    throw new ActualizarEntidadException("Sucursal", "No se puede activar la sucursal porque la entidad bancaria está inactiva.");
                }
                LocacionGeografica locacion = locacionGeograficaRepositorio.findById(sucursal.getLocacionId())
                        .orElseThrow(() -> new EntidadNoEncontradaException("Locación no encontrada con ID: " + sucursal.getLocacionId(), 1, "Locacion"));
                if (locacion.getEstado() != EstadoLocacionesGeograficasEnum.ACTIVO) {
                    throw new ActualizarEntidadException("Sucursal", "No se puede activar la sucursal porque la locación geográfica está inactiva.");
                }
            }
            sucursal.setEstado(nuevoEstado);
            sucursal.setVersion(sucursal.getVersion() + 1);
            sucursalRepositorio.save(sucursal);
            log.info("Estado de sucursal {} cambiado a {}.", id, nuevoEstado.name());
        } catch (RuntimeException e) {
            log.error("Error al cambiar estado de sucursal {}: {}", id, e.getMessage(), e);
            throw new ActualizarEntidadException("Sucursal", "Error al cambiar el estado de la sucursal: " + e.getMessage());
        }
    }

    public List<Sucursal> listarSucursalesActivasPorLocacion(String locacionId) {
        log.info("Listando sucursales activas para locación ID: {}", locacionId);
        List<Sucursal> sucursales = sucursalRepositorio.findByLocacionIdAndEstado(locacionId, EstadoSucursalesEnum.ACTIVO);
        log.info("Se encontraron {} sucursales activas para la locación ID: {}", sucursales.size(), locacionId);
        return sucursales;
    }




}
