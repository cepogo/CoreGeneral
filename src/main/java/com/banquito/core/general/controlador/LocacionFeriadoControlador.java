package com.banquito.core.general.controlador;

import com.banquito.core.general.dto.LocacionGeograficaDTO;
import com.banquito.core.general.dto.FeriadoDTO;
import com.banquito.core.general.dto.FeriadoCreacionDTO;
import com.banquito.core.general.dto.LocacionGeograficaCreacionDTO;
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

    @GetMapping("/locaciones/codigo/{codigoLocacion}")
    @Operation(summary = "Obtener locación geográfica por código")
    public ResponseEntity<LocacionGeograficaDTO> obtenerLocacionPorCodigo(@PathVariable String codigoLocacion) {
        LocacionGeografica locacion = service.obtenerLocacionPorCodigo(codigoLocacion);
        return ResponseEntity.ok(locacionMapper.toDTO(locacion));
    }



    @GetMapping("/locaciones/activas")
    @Operation(summary = "Listar locaciones geográficas activas")
    public ResponseEntity<List<LocacionGeograficaDTO>> listarLocacionesActivas() {
        List<LocacionGeograficaDTO> dtos = service.listarLocacionesActivas()
                .stream()
                .map(locacionMapper::toDTO)
                .toList(); // Usar toList() en lugar de collect(Collectors.toList()) - más eficiente
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/locaciones/{id}/estado")
    @Operation(summary = "Cambiar estado de locación geográfica")
    public ResponseEntity<Void> cambiarEstadoLocacion(@PathVariable String id, @RequestParam String nuevoEstado) {
        service.cambiarEstadoLocacionGeografica(id, nuevoEstado);
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


    @PatchMapping("/feriados/{id}/estado")
    @Operation(summary = "Cambiar estado de feriado")
    public ResponseEntity<Void> cambiarEstadoFeriado(@PathVariable String id, @RequestParam String nuevoEstado) {
        Feriado feriado = service.obtenerFeriadoPorId(id);
        feriado.setEstado(nuevoEstado);
        service.cambiarEstadoFeriado(feriado);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/feriados")
    @Operation(summary = "Listar feriados activos de un año")
    public ResponseEntity<List<FeriadoDTO>> listarFeriadosPorAnio(@RequestParam int anio) {
        List<FeriadoDTO> dtos = service.obtenerFeriadosPorAnio(anio)
                .stream()
                .map(feriadoMapper::toDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/feriados/nacionales")
    @Operation(summary = "Listar feriados nacionales de un año")
    public ResponseEntity<List<FeriadoDTO>> listarFeriadosNacionalesPorAnio(@RequestParam int anio) {
        List<FeriadoDTO> dtos = service.obtenerFeriadosNacionalesPorAnio(anio)
                .stream()
                .map(feriadoMapper::toDTO)
                .toList(); 
        return ResponseEntity.ok(dtos);
    }


} 