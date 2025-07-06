package com.banquito.core.general.controlador;

import com.banquito.core.general.modelo.Moneda;
import com.banquito.core.general.modelo.EntidadBancaria;
import com.banquito.core.general.modelo.LocacionGeografica;
import com.banquito.core.general.modelo.Sucursal;
import com.banquito.core.general.modelo.Pais;
import com.banquito.core.general.repositorio.MonedaRepositorio;
import com.banquito.core.general.repositorio.EntidadBancariaRepositorio;
import com.banquito.core.general.repositorio.LocacionGeograficaRepositorio;
import com.banquito.core.general.repositorio.SucursalRepositorio;
import com.banquito.core.general.repositorio.PaisRepositorio;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/externo")
@RequiredArgsConstructor
@Tag(name = "Datos Externos", description = "APIs para consumo de otros microservicios")
public class ExternoControlador {
    
    private final MonedaRepositorio monedaRepositorio;
    private final EntidadBancariaRepositorio entidadBancariaRepositorio;
    private final LocacionGeograficaRepositorio locacionGeograficaRepositorio;
    private final SucursalRepositorio sucursalRepositorio;
    private final PaisRepositorio paisRepositorio;

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

    // ================= SUCURSALES =================
    @GetMapping("/sucursales/{codigoSucursal}")
    @Operation(summary = "Verificar si existe una sucursal por código")
    public ResponseEntity<Boolean> existeSucursal(@PathVariable String codigoSucursal) {
        Optional<Sucursal> sucursal = sucursalRepositorio.findByCodigoSucursal(codigoSucursal);
        return ResponseEntity.ok(sucursal.isPresent() && "ACTIVO".equals(sucursal.get().getEstado()));
    }

    // ================= PAÍSES =================
    @GetMapping("/paises/{codigoPais}")
    @Operation(summary = "Verificar si existe un país por código")
    public ResponseEntity<Boolean> existePais(@PathVariable String codigoPais) {
        Pais pais = paisRepositorio.findByCodigoPais(codigoPais);
        return ResponseEntity.ok(pais != null && "ACTIVO".equals(pais.getEstado()));
    }
} 