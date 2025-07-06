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

    @GetMapping("/paises/estado/{estado}")
    @Operation(summary = "Listar países por estado")
    public ResponseEntity<List<PaisDTO>> listarPaisesPorEstado(@PathVariable String estado) {
        List<PaisDTO> dtos = service.listarPaisesPorEstado(estado);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/paises/{codigoPais}")
    @Operation(summary = "Modificar país")
    public ResponseEntity<PaisDTO> actualizarPais(@PathVariable String codigoPais, @Valid @RequestBody PaisDTO dto) {
        PaisDTO actualizado = service.actualizarPais(codigoPais, dto);
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/paises/{codigoPais}/estado")
    @Operation(summary = "Cambiar estado de país")
    public ResponseEntity<Void> cambiarEstadoPais(@PathVariable String codigoPais, @RequestParam String estado) {
        service.cambiarEstadoPais(codigoPais, estado);
        return ResponseEntity.ok().build();
    }

    // ================= MONEDAS =================

    @PostMapping("/monedas")
    @Operation(summary = "Crear moneda")
    public ResponseEntity<MonedaDTO> crearMoneda(@Valid @RequestBody MonedaDTO dto) {
        MonedaDTO creado = service.crearMoneda(dto);
        return ResponseEntity.ok(creado);
    }

    @GetMapping("/monedas/estado/{estado}")
    @Operation(summary = "Listar monedas por estado")
    public ResponseEntity<List<MonedaDTO>> listarMonedasPorEstado(@PathVariable String estado) {
        List<MonedaDTO> dtos = service.listarMonedasPorEstado(estado);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/monedas/{codigoMoneda}")
    @Operation(summary = "Modificar moneda")
    public ResponseEntity<MonedaDTO> actualizarMoneda(@PathVariable String codigoMoneda, @Valid @RequestBody MonedaDTO dto) {
        MonedaDTO actualizado = service.actualizarMoneda(codigoMoneda, dto);
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/monedas/{codigoMoneda}/estado")
    @Operation(summary = "Cambiar estado de moneda")
    public ResponseEntity<Void> cambiarEstadoMoneda(@PathVariable String codigoMoneda, @RequestParam String estado) {
        service.cambiarEstadoMoneda(codigoMoneda, estado);
        return ResponseEntity.ok().build();
    }
} 