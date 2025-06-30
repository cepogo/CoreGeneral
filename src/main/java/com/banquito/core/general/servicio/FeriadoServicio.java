package com.banquito.core.general.servicio;

import com.banquito.core.general.enums.EstadoGeneralEnum;
import com.banquito.core.general.enums.TipoFeriadosEnum;
import com.banquito.core.general.excepcion.ActualizarEntidadException;
import com.banquito.core.general.excepcion.CrearEntidadException;
import com.banquito.core.general.excepcion.EntidadNoEncontradaException;
import com.banquito.core.general.modelo.Feriado;
import com.banquito.core.general.modelo.LocacionGeografica;
import com.banquito.core.general.repositorio.FeriadosRepositorio;
import com.banquito.core.general.repositorio.LocacionGeograficaRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeriadoServicio {
    
    private final FeriadosRepositorio feriadosRepositorio;
    private final LocacionGeograficaRepositorio locacionGeograficaRepositorio;

    @Transactional
    public Feriado crear(Feriado feriado, String locacionId) {
        log.info("Iniciando creación de feriado '{}'", feriado.getNombre());
        
        if (TipoFeriadosEnum.LOCAL.equals(feriado.getTipo())) {
            if (locacionId == null || locacionId.isEmpty()) {
                throw new CrearEntidadException("Feriado", "Para feriados locales debe especificar una locación.");
            }
            log.debug("Feriado LOCAL, buscando locación con ID: {}", locacionId);
            LocacionGeografica locacion = locacionGeograficaRepositorio.findById(locacionId)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la locación con ID: " + locacionId, null, "LocacionGeografica"));
            feriado.setLocacion(locacion);
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

    @Transactional
    public Feriado modificar(Feriado feriado) {
        log.info("Iniciando modificación del feriado con ID: {}", feriado.getId());
        feriado.setVersion(feriado.getVersion() + 1L);
        Feriado feriadoActualizado = feriadosRepositorio.save(feriado);
        log.info("Feriado con ID {} modificado exitosamente.", feriado.getId());
        return feriadoActualizado;
    }

    @Transactional
    public Feriado cambiarEstado(Feriado feriado) {
        log.info("Cambiando estado del feriado con ID: {} a {}", feriado.getId(), feriado.getEstado());

        if (feriado.getEstado() == EstadoGeneralEnum.ACTIVO && feriado.getLocacion() != null) {
            if (!"ACTIVO".equals(feriado.getLocacion().getEstado())) { 
                log.warn("No se puede activar el feriado porque la locación geográfica '{}' está inactiva.", feriado.getLocacion().getProvincia());
                throw new ActualizarEntidadException("Feriado", "No se puede activar el feriado porque la locación geográfica '" + feriado.getLocacion().getProvincia() + "' está inactiva.");
            }
        }

        return this.modificar(feriado);
    }

    public List<Feriado> obtenerPorAnio(int anio) {
        log.info("Listando todos los feriados para el año: {}", anio);
        LocalDate fechaInicio = LocalDate.of(anio, 1, 1);
        LocalDate fechaFin = LocalDate.of(anio, 12, 31);
        
        List<Feriado> feriados = feriadosRepositorio.findByEstadoAndFechaBetween(
                EstadoGeneralEnum.ACTIVO, fechaInicio, fechaFin);
        
        log.info("Se encontraron {} feriados activos para el año {}", feriados.size(), anio);
        return feriados;
    }

    public List<Feriado> obtenerNacionalesPorAnio(int anio) {
        log.info("Listando feriados NACIONALES para el año: {}", anio);
        LocalDate fechaInicio = LocalDate.of(anio, 1, 1);
        LocalDate fechaFin = LocalDate.of(anio, 12, 31);

        List<Feriado> feriados = feriadosRepositorio.findByEstadoAndTipoAndFechaBetween(
                EstadoGeneralEnum.ACTIVO, TipoFeriadosEnum.NACIONAL, fechaInicio, fechaFin);

        log.info("Se encontraron {} feriados nacionales para el año {}", feriados.size(), anio);
        return feriados;
    }

    public Feriado obtenerPorId(String id) {
        log.debug("Buscando feriado con id: {}", id);
        return feriadosRepositorio.findById(id)
            .orElseThrow(() -> new EntidadNoEncontradaException("Feriado no encontrado con ID: " + id, null, "Feriado"));
    }
} 