package com.banquito.core.general.controlador;

import com.banquito.core.general.dto.*;
import com.banquito.core.general.mapper.MonedaMapper;
import com.banquito.core.general.mapper.EntidadBancariaMapper;
import com.banquito.core.general.mapper.LocacionGeograficaMapper;
import com.banquito.core.general.modelo.Moneda;
import com.banquito.core.general.modelo.EntidadBancaria;
import com.banquito.core.general.modelo.LocacionGeografica;
import com.banquito.core.general.repositorio.MonedaRepositorio;
import com.banquito.core.general.repositorio.EntidadBancariaRepositorio;
import com.banquito.core.general.repositorio.LocacionGeograficaRepositorio;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/externo")
@RequiredArgsConstructor
@Tag(name = "Datos Externos", description = "APIs para consumo de otros microservicios")
public class ExternoControlador {
    
    private final MonedaRepositorio monedaRepositorio;
    private final EntidadBancariaRepositorio entidadBancariaRepositorio;
    private final LocacionGeograficaRepositorio locacionGeograficaRepositorio;
    private final MonedaMapper monedaMapper;
    private final EntidadBancariaMapper entidadBancariaMapper;
    private final LocacionGeograficaMapper locacionMapper;

    // ================= MONEDAS =================
    @GetMapping("/monedas/{codigoMoneda}")
    @Operation(summary = "Verificar si existe una moneda por código")
    public ResponseEntity<Boolean> existeMoneda(@PathVariable String codigoMoneda) {
        Moneda moneda = monedaRepositorio.findByCodigoMoneda(codigoMoneda);
        return ResponseEntity.ok(moneda != null && "ACTIVO".equals(moneda.getEstado()));
    }

    // ================= ENTIDADES BANCARIAS =================
    @GetMapping("/entidades/{codigoLocal}")
    @Operation(summary = "Verificar si existe una entidad bancaria por código")
    public ResponseEntity<Boolean> existeEntidadBancaria(@PathVariable String codigoLocal) {
        Optional<EntidadBancaria> entidad = entidadBancariaRepositorio.findByCodigoLocal(codigoLocal);
        return ResponseEntity.ok(entidad.isPresent() && "ACTIVO".equals(entidad.get().getEstado()));
    }

    // ================= LOCACIONES GEOGRÁFICAS =================
    @GetMapping("/locaciones/{codigoLocacion}")
    @Operation(summary = "Verificar si existe una locación por código")
    public ResponseEntity<Boolean> existeLocacion(@PathVariable String codigoLocacion) {
        Optional<LocacionGeografica> locacion = locacionGeograficaRepositorio.findByCodigoLocacion(codigoLocacion);
        return ResponseEntity.ok(locacion.isPresent() && "ACTIVO".equals(locacion.get().getEstado()));
    }
} 