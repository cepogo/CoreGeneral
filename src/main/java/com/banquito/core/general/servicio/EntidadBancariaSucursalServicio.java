package com.banquito.core.general.servicio;

import com.banquito.core.general.dto.*;
import com.banquito.core.general.excepcion.ActualizarEntidadException;
import com.banquito.core.general.excepcion.CrearEntidadException;
import com.banquito.core.general.excepcion.EntidadNoEncontradaException;
import com.banquito.core.general.mapper.EntidadBancariaMapper;
import com.banquito.core.general.mapper.LocacionGeograficaMapper;
import com.banquito.core.general.mapper.MonedaMapper;
import com.banquito.core.general.mapper.SucursalMapper;
import com.banquito.core.general.modelo.EntidadBancaria;
import com.banquito.core.general.modelo.LocacionGeografica;
import com.banquito.core.general.modelo.Moneda;
import com.banquito.core.general.modelo.Sucursal;
import com.banquito.core.general.repositorio.EntidadBancariaRepositorio;
import com.banquito.core.general.repositorio.LocacionGeograficaRepositorio;
import com.banquito.core.general.repositorio.MonedaRepositorio;
import com.banquito.core.general.repositorio.SucursalRepositorio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EntidadBancariaSucursalServicio {

    private final EntidadBancariaRepositorio entidadBancariaRepositorio;
    private final SucursalRepositorio sucursalRepositorio;
    private final LocacionGeograficaRepositorio locacionGeograficaRepositorio;
    private final MonedaRepositorio monedaRepositorio;

    private final EntidadBancariaMapper entidadBancariaMapper;
    private final SucursalMapper sucursalMapper;
    private final LocacionGeograficaMapper locacionGeograficaMapper;
    private final MonedaMapper monedaMapper;

    public EntidadBancariaSucursalServicio(EntidadBancariaRepositorio entidadBancariaRepositorio, SucursalRepositorio sucursalRepositorio, LocacionGeograficaRepositorio locacionRepositorio, MonedaRepositorio monedaRepositorio, EntidadBancariaMapper entidadBancariaMapper, SucursalMapper sucursalMapper, LocacionGeograficaMapper locacionGeograficaMapper, MonedaMapper monedaMapper) {
        this.entidadBancariaRepositorio = entidadBancariaRepositorio;
        this.sucursalRepositorio = sucursalRepositorio;
        this.locacionGeograficaRepositorio = locacionRepositorio;
        this.monedaRepositorio = monedaRepositorio;
        this.entidadBancariaMapper = entidadBancariaMapper;
        this.sucursalMapper = sucursalMapper;
        this.locacionGeograficaMapper = locacionGeograficaMapper;
        this.monedaMapper = monedaMapper;
    }

    // ================= ENTIDAD BANCARIA =================

    @Transactional
    public EntidadBancaria crearEntidad(EntidadBancariaCreacionDTO dto) {
        log.info("Creando nueva entidad bancaria con nombre: {}", dto.getNombre());
        if(entidadBancariaRepositorio.findByCodigoLocal(dto.getCodigoLocal()).isPresent()) {
            log.error("Ya existe una entidad bancaria con el código local: {}", dto.getCodigoLocal());
            throw new CrearEntidadException("EntidadBancaria", "Ya existe una entidad bancaria con el código local: " + dto.getCodigoLocal());
        }

        EntidadBancaria entidad = entidadBancariaMapper.toEntity(dto);
        entidad.setEstado("ACTIVO");

        if (dto.getCodigosMoneda() != null && !dto.getCodigosMoneda().isEmpty()) {
            List<MonedaDTO> monedas = new ArrayList<>();
            for (String codigoMoneda : dto.getCodigosMoneda()) {
                Moneda moneda = monedaRepositorio.findByCodigoMoneda(codigoMoneda);
                if (moneda != null) {
                    monedas.add(monedaMapper.toDTO(moneda));
                } else {
                    log.warn("No se encontró la moneda con el código: {}", codigoMoneda);
                    throw new EntidadNoEncontradaException("No se encontró la moneda con el código: " + codigoMoneda, 0, "Moneda");
                }
            }
            entidad.setMonedas(monedas);
        }
        entidad.setVersion(1L);
        return entidadBancariaRepositorio.save(entidad);
    }

    public EntidadBancaria obtenerEntidadPorCodigoLocal(String codigoLocal) {
        return entidadBancariaRepositorio.findByCodigoLocal(codigoLocal)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la entidad bancaria con código: " + codigoLocal, 0, ""));
    }

    public EntidadBancariaDTO obtenerPrimeraEntidadBancariaActiva() {
        log.info("Obteniendo la primera entidad bancaria activa");
        EntidadBancaria entidad = entidadBancariaRepositorio.findFirstByEstado("ACTIVO")
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontraron entidades bancarias activas.", 2, "EntidadBancaria"));
        return entidadBancariaMapper.toDTO(entidad);
    }

    @Transactional
    public void cambiarEstadoEntidadBancaria(String codigoLocal, String nuevoEstado) {
        log.info("Cambiando estado de la entidad bancaria con codigoLocal: {} a {}", codigoLocal, nuevoEstado);
        EntidadBancaria entidad = entidadBancariaRepositorio.findByCodigoLocal(codigoLocal)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la entidad bancaria con codigoLocal: " + codigoLocal, 2, "EntidadBancaria"));
        entidad.setEstado(nuevoEstado);
        entidad.setVersion(entidad.getVersion() + 1);

        if ("INACTIVO".equals(nuevoEstado)) {
            List<Sucursal> sucursalesAsociadas = sucursalRepositorio.findByCodigoEntidadBancaria(codigoLocal);
            if (!sucursalesAsociadas.isEmpty()) {
                for (Sucursal sucursal : sucursalesAsociadas) {
                    sucursal.setEstado("INACTIVO");
                    sucursal.setVersion(sucursal.getVersion() + 1);
                    sucursalRepositorio.save(sucursal);
                }
                log.info("Todas las sucursales asociadas a la entidad bancaria con código: {} han sido desactivadas", codigoLocal);

            }
        }
        entidadBancariaRepositorio.save(entidad);
        log.info("Estado de la entidad bancaria actualizado a {} para la entidad con código: {}", nuevoEstado, codigoLocal);

    }

    @Transactional
    public EntidadBancariaDTO agregarMonedasAEntidad(String codigoLocal, EntidadBancariaMonedaCreacionDTO dto) {
        EntidadBancaria entidad = obtenerEntidadPorCodigoLocal(codigoLocal);

        List<MonedaDTO> monedasSoportadas = entidad.getMonedas();
        if (monedasSoportadas == null) {
            monedasSoportadas = new ArrayList<>();
        }

        Set<String> codigosExistentes = monedasSoportadas.stream()
                .map(MonedaDTO::getCodigoMoneda)
                .collect(Collectors.toSet());
        for (String codigoMoneda : dto.getCodigosMoneda()) {
            if (!codigosExistentes.contains(codigoMoneda)) {
                Moneda moneda = monedaRepositorio.findByCodigoMoneda(codigoMoneda);
                if (moneda != null) {
                    monedasSoportadas.add(monedaMapper.toDTO(moneda));
                } else {
                    throw new EntidadNoEncontradaException("No se encontró la moneda con el código: " + codigoMoneda, 0, "Moneda");
                }
            }
        }

        entidad.setMonedas(monedasSoportadas);
        entidadBancariaRepositorio.save(entidad);
        return entidadBancariaMapper.toDTO(entidad);
    }

    // ================= SUCURSAL =================

    public Sucursal obtenerSucursalPorCodigo(String codigoSucursal) {
        return sucursalRepositorio.findByCodigoSucursal(codigoSucursal)
                .orElseThrow(() -> new EntidadNoEncontradaException("Sucursal no encontrada con código: " + codigoSucursal, 2, "Sucursal"));
    }

    @Transactional
    public Sucursal crearSucursal(String entidadCodigoLocal, SucursalCreacionDTO dto) {
        log.info("Iniciando creación de sucursal con código: {}", dto.getCodigoSucursal());
        if (sucursalRepositorio.findByCodigoSucursal(dto.getCodigoSucursal()).isPresent()) {
            throw new CrearEntidadException("Sucursal", "Ya existe una sucursal con el código: " + dto.getCodigoSucursal());
        }
        try {
            EntidadBancaria entidad = obtenerEntidadPorCodigoLocal(entidadCodigoLocal);

            LocacionGeografica locacion = locacionGeograficaRepositorio.findByCodigoLocacion(dto.getCodigoLocacion())
                    .orElseThrow(() -> new EntidadNoEncontradaException("Locación no encontrada con codigo: " + dto.getCodigoLocacion(), 1, "Locacion"));

            Sucursal sucursal = sucursalMapper.toEntity(dto);
            sucursal.setCodigoEntidadBancaria(entidad.getCodigoLocal());
            sucursal.setLocacionGeografica(locacionGeograficaMapper.toEmbeddedDTO(locacion));
            sucursal.setFechaCreacion(LocalDate.now());
            sucursal.setEstado("ACTIVO");
            sucursal.setVersion(1L);
            Sucursal savedEntity = sucursalRepositorio.save(sucursal);
            log.info("Sucursal con código {} creada exitosamente.", savedEntity.getCodigoSucursal());
            return savedEntity;
        } catch (Exception e) {
            log.error("Error al crear sucursal: {}", e.getMessage(), e);
            throw new CrearEntidadException("Sucursal", "Error al crear la sucursal: " + e.getMessage());
        }
    }

    @Transactional
    public Sucursal modificarSucursal(String codigoSucursal, SucursalUpdateDTO dto) {
        log.info("Iniciando modificación de sucursal con codigo: {}", codigoSucursal);
        Sucursal sucursal = obtenerSucursalPorCodigo(codigoSucursal);
        try {
            sucursalMapper.updateFromDTO(dto, sucursal);
            sucursal.setVersion(sucursal.getVersion() + 1);
            Sucursal savedEntity = sucursalRepositorio.save(sucursal);
            log.info("Sucursal con ID {} modificada exitosamente.", savedEntity.getId());
            return savedEntity;
        } catch (Exception e) {
            log.error("Error al modificar sucursal con codigo {}: {}", codigoSucursal, e.getMessage(), e);
            throw new ActualizarEntidadException("Sucursal", "Error al modificar la sucursal: " + e.getMessage());
        }
    }

    @Transactional
    public void cambiarEstadoSucursal(String codigoSucursal, String nuevoEstado) {
        log.info("Cambiando estado de la sucursal con codigo: {} a {}", codigoSucursal, nuevoEstado);
        Sucursal sucursal = obtenerSucursalPorCodigo(codigoSucursal);
        try {
            if ("ACTIVO".equals(nuevoEstado)) {
                EntidadBancaria entidad = entidadBancariaRepositorio.findByCodigoLocal(sucursal.getCodigoEntidadBancaria())
                        .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la entidad bancaria con código: " + sucursal.getCodigoEntidadBancaria(), 2, "EntidadBancaria"));
                if (!"ACTIVO".equals(entidad.getEstado())) {
                    throw new ActualizarEntidadException("Sucursal", "No se puede activar una sucursal si la entidad bancaria no está activa.");
                }
                LocacionGeografica locacion = locacionGeograficaRepositorio.findByCodigoLocacion(sucursal.getLocacionGeografica().getCodigoLocacion())
                        .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la locación con código: " + sucursal.getLocacionGeografica().getCodigoLocacion(), 2, "LocacionGeografica"));
                if (!"ACTIVO".equals(locacion.getEstado())) {
                    throw new ActualizarEntidadException("Sucursal", "No se puede activar una sucursal si la locación no está activa.");
                }
            }
            sucursal.setEstado(nuevoEstado);
            sucursal.setVersion(sucursal.getVersion() + 1);
            sucursalRepositorio.save(sucursal);
            log.info("Estado de la sucursal con código {} cambiado a {}.", codigoSucursal, nuevoEstado);
        } catch (Exception e) {
            log.error("Error al cambiar estado de sucursal con código {}: {}", codigoSucursal, e.getMessage(), e);
            throw new ActualizarEntidadException("Sucursal", "Error al cambiar el estado de la sucursal: " + e.getMessage());
        }
    }

    public List<Sucursal> listarSucursalesActivasPorCodigoLocacion(String codigoLocacion) {
        log.info("Listando sucursales activas para la locación con código: {}", codigoLocacion);
        List<Sucursal> sucursales = sucursalRepositorio.findByLocacionGeografica_CodigoLocacionAndEstado(codigoLocacion, "ACTIVO");
        log.info("Se encontraron {} sucursales activas para la locación {}", sucursales.size(), codigoLocacion);
        return sucursales;
    }
}
