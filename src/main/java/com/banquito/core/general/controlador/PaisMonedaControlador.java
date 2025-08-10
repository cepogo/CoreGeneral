package com.banquito.core.general.controlador;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banquito.core.general.dto.MonedaDTO;
import com.banquito.core.general.dto.PaisDTO;
import com.banquito.core.general.servicio.PaisMonedaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/general/v1/paises-monedas")
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

    @GetMapping("/paises/estado/{estado}")
    @Operation(summary = "Listar países por estado")
    public ResponseEntity<List<PaisDTO>> listarPaisesPorEstado(@PathVariable String estado) {
        List<PaisDTO> dtos = service.listarPaisesPorEstado(estado);
        return ResponseEntity.ok(dtos);
    }


    @PatchMapping("/paises/{codigoPais}/estado")
    @Operation(summary = "Cambiar estado de país")
    public ResponseEntity<Void> cambiarEstadoPais(@PathVariable String codigoPais, @RequestParam String estado) {
        service.cambiarEstadoPais(codigoPais, estado);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/paises/{codigoPais}/moneda/{codigoMoneda}")
    @Operation(summary = "Agregar o actualizar la moneda de un país")
    public ResponseEntity<PaisDTO> agregarMonedaAPais(@PathVariable String codigoPais, @PathVariable String codigoMoneda) {
        PaisDTO paisActualizado = service.agregarMonedaAPais(codigoPais, codigoMoneda);
        return ResponseEntity.ok(paisActualizado);
    }

    // ================= MONEDAS =================

    @PostMapping("/monedas")
    @Operation(summary = "Crear moneda")
    public ResponseEntity<MonedaDTO> crearMoneda(@Valid @RequestBody MonedaDTO dto) {
        MonedaDTO creado = service.crearMoneda(dto);
        return ResponseEntity.ok(creado);
    }

    @GetMapping("/monedas/{codigoMoneda}")
    @Operation(summary = "Obtener moneda por código")
    public ResponseEntity<MonedaDTO> obtenerMonedaPorCodigo(@PathVariable String codigoMoneda) {
        MonedaDTO moneda = service.obtenerMonedaPorCodigo(codigoMoneda);
        return ResponseEntity.ok(moneda);
    }

    @GetMapping("/monedas/estado/{estado}")
    @Operation(summary = "Listar monedas por estado")
    public ResponseEntity<List<MonedaDTO>> listarMonedasPorEstado(@PathVariable String estado) {
        List<MonedaDTO> dtos = service.listarMonedasPorEstado(estado);
        return ResponseEntity.ok(dtos);
    }


    @PatchMapping("/monedas/{codigoMoneda}/estado")
    @Operation(summary = "Cambiar estado de moneda")
    public ResponseEntity<Void> cambiarEstadoMoneda(@PathVariable String codigoMoneda, @RequestParam String estado) {
        service.cambiarEstadoMoneda(codigoMoneda, estado);
        return ResponseEntity.ok().build();
    }
} 