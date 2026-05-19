package com.viniciusdev.desafio_itau.Estatistica.Controller;

import com.viniciusdev.desafio_itau.Estatistica.Service.EstatisticaService;
import com.viniciusdev.desafio_itau.Estatistica.dto.EstatisticaDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EstatisticaController {

    private final EstatisticaService service;

    public EstatisticaController(
            EstatisticaService service
    ) {
        this.service = service;
    }

    @GetMapping("/estatistica")
    public ResponseEntity<EstatisticaDTO> buscarEstatistica() {

        return ResponseEntity.ok(
                service.gerarEstatistica()
        );

    }

}