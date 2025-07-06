package com.banquito.core.general.servicio;

import com.banquito.core.general.dto.LocacionGeograficaDTO;
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
            locacion.setEstado("ACTIVO");
            locacion.setVersion(1L);
            LocacionGeografica locacionGuardada = locacionGeograficaRepositorio.save(locacion);
            log.info("Locación geográfica creada exitosamente con ID: {}", locacionGuardada.getId());
            return locacionGuardada;
        } catch (Exception e) {
            log.error("Error inesperado al crear locación geográfica para {}: {}", locacion.getProvincia(), e.getMessage(), e);
            throw new CrearEntidadException("LocacionGeografica", "Error al crear locación geográfica: " + e.getMessage());
        }
    }

    public void cambiarEstadoLocacionGeografica(String codigoLocacion, String nuevoEstado) {
        log.info("Cambiando estado de locación geográfica con código: {} a {}", codigoLocacion, nuevoEstado);
        LocacionGeografica entity = locacionGeograficaRepositorio.findByCodigoLocacion(codigoLocacion)
                .orElseThrow(() -> new EntidadNoEncontradaException("Locación geográfica no encontrada con código: " + codigoLocacion, null, "LocacionGeografica"));
        try {
            entity.setEstado(nuevoEstado);
            entity.setVersion(entity.getVersion() == null ? 1L : entity.getVersion() + 1L);
            locacionGeograficaRepositorio.save(entity);
            log.info("Estado de locación geográfica con código {} cambiado a {}.", codigoLocacion, nuevoEstado);
        } catch (Exception e) {
            log.error("Error al cambiar estado de locación geográfica {}: {}", codigoLocacion, e.getMessage(), e);
            throw new ActualizarEntidadException("LocacionGeografica", "Error al cambiar el estado de la locación geográfica: " + e.getMessage());
        }
    }

    // Método único para filtrar locaciones por nivel
    public List<LocacionGeografica> listarLocacionesPorNivel(String nivel, String codigoProvincia, String codigoCanton) {
        log.info("Listando locaciones para nivel: {}, provincia: {}, cantón: {}", nivel, codigoProvincia, codigoCanton);
        
        List<LocacionGeografica> locaciones;
        
        switch (nivel != null ? nivel.toLowerCase() : "provincia") {
            case "provincia":
                locaciones = locacionGeograficaRepositorio.findByEstadoAndCodigoCantonIsNullAndCodigoParroquiaIsNull("ACTIVO");
                log.info("Se encontraron {} provincias", locaciones.size());
                break;
                
            case "canton":
                if (codigoProvincia == null || codigoProvincia.isEmpty()) {
                    throw new IllegalArgumentException("Para listar cantones debe especificar el código de provincia");
                }
                locaciones = locacionGeograficaRepositorio.findByEstadoAndCodigoProvinciaAndCodigoParroquiaIsNull("ACTIVO", codigoProvincia);
                log.info("Se encontraron {} cantones para la provincia {}", locaciones.size(), codigoProvincia);
                break;
                
            case "parroquia":
                if (codigoProvincia == null || codigoProvincia.isEmpty() || codigoCanton == null || codigoCanton.isEmpty()) {
                    throw new IllegalArgumentException("Para listar parroquias debe especificar el código de provincia y cantón");
                }
                locaciones = locacionGeograficaRepositorio.findByEstadoAndCodigoProvinciaAndCodigoCanton("ACTIVO", codigoProvincia, codigoCanton);
                log.info("Se encontraron {} parroquias para el cantón {} de la provincia {}", locaciones.size(), codigoCanton, codigoProvincia);
                break;
                
            default:
                throw new IllegalArgumentException("Nivel no válido. Use: provincia, canton, o parroquia");
        }
        
        return locaciones;
    }

    // ================= FERIADO =================

    private String generarCodigoFeriado(String nombre, int anio) {
        // Tomar las primeras 3 letras del nombre y agregar el año
        String prefijo = nombre.length() >= 3 ? nombre.substring(0, 3).toUpperCase() : nombre.toUpperCase();
        return prefijo + anio;
    }

    public Feriado crearFeriado(Feriado feriado, String codigoLocacion) {
        log.info("Iniciando creación de feriado '{}', tipo {}", feriado.getNombre(), feriado.getTipo());
        
        // Generar código de feriado automáticamente
        String codigoFeriado = generarCodigoFeriado(feriado.getNombre(), feriado.getFecha().getYear());
        feriado.setCodigoFeriado(codigoFeriado);
        log.info("Código de feriado generado: {}", codigoFeriado);
        
        if ("LOCAL".equals(feriado.getTipo())) {
            if (codigoLocacion == null || codigoLocacion.isEmpty()) {
                throw new CrearEntidadException("Feriado", "Para feriados locales debe especificar una locación.");
            }
            
            LocacionGeografica locacion = locacionGeograficaRepositorio.findByCodigoLocacion(codigoLocacion)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la locación con código: " + codigoLocacion, null, "LocacionGeografica"));
            
            LocacionGeograficaDTO locacionEmbebida = locacionMapper.toEmbeddedDTO(locacion);
            feriado.setLocacion(locacionEmbebida);
        } else {
            log.debug("Feriado NACIONAL, no se asigna locación específica.");
            feriado.setLocacion(null);
        }
        
        feriado.setEstado("ACTIVO");
        feriado.setVersion(1L);
        Feriado feriadoGuardado = feriadosRepositorio.save(feriado);
        log.info("Feriado '{}' creado exitosamente con código: {}", feriadoGuardado.getNombre(), feriadoGuardado.getCodigoFeriado());
        return feriadoGuardado;
    }

    public Feriado obtenerFeriadoPorCodigo(String codigoFeriado) {
        log.info("Buscando feriado con código: {}", codigoFeriado);
        return feriadosRepositorio.findByCodigoFeriado(codigoFeriado)
            .orElseThrow(() -> new EntidadNoEncontradaException("Feriado no encontrado con código: " + codigoFeriado, null, "Feriado"));
    }

    public void cambiarEstadoFeriadoPorCodigo(String codigoFeriado, String nuevoEstado) {
        log.info("Cambiando estado del feriado con código: {} a {}", codigoFeriado, nuevoEstado);
        Feriado feriado = obtenerFeriadoPorCodigo(codigoFeriado);
        feriado.setEstado(nuevoEstado);
        feriado.setVersion(feriado.getVersion() + 1L);
        feriadosRepositorio.save(feriado);
        log.info("Estado del feriado con código {} cambiado a {}.", codigoFeriado, nuevoEstado);
    }

    // Listar feriados por tipo y año
    public List<Feriado> listarFeriadosPorTipoYAnio(String tipo, int anio) {
        log.info("Listando feriados {} para el año: {}", tipo, anio);
        LocalDate fechaInicio = LocalDate.of(anio, 1, 1);
        LocalDate fechaFin = LocalDate.of(anio, 12, 31);
        List<Feriado> feriados = feriadosRepositorio.findByEstadoAndTipoAndFechaBetween(
            "ACTIVO", tipo, fechaInicio, fechaFin);
        log.info("Se encontraron {} feriados {} para el año {}", feriados.size(), tipo, anio);
        return feriados;
    }

    // Listar feriados por año
    public List<Feriado> listarFeriadosPorAnio(int anio) {
        log.info("Listando todos los feriados para el año: {}", anio);
        LocalDate fechaInicio = LocalDate.of(anio, 1, 1);
        LocalDate fechaFin = LocalDate.of(anio, 12, 31);
        List<Feriado> feriados = feriadosRepositorio.findByEstadoAndFechaBetween(
            "ACTIVO", fechaInicio, fechaFin);
        log.info("Se encontraron {} feriados para el año {}", feriados.size(), anio);
        return feriados;
    }
} 