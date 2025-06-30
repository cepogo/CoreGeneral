package com.banquito.core.general.controlador;

import com.banquito.core.general.dto.*;
import com.banquito.core.general.enums.EstadoGeneralEnum;
import com.banquito.core.general.enums.EstadoSucursalesEnum;
import com.banquito.core.general.mapper.EntidadBancariaMapper;
import com.banquito.core.general.mapper.SucursalMapper;
import com.banquito.core.general.modelo.Sucursal;
import com.banquito.core.general.servicio.EntidadBancariaSucursalServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/banco")
@Tag(name = "Entidades y Sucursales", description = "Gestiona las Entidades Bancarias y sus Sucursales")
public class EntidadBancariaSucursalControlador {

    private final EntidadBancariaSucursalServicio servicio;
    private final EntidadBancariaMapper entidadMapper;
    private final SucursalMapper sucursalMapper;

    public EntidadBancariaSucursalControlador(EntidadBancariaSucursalServicio servicio, EntidadBancariaMapper entidadMapper, SucursalMapper sucursalMapper) {
        this.servicio = servicio;
        this.entidadMapper = entidadMapper;
        this.sucursalMapper = sucursalMapper;
    }

    @PostMapping
    @Operation(summary = "Crear una nueva Entidad Bancaria", description = "Crea una nueva entidad bancaria en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Entidad creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<EntidadBancariaDTO> crearEntidad(@Valid @RequestBody EntidadBancariaCreacionDTO dto) {
        return ResponseEntity.ok(entidadMapper.toDTO(servicio.crearEntidad(dto)));
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

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar el estado de una Entidad Bancaria", description = "Cambia el estado de una entidad bancaria a ACTIVO o INACTIVO. Si se inactiva, todas sus sucursales también se inactivarán.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Estado cambiado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Entidad no encontrada")
    })
    public ResponseEntity<Void> cambiarEstadoEntidad(@PathVariable String id, @RequestParam EstadoGeneralEnum estado) {
        servicio.cambiarEstadoEntidadBancaria(id, estado);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{entidadId}/sucursales")
    @Operation(summary = "Crear una nueva Sucursal para una Entidad", description = "Crea una nueva sucursal y la asocia a una entidad bancaria existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucursal creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Entidad Bancaria o Locación no encontrada")
    })
    public ResponseEntity<SucursalDTO> crearSucursal(@PathVariable String entidadId, @Valid @RequestBody SucursalCreacionDTO dto) {
        return ResponseEntity.ok(sucursalMapper.toDTO(servicio.crearSucursal(entidadId, dto)));
    }

    @PutMapping("/sucursales/{id}")
    @Operation(summary = "Modificar datos de una Sucursal", description = "Modifica los datos permitidos de una sucursal existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucursal modificada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    public ResponseEntity<SucursalDTO> modificarSucursal(@PathVariable String id, @Valid @RequestBody SucursalUpdateDTO dto) {
        return ResponseEntity.ok(sucursalMapper.toDTO(servicio.modificarSucursal(id, dto)));
    }

    @PatchMapping("/sucursales/{id}/estado")
    @Operation(summary = "Cambiar el estado de una Sucursal", description = "Cambia el estado de una sucursal a ACTIVO o INACTIVO.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Estado cambiado exitosamente"),
            @ApiResponse(responseCode = "400", description = "No se puede activar la sucursal si su entidad o locación están inactivas"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    public ResponseEntity<Void> cambiarEstadoSucursal(@PathVariable String id, @RequestParam EstadoSucursalesEnum estado) {
        servicio.cambiarEstadoSucursal(id, estado);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sucursales/locacion/{locacionId}")
    @Operation(summary = "Listar sucursales activas por locación", description = "Obtiene una lista de todas las sucursales activas para un ID de locación geográfica específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")
    })
    public ResponseEntity<List<SucursalDTO>> listarSucursalesPorLocacion(@PathVariable String locacionId) {
        List<Sucursal> sucursales = servicio.listarSucursalesActivasPorLocacion(locacionId);
        return ResponseEntity.ok(sucursales.stream().map(sucursalMapper::toDTO).collect(Collectors.toList()));
    }
}
