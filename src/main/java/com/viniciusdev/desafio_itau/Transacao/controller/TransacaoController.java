package com.viniciusdev.desafio_itau.Transacao.controller;

import com.viniciusdev.desafio_itau.Transacao.dto.TransacaoDTO;
import com.viniciusdev.desafio_itau.Transacao.repository.TransacaoRepository;
import com.viniciusdev.desafio_itau.Transacao.service.TransacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/transacao")
public class TransacaoController {

    private static final Logger logger = LoggerFactory.getLogger(TransacaoController.class);

    private final TransacaoService transacaoService;

    private final TransacaoRepository transacaoRepository;

    public TransacaoController(TransacaoService transacaoService, TransacaoRepository transacaoRepository) {
        this.transacaoService = transacaoService;
        this.transacaoRepository = transacaoRepository;
    }

    @PostMapping
    public ResponseEntity adicionar(@RequestBody TransacaoDTO transacao){
        try {
            // Normalize timestamp: if null, in the future or older than 60s, replace with server 'now'
            java.time.OffsetDateTime now = java.time.OffsetDateTime.now();
            java.time.OffsetDateTime cutoff = now.minusSeconds(60);
            TransacaoDTO toSave = transacao;
            if (transacao.dataHora() == null || transacao.dataHora().isAfter(now) || transacao.dataHora().isBefore(cutoff)) {
                toSave = new TransacaoDTO(transacao.valor(), now);
                logger.info("[TransacaoController] normalized timestamp to now for incoming request: {}", toSave);
            }

            transacaoService.validarTransacao(toSave);
            transacaoRepository.salvarDados(toSave);
            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (IllegalArgumentException exception){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // NOTE: debug endpoint removed. Use POST /transacao with appropriate payloads for production.

    @DeleteMapping
    public ResponseEntity deletar() {
        transacaoRepository.deletarDados();
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @GetMapping
    public ResponseEntity<java.util.List<TransacaoDTO>> listar() {
        return ResponseEntity.ok(transacaoRepository.buscarTodos());
    }

    @GetMapping("/recentes")
    public ResponseEntity<java.util.List<TransacaoDTO>> listarRecentes() {
        java.time.OffsetDateTime now = java.time.OffsetDateTime.now();
        java.time.OffsetDateTime cutoff = now.minusSeconds(60);

        var recentes = transacaoRepository.buscarTodos()
                .stream()
                .filter(t -> t.dataHora() != null && !t.dataHora().isBefore(cutoff))
                .toList();

        return ResponseEntity.ok(recentes);
    }
}
