package com.banquito.core.general.controlador;

import com.banquito.core.general.dto.MonedaDTO;
import com.banquito.core.general.dto.PaisDTO;
import com.banquito.core.general.servicio.PaisMonedaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/paises-monedas")
@RequiredArgsConstructor
@Tag(name = "Países y Monedas", description = "Operaciones para países y monedas")
public class PaisMonedaControlador {
    private final PaisMonedaService service;

    // ================= PAÍSES =================

    @PostMapping("/paises")
    @Operation(summary = "Crear país")
    public ResponseEntity<PaisDTO> crearPais(@Valid @RequestBody PaisDTO dto) {
        PaisDTO creado = service.crearPais(dto);
        return ResponseEntity.ok(creado);
    }

    @GetMapping("/paises")
    @Operation(summary = "Listar países")
    public ResponseEntity<List<PaisDTO>> listarPaises() {
        List<PaisDTO> dtos = service.listarPaises();
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/paises/{id}")
    @Operation(summary = "Modificar país")
    public ResponseEntity<PaisDTO> actualizarPais(@PathVariable String id, @Valid @RequestBody PaisDTO dto) {
        PaisDTO actualizado = service.actualizarPais(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/paises/{id}/estado")
    @Operation(summary = "Cambiar estado de país")
    public ResponseEntity<Void> cambiarEstadoPais(@PathVariable String id, @RequestParam String estado) {
        service.cambiarEstadoPais(id, estado);
        return ResponseEntity.ok().build();
    }

    // ================= MONEDAS =================

    @PostMapping("/paises/{idPais}/monedas")
    @Operation(summary = "Crear moneda para un país")
    public ResponseEntity<MonedaDTO> crearMoneda(@PathVariable String idPais, @Valid @RequestBody MonedaDTO dto) {
        MonedaDTO creado = service.crearMoneda(idPais, dto);
        return ResponseEntity.ok(creado);
    }

    @GetMapping("/monedas")
    @Operation(summary = "Listar monedas")
    public ResponseEntity<List<MonedaDTO>> listarMonedas() {
        List<MonedaDTO> dtos = service.listarMonedas();
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/monedas/{id}")
    @Operation(summary = "Modificar moneda")
    public ResponseEntity<MonedaDTO> actualizarMoneda(@PathVariable String id, @Valid @RequestBody MonedaDTO dto) {
        MonedaDTO actualizado = service.actualizarMoneda(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/monedas/{id}/estado")
    @Operation(summary = "Cambiar estado de moneda")
    public ResponseEntity<Void> cambiarEstadoMoneda(@PathVariable String id, @RequestParam String estado) {
        service.cambiarEstadoMoneda(id, estado);
        return ResponseEntity.ok().build();
    }
} 