package com.banquito.core.general.servicio;

import com.banquito.core.general.enums.EstadoGeneralEnum;
import com.banquito.core.general.enums.EstadoLocacionesGeograficasEnum;
import com.banquito.core.general.enums.TipoFeriadosEnum;
import com.banquito.core.general.excepcion.ActualizarEntidadException;
import com.banquito.core.general.excepcion.CrearEntidadException;
import com.banquito.core.general.excepcion.EntidadNoEncontradaException;
import com.banquito.core.general.mapper.LocacionGeograficaMapper;
import com.banquito.core.general.modelo.Feriado;
import com.banquito.core.general.modelo.LocacionGeografica;
import com.banquito.core.general.repositorio.FeriadosRepositorio;
import com.banquito.core.general.repositorio.LocacionGeograficaRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocacionFeriadoService {
    private final LocacionGeograficaRepositorio locacionGeograficaRepositorio;
    private final FeriadosRepositorio feriadosRepositorio;
    private final LocacionGeograficaMapper locacionMapper;

    // ================= LOCACION GEOGRAFICA =================

    public LocacionGeografica crearLocacionGeografica(LocacionGeografica locacion) {
        try {
            log.info("Creando nueva locación geográfica: {}", locacion.getProvincia());
            locacion.setEstado(EstadoLocacionesGeograficasEnum.ACTIVO);
            locacion.setVersion(1L);
            LocacionGeografica locacionGuardada = locacionGeograficaRepositorio.save(locacion);
            log.info("Locación geográfica creada exitosamente con ID: {}", locacionGuardada.getId());
            return locacionGuardada;
        } catch (Exception e) {
            log.error("Error inesperado al crear locación geográfica para {}: {}", locacion.getProvincia(), e.getMessage(), e);
            throw new CrearEntidadException("LocacionGeografica", "Error al crear locación geográfica: " + e.getMessage());
        }
    }

    public LocacionGeografica modificarLocacionGeografica(String idLocacion, LocacionGeografica cambios) {
        log.info("Iniciando modificación de locación geográfica con ID: {}", idLocacion);
        LocacionGeografica entity = this.locacionGeograficaRepositorio.findById(idLocacion)
                .orElseThrow(() -> new EntidadNoEncontradaException("Locación geográfica no encontrada", 2, "LocacionGeografica"));
        try {
            locacionMapper.updateFromEntity(cambios, entity);
            entity.setVersion(entity.getVersion() == null ? 1L : entity.getVersion() + 1L);
            LocacionGeografica savedEntity = this.locacionGeograficaRepositorio.save(entity);
            log.info("Locación geográfica con ID {} modificada exitosamente.", savedEntity.getId());
            return savedEntity;
        } catch (Exception e) {
            log.error("Error al modificar locación geográfica {}: {}", idLocacion, e.getMessage(), e);
            throw new ActualizarEntidadException("LocacionGeografica", "Error al modificar la locación geográfica: " + e.getMessage());
        }
    }

    public List<LocacionGeografica> listarLocacionesActivas() {
        log.info("Listando todas las locaciones geográficas activas");
        List<LocacionGeografica> locaciones = locacionGeograficaRepositorio.findByEstado(EstadoLocacionesGeograficasEnum.ACTIVO);
        log.info("Se encontraron {} locaciones activas", locaciones.size());
        return locaciones;
    }

    public void cambiarEstadoLocacionGeografica(String idLocacion, EstadoLocacionesGeograficasEnum nuevoEstado) {
        log.info("Cambiando estado de locación geográfica con ID: {} a {}", idLocacion, nuevoEstado);
        LocacionGeografica entity = locacionGeograficaRepositorio.findById(idLocacion)
                .orElseThrow(() -> new EntidadNoEncontradaException("Locación geográfica no encontrada", 2, "LocacionGeografica"));
        try {
            entity.setEstado(nuevoEstado);
            entity.setVersion(entity.getVersion() == null ? 1L : entity.getVersion() + 1L);
            locacionGeograficaRepositorio.save(entity);
            log.info("Estado de locación geográfica con ID {} cambiado a {}.", idLocacion, nuevoEstado);
        } catch (Exception e) {
            log.error("Error al cambiar estado de locación geográfica {}: {}", idLocacion, e.getMessage(), e);
            throw new ActualizarEntidadException("LocacionGeografica", "Error al cambiar el estado de la locación geográfica: " + e.getMessage());
        }
    }

    public LocacionGeografica obtenerLocacionPorId(String idLocacion) {
        log.info("Buscando locación geográfica con ID: {}", idLocacion);
        return locacionGeograficaRepositorio.findById(idLocacion)
                .orElseThrow(() -> new EntidadNoEncontradaException("Locación geográfica no encontrada", 2, "LocacionGeografica"));
    }

    // ================= FERIADO =================

    public Feriado crearFeriado(Feriado feriado, String locacionId) {
        log.info("Iniciando creación de feriado '{}', tipo {}", feriado.getNombre(), feriado.getTipo());
        
        if (TipoFeriadosEnum.LOCAL.equals(feriado.getTipo())) {
            if (locacionId == null || locacionId.isEmpty()) {
                throw new CrearEntidadException("Feriado", "Para feriados locales debe especificar una locación.");
            }
            
            LocacionGeografica locacion = locacionGeograficaRepositorio.findById(locacionId)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la locación con ID: " + locacionId, null, "LocacionGeografica"));
            
            com.banquito.core.general.modelo.LocacionGeograficaDTO locacionEmbebida = locacionMapper.toEmbeddedDTO(locacion);
            feriado.setLocacion(locacionEmbebida);
        } else {
            log.debug("Feriado NACIONAL, no se asigna locación específica.");
            feriado.setLocacion(null);
        }
        
        feriado.setEstado(EstadoGeneralEnum.ACTIVO);
        feriado.setVersion(1L);
        Feriado feriadoGuardado = feriadosRepositorio.save(feriado);
        log.info("Feriado '{}' creado exitosamente con ID: {}", feriadoGuardado.getNombre(), feriadoGuardado.getId());
        return feriadoGuardado;
    }

    public Feriado modificarFeriado(Feriado feriado) {
        log.info("Iniciando modificación del feriado con ID: {}", feriado.getId());
        feriado.setVersion(feriado.getVersion() + 1L);
        Feriado feriadoActualizado = feriadosRepositorio.save(feriado);
        log.info("Feriado con ID {} modificado exitosamente.", feriado.getId());
        return feriadoActualizado;
    }

    public Feriado cambiarEstadoFeriado(Feriado feriado) {
        log.info("Cambiando estado del feriado con ID: {} a {}", feriado.getId(), feriado.getEstado());
        return this.modificarFeriado(feriado);
    }

    public List<Feriado> obtenerFeriadosPorAnio(int anio) {
        log.info("Listando todos los feriados para el año: {}", anio);
        LocalDate fechaInicio = LocalDate.of(anio, 1, 1);
        LocalDate fechaFin = LocalDate.of(anio, 12, 31);
        List<Feriado> feriados = feriadosRepositorio.findByEstadoAndFechaBetween(
                EstadoGeneralEnum.ACTIVO, fechaInicio, fechaFin);
        log.info("Se encontraron {} feriados activos para el año {}", feriados.size(), anio);
        return feriados;
    }

    public List<Feriado> obtenerFeriadosNacionalesPorAnio(int anio) {
        log.info("Listando feriados NACIONALES para el año: {}", anio);
        LocalDate fechaInicio = LocalDate.of(anio, 1, 1);
        LocalDate fechaFin = LocalDate.of(anio, 12, 31);
        List<Feriado> feriados = feriadosRepositorio.findByEstadoAndTipoAndFechaBetween(
                EstadoGeneralEnum.ACTIVO, TipoFeriadosEnum.NACIONAL, fechaInicio, fechaFin);
        log.info("Se encontraron {} feriados nacionales para el año {}", feriados.size(), anio);
        return feriados;
    }

    public Feriado obtenerFeriadoPorId(String id) {
        log.debug("Buscando feriado con id: {}", id);
        return feriadosRepositorio.findById(id)
            .orElseThrow(() -> new EntidadNoEncontradaException("Feriado no encontrado con ID: " + id, null, "Feriado"));
    }
} 