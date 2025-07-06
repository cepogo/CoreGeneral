package com.banquito.core.general.servicio;

import com.banquito.core.general.dto.MonedaDTO;
import com.banquito.core.general.dto.PaisDTO;
import com.banquito.core.general.excepcion.CrearEntidadException;
import com.banquito.core.general.excepcion.EntidadNoEncontradaException;
import com.banquito.core.general.mapper.MonedaMapper;
import com.banquito.core.general.mapper.PaisMapper;
import com.banquito.core.general.modelo.Moneda;
import com.banquito.core.general.modelo.Pais;
import com.banquito.core.general.repositorio.MonedaRepositorio;
import com.banquito.core.general.repositorio.PaisRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
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
        if (paisRepositorio.findByCodigoPais(paisDTO.getCodigoPais()) != null) {
            throw new CrearEntidadException("Pais", "Ya existe un país con el código: " + paisDTO.getCodigoPais());
        }
        Pais pais = paisMapper.toEntity(paisDTO);
        if (paisDTO.getCodigoMoneda() != null) {
            Moneda moneda = monedaRepositorio.findByCodigoMoneda(paisDTO.getCodigoMoneda());
            if (moneda == null) {
                throw new EntidadNoEncontradaException("Moneda no encontrada con código: " + paisDTO.getCodigoMoneda(), 2, "Moneda");
            }
            pais.setMoneda(monedaMapper.toDTO(moneda));
        }

        pais.setEstado("ACTIVO");
        pais.setVersion(1L);
        return paisMapper.toDTO(paisRepositorio.save(pais));
    }

    public List<PaisDTO> listarPaisesPorEstado(String estado) {
        return paisRepositorio.findByEstado(estado).stream()
                .map(paisMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PaisDTO agregarMonedaAPais(String codigoPais, String codigoMoneda) {
        Pais pais = paisRepositorio.findByCodigoPais(codigoPais);
        if (pais == null) {
            throw new EntidadNoEncontradaException("País no encontrado con código: " + codigoPais, 2, "Pais");

        }
        Moneda moneda = monedaRepositorio.findByCodigoMoneda(codigoMoneda);
        if (moneda == null) {
            throw new EntidadNoEncontradaException("Moneda no encontrada con código: " + codigoMoneda, 2, "Moneda");
        }
        pais.setMoneda(monedaMapper.toDTO(moneda));
        pais.setVersion(pais.getVersion() != null ? pais.getVersion() + 1 : 1L);
        return paisMapper.toDTO(paisRepositorio.save(pais));
    }

    @Transactional
    public void cambiarEstadoPais(String codigoPais, String nuevoEstado) {
        Pais pais = paisRepositorio.findByCodigoPais(codigoPais);
        if (pais == null) {
            throw new EntidadNoEncontradaException("País no encontrado con código: " + codigoPais, 2, "Pais");
        }
        pais.setEstado(nuevoEstado);
        pais.setVersion(pais.getVersion() != null ? pais.getVersion() + 1 : 1L);
        paisRepositorio.save(pais);
    }

    @Transactional
    public MonedaDTO crearMoneda(MonedaDTO monedaDTO) {

        if (monedaRepositorio.findByCodigoMoneda(monedaDTO.getCodigoMoneda()) != null) {
            throw new CrearEntidadException("Moneda", "Ya existe una moneda con el código: " + monedaDTO.getCodigoMoneda());
        }
        
        Moneda moneda = monedaMapper.toEntity(monedaDTO);
        moneda.setEstado("ACTIVO");
        moneda.setVersion(1L);
        
        return monedaMapper.toDTO(monedaRepositorio.save(moneda));
    }

    public List<MonedaDTO> listarMonedas() {
        return monedaRepositorio.findAll().stream()
                .map(monedaMapper::toDTO)
                .collect(Collectors.toList());
    }
    public MonedaDTO obtenerMonedaPorCodigo(String codigoMoneda) {
            Moneda moneda = monedaRepositorio.findByCodigoMoneda(codigoMoneda);
            if (moneda == null) {
                throw new EntidadNoEncontradaException("Moneda no encontrada con código: " + codigoMoneda, 2, "Moneda");
            }
            return monedaMapper.toDTO(moneda);
        }

    public List<MonedaDTO> listarMonedasPorEstado(String estado) {
        return monedaRepositorio.findByEstado(estado).stream()
                .map(monedaMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Transactional
    public void cambiarEstadoMoneda(String codigoMoneda, String nuevoEstado) {
        Moneda moneda = monedaRepositorio.findByCodigoMoneda(codigoMoneda);
        if (moneda == null) {
            throw new EntidadNoEncontradaException("Moneda no encontrada con código: " + codigoMoneda, 2, "Moneda");
        }


        moneda.setEstado(nuevoEstado);
        moneda.setVersion(moneda.getVersion() != null ? moneda.getVersion() + 1 : 1L);
        monedaRepositorio.save(moneda);
    }
} 