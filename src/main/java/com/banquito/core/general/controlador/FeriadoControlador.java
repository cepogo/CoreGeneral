package com.banquito.core.general.controlador;

import com.banquito.core.general.dto.FeriadoCreacionDTO;
import com.banquito.core.general.dto.FeriadoDTO;
import com.banquito.core.general.dto.FeriadoUpdateDTO;
import com.banquito.core.general.enums.EstadoGeneralEnum;
import com.banquito.core.general.mapper.FeriadoMapper;
import com.banquito.core.general.modelo.Feriado;
import com.banquito.core.general.servicio.FeriadoServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/feriados")
@RequiredArgsConstructor
@Tag(name = "Feriado", description = "Operaciones para gestionar feriados")
public class FeriadoControlador {

    private final FeriadoServicio feriadoServicio;
    private final FeriadoMapper feriadoMapper;

    @PostMapping
    @Operation(summary = "Crear un nuevo feriado", description = "Crea un nuevo feriado. El campo 'locacionId' es obligatorio para feriados de tipo LOCAL.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Feriado creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Locación no encontrada")
    })
    public ResponseEntity<FeriadoDTO> crear(@Valid @RequestBody FeriadoCreacionDTO dto) {
        Feriado feriado = feriadoMapper.toEntity(dto);
        Feriado feriadoCreado = feriadoServicio.crear(feriado, dto.getLocacionId());
        return ResponseEntity.ok(feriadoMapper.toDTO(feriadoCreado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar un feriado existente", description = "Modifica la fecha y el nombre de un feriado a partir de su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Feriado modificado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Feriado no encontrado")
    })
    public ResponseEntity<FeriadoDTO> modificar(
            @PathVariable String id,
            @Valid @RequestBody FeriadoUpdateDTO dto) {
        Feriado feriado = feriadoServicio.obtenerPorId(id);
        feriadoMapper.updateFromDTO(dto, feriado);
        Feriado feriadoActualizado = feriadoServicio.modificar(feriado);
        return ResponseEntity.ok(feriadoMapper.toDTO(feriadoActualizado));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar el estado de un feriado", description = "Cambia el estado (ACTIVO/INACTIVO) de un feriado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado cambiado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Feriado no encontrado")
    })
    public ResponseEntity<Void> cambiarEstado(
            @PathVariable String id,
            @RequestParam EstadoGeneralEnum nuevoEstado) {
        Feriado feriado = feriadoServicio.obtenerPorId(id);
        feriado.setEstado(nuevoEstado);
        feriadoServicio.cambiarEstado(feriado);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "Listar todos los feriados activos de un año", description = "Devuelve todos los feriados (nacionales y locales) activos de un año.")
    public ResponseEntity<List<FeriadoDTO>> obtenerPorAnio(@RequestParam int anio) {
        List<Feriado> feriados = feriadoServicio.obtenerPorAnio(anio);
        List<FeriadoDTO> dtos = feriados.stream()
                .map(feriadoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/nacionales")
    @Operation(summary = "Listar feriados nacionales activos de un año", description = "Devuelve solo los feriados nacionales activos de un año.")
    public ResponseEntity<List<FeriadoDTO>> obtenerNacionalesPorAnio(@RequestParam int anio) {
        List<Feriado> feriados = feriadoServicio.obtenerNacionalesPorAnio(anio);
        List<FeriadoDTO> dtos = feriados.stream()
                .map(feriadoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
} 