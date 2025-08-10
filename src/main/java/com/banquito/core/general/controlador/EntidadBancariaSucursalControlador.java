package com.banquito.core.general.controlador;

import java.util.List;
import java.util.stream.Collectors;

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

import com.banquito.core.general.dto.EntidadBancariaCreacionDTO;
import com.banquito.core.general.dto.EntidadBancariaDTO;
import com.banquito.core.general.dto.EntidadBancariaMonedaCreacionDTO;
import com.banquito.core.general.dto.SucursalCreacionDTO;
import com.banquito.core.general.dto.SucursalDTO;
import com.banquito.core.general.dto.SucursalUpdateDTO;
import com.banquito.core.general.mapper.EntidadBancariaMapper;
import com.banquito.core.general.mapper.SucursalMapper;
import com.banquito.core.general.modelo.Sucursal;
import com.banquito.core.general.servicio.EntidadBancariaSucursalServicio;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/general/v1/banco")
@Tag(name = "Entidades y Sucursales", description = "Gestiona las Entidades Bancarias y sus Sucursales")
public class EntidadBancariaSucursalControlador {

    private final EntidadBancariaSucursalServicio servicio;
    private final EntidadBancariaMapper entidadBancariaMapper;
    private final SucursalMapper sucursalMapper;

    public EntidadBancariaSucursalControlador(EntidadBancariaSucursalServicio servicio, EntidadBancariaMapper entidadBancariaMapper, SucursalMapper sucursalMapper) {
        this.servicio = servicio;
        this.entidadBancariaMapper = entidadBancariaMapper;
        this.sucursalMapper = sucursalMapper;
    }

    @PostMapping
    @Operation(summary = "Crear una nueva Entidad Bancaria", description = "Crea una nueva entidad bancaria en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Entidad creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<EntidadBancariaDTO> crearEntidad(@Valid @RequestBody EntidadBancariaCreacionDTO dto) {
        return ResponseEntity.ok(entidadBancariaMapper.toDTO(servicio.crearEntidad(dto)));
    }

    @GetMapping("/activa/primera")
    @Operation(summary = "Obtener la primera entidad bancaria activa", description = "Busca y devuelve la primera entidad bancaria que encuentre con estado ACTIVO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entidad encontrada"),
            @ApiResponse(responseCode = "404", description = "No se encontraron entidades activas")
    })
    public ResponseEntity<EntidadBancariaDTO> obtenerPrimeraEntidadBancariaActiva() {
        return ResponseEntity.ok(servicio.obtenerPrimeraEntidadBancariaActiva());
    }

    @PatchMapping("/{codigoLocal}/estado")
    @Operation(summary = "Cambiar el estado de una Entidad Bancaria", description = "Cambia el estado de una entidad bancaria a ACTIVO o INACTIVO. Si se inactiva, todas sus sucursales también se inactivarán.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Estado cambiado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Entidad no encontrada")
    })
    public ResponseEntity<Void> cambiarEstadoEntidad(@PathVariable String codigoLocal, @RequestParam String estado) {
        servicio.cambiarEstadoEntidadBancaria(codigoLocal, estado);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{entidadCodigoLocal}/sucursales")
    @Operation(summary = "Crear una nueva Sucursal para una Entidad", description = "Crea una nueva sucursal y la asocia a una entidad bancaria existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucursal creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Entidad Bancaria o Locación no encontrada")
    })
    public ResponseEntity<SucursalDTO> crearSucursal(@PathVariable String entidadCodigoLocal, @Valid @RequestBody SucursalCreacionDTO dto) {
        return ResponseEntity.ok(sucursalMapper.toDTO(servicio.crearSucursal(entidadCodigoLocal, dto)));
    }

    @PutMapping("/{codigoLocal}/monedas")
    @Operation(summary = "Agregar monedas a una Entidad Bancaria", description = "Asocia monedas a una entidad bancaria existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Monedas agregadas exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Entidad Bancaria o monedas no encontradas")
    })
    public ResponseEntity<EntidadBancariaDTO> agregarMonedasAEntidad( @PathVariable String codigoLocal, @RequestBody EntidadBancariaMonedaCreacionDTO dto) {
        try {
            EntidadBancariaDTO entidadActualizada = this.servicio.agregarMonedasAEntidad(codigoLocal, dto);
            return ResponseEntity.ok(entidadActualizada);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // ================= SUCURSALES =================

    @GetMapping("/sucursales")
    @Operation(summary = "Listar sucursales por nivel geográfico", description = "Filtra sucursales por provincia o cantón. Use nivel=provincia para ver todas las sucursales de una provincia, o nivel=canton para ver sucursales de un cantón específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
    })
    public ResponseEntity<List<SucursalDTO>> listarSucursalesPorNivel(
            @RequestParam(defaultValue = "provincia") String nivel,
            @RequestParam(required = false) String codigoProvincia,
            @RequestParam(required = false) String codigoCanton) {
        
        List<Sucursal> sucursales = servicio.listarSucursalesPorNivel(nivel, codigoProvincia, codigoCanton);
        List<SucursalDTO> sucursalesDTO = sucursales.stream()
                .map(sucursalMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(sucursalesDTO);
    }

    @PutMapping("/sucursales/{codigo}")
    @Operation(summary = "Modificar datos de una Sucursal", description = "Modifica los datos permitidos de una sucursal existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucursal modificada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    public ResponseEntity<SucursalDTO> modificarSucursal(@PathVariable String codigo, @Valid @RequestBody SucursalUpdateDTO dto) {
        return ResponseEntity.ok(sucursalMapper.toDTO(servicio.modificarSucursal(codigo, dto)));
    }

    @PatchMapping("/sucursales/{codigo}/estado")
    @Operation(summary = "Cambiar el estado de una Sucursal", description = "Cambia el estado de una sucursal a ACTIVO o INACTIVO.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Estado cambiado exitosamente"),
            @ApiResponse(responseCode = "400", description = "No se puede activar la sucursal si su entidad o locación están inactivas"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    public ResponseEntity<Void> cambiarEstadoSucursal(@PathVariable String codigo, @RequestParam String estado) {
        servicio.cambiarEstadoSucursal(codigo, estado);
        return ResponseEntity.noContent().build();
    }
}