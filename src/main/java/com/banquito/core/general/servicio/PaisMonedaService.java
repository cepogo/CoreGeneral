package com.banquito.core.general.servicio;

import com.banquito.core.general.dto.MonedaDTO;
import com.banquito.core.general.dto.PaisDTO;
import com.banquito.core.general.mapper.MonedaMapper;
import com.banquito.core.general.mapper.PaisMapper;
import com.banquito.core.general.modelo.Moneda;
import com.banquito.core.general.modelo.Pais;
import com.banquito.core.general.repositorio.MonedaRepositorio;
import com.banquito.core.general.repositorio.PaisRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaisMonedaService {
    private final PaisRepositorio paisRepositorio;
    private final MonedaRepositorio monedaRepositorio;
    private final PaisMapper paisMapper;
    private final MonedaMapper monedaMapper;

    public PaisMonedaService(PaisRepositorio paisRepositorio, MonedaRepositorio monedaRepositorio, PaisMapper paisMapper, MonedaMapper monedaMapper) {
        this.paisRepositorio = paisRepositorio;
        this.monedaRepositorio = monedaRepositorio;
        this.paisMapper = paisMapper;
        this.monedaMapper = monedaMapper;
    }

    @Transactional
    public PaisDTO crearPais(PaisDTO paisDTO) {
        if (paisRepositorio.findByCodigo(paisDTO.getCodigo()) != null) {
            throw new RuntimeException("Ya existe un país con el código: " + paisDTO.getCodigo());
        }
        Pais pais = paisMapper.toEntity(paisDTO);
        pais.setEstado("ACTIVO");
        pais.setVersion(1L);
        return paisMapper.toDTO(paisRepositorio.save(pais));
    }

    public List<PaisDTO> listarPaises() {
        return paisRepositorio.findAll().stream().map(paisMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public PaisDTO actualizarPais(String id, PaisDTO paisDTO) {
        Pais pais = paisRepositorio.findById(id).orElseThrow(() -> new RuntimeException("País no encontrado"));
        Optional.ofNullable(paisDTO.getNombre()).ifPresent(pais::setNombre);
        Optional.ofNullable(paisDTO.getCodigoTelefono()).ifPresent(pais::setCodigoTelefono);
        pais.setVersion(pais.getVersion() != null ? pais.getVersion() + 1 : 1L);
        return paisMapper.toDTO(paisRepositorio.save(pais));
    }

    @Transactional
    public PaisDTO cambiarEstadoPais(String id, String nuevoEstado) {
        Pais pais = paisRepositorio.findById(id).orElseThrow(() -> new RuntimeException("País no encontrado"));
        pais.setEstado(nuevoEstado);
        pais.setVersion(pais.getVersion() != null ? pais.getVersion() + 1 : 1L);
        return paisMapper.toDTO(paisRepositorio.save(pais));
    }

    @Transactional
    public MonedaDTO crearMoneda(String idPais, MonedaDTO monedaDTO) {
        Pais pais = paisRepositorio.findById(idPais).orElseThrow(() -> new RuntimeException("País no encontrado"));
        if (monedaRepositorio.findByCodigo(monedaDTO.getCodigo()) != null) {
            throw new RuntimeException("Ya existe una moneda con el código: " + monedaDTO.getCodigo());
        }
        Moneda moneda = monedaMapper.toEntity(monedaDTO);
        moneda.setPais(paisMapper.toDTO(pais));
        moneda.setEstado("ACTIVO");
        moneda.setVersion(1L);
        return monedaMapper.toDTO(monedaRepositorio.save(moneda));
    }

    public List<MonedaDTO> listarMonedas() {
        return monedaRepositorio.findAll().stream().map(monedaMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public MonedaDTO actualizarMoneda(String id, MonedaDTO monedaDTO) {
        Moneda moneda = monedaRepositorio.findById(id).orElseThrow(() -> new RuntimeException("Moneda no encontrada"));
        Optional.ofNullable(monedaDTO.getNombre()).ifPresent(moneda::setNombre);
        Optional.ofNullable(monedaDTO.getSimbolo()).ifPresent(moneda::setSimbolo);
        moneda.setVersion(moneda.getVersion() != null ? moneda.getVersion() + 1 : 1L);
        return monedaMapper.toDTO(monedaRepositorio.save(moneda));
    }

    @Transactional
    public MonedaDTO cambiarEstadoMoneda(String id, String nuevoEstado) {
        Moneda moneda = monedaRepositorio.findById(id).orElseThrow(() -> new RuntimeException("Moneda no encontrada"));
        moneda.setEstado(nuevoEstado);
        moneda.setVersion(moneda.getVersion() != null ? moneda.getVersion() + 1 : 1L);
        return monedaMapper.toDTO(monedaRepositorio.save(moneda));
    }
} 