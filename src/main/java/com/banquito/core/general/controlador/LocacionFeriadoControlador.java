package com.banquito.core.general.controlador;

import com.banquito.core.general.dto.*;
import com.banquito.core.general.mapper.LocacionGeograficaMapper;
import com.banquito.core.general.mapper.FeriadoMapper;
import com.banquito.core.general.modelo.LocacionGeografica;
import com.banquito.core.general.modelo.Feriado;
import com.banquito.core.general.servicio.LocacionFeriadoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locaciones-feriados")
@RequiredArgsConstructor
@Tag(name = "Locaciones y Feriados", description = "Operaciones para locaciones geográficas y feriados")
public class LocacionFeriadoControlador {
    private final LocacionFeriadoService service;
    private final LocacionGeograficaMapper locacionMapper;
    private final FeriadoMapper feriadoMapper;

    // ================= LOCACIONES =================

    @PostMapping("/locaciones")
    @Operation(summary = "Crear locación geográfica")
    public ResponseEntity<LocacionGeograficaDTO> crearLocacion(@Valid @RequestBody LocacionGeograficaCreacionDTO dto) {
        LocacionGeografica entity = locacionMapper.toEntity(dto);
        LocacionGeografica creada = service.crearLocacionGeografica(entity);
        return ResponseEntity.ok(locacionMapper.toDTO(creada));
    }

    @GetMapping("/locaciones")
    @Operation(summary = "Listar locaciones por nivel geográfico", 
               description = "Filtrar por nivel: provincia, canton, parroquia. Para canton requiere codigoProvincia. Para parroquia requiere codigoProvincia y codigoCanton.")
    public ResponseEntity<?> listarLocaciones(
            @RequestParam(required = false, defaultValue = "provincia") String nivel,
            @RequestParam(required = false) String codigoProvincia,
            @RequestParam(required = false) String codigoCanton) {
        
        List<LocacionGeografica> locaciones = service.listarLocacionesPorNivel(nivel, codigoProvincia, codigoCanton);
        
        switch (nivel.toLowerCase()) {
            case "provincia":
                List<ProvinciaDTO> provincias = locaciones.stream()
                        .map(locacionMapper::toProvinciaDTO)
                        .toList();
                return ResponseEntity.ok(provincias);
                
            case "canton":
                List<CantonDTO> cantones = locaciones.stream()
                        .map(locacionMapper::toCantonDTO)
                        .toList();
                return ResponseEntity.ok(cantones);
                
            case "parroquia":
                List<ParroquiaDTO> parroquias = locaciones.stream()
                        .map(locacionMapper::toParroquiaDTO)
                        .toList();
                return ResponseEntity.ok(parroquias);
                
            default:
                List<LocacionGeograficaDTO> dtos = locaciones.stream()
                        .map(locacionMapper::toDTO)
                        .toList();
                return ResponseEntity.ok(dtos);
        }
    }

    @PatchMapping("/locaciones/codigo/{codigoLocacion}/estado")
    @Operation(summary = "Cambiar estado de locación geográfica por código")
    public ResponseEntity<Void> cambiarEstadoLocacion(@PathVariable String codigoLocacion, @RequestParam String nuevoEstado) {
        service.cambiarEstadoLocacionGeografica(codigoLocacion, nuevoEstado);
        return ResponseEntity.ok().build();
    }

   // ================= FERIADOS =================

    @PostMapping("/feriados")
    @Operation(summary = "Crear feriado")
    public ResponseEntity<FeriadoDTO> crearFeriado(@Valid @RequestBody FeriadoCreacionDTO dto) {
        Feriado feriado = feriadoMapper.toEntity(dto);
        Feriado creado = service.crearFeriado(feriado, dto.getLocacionId());
        return ResponseEntity.ok(feriadoMapper.toDTO(creado));
    }

    @PatchMapping("/feriados/codigo/{codigoFeriado}/estado")
    @Operation(summary = "Cambiar estado de feriado por código")
    public ResponseEntity<Void> cambiarEstadoFeriadoPorCodigo(@PathVariable String codigoFeriado, @RequestParam String nuevoEstado) {
        service.cambiarEstadoFeriadoPorCodigo(codigoFeriado, nuevoEstado);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/feriados/tipo/{tipo}/anio/{anio}")
    @Operation(summary = "Listar feriados por tipo y año")
    public ResponseEntity<List<FeriadoDTO>> listarFeriadosPorTipoYAnio(@PathVariable String tipo, @PathVariable int anio) {
        List<FeriadoDTO> dtos = service.listarFeriadosPorTipoYAnio(tipo, anio)
                .stream()
                .map(feriadoMapper::toDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/feriados/anio/{anio}")
    @Operation(summary = "Listar feriados por año")
    public ResponseEntity<List<FeriadoDTO>> listarFeriadosPorAnio(@PathVariable int anio) {
        List<FeriadoDTO> dtos = service.listarFeriadosPorAnio(anio)
                .stream()
                .map(feriadoMapper::toDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }
} 