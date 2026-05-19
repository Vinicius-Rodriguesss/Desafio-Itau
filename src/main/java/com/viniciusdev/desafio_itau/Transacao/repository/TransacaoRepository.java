package com.viniciusdev.desafio_itau.Transacao.repository;

import com.viniciusdev.desafio_itau.Transacao.dto.TransacaoDTO;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@Repository
public class TransacaoRepository {

    // Use a thread-safe list implementation to avoid concurrent modification issues
    private final java.util.List<TransacaoDTO> listaDeTransacoes = new java.util.concurrent.CopyOnWriteArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(TransacaoRepository.class);

    // Salvar os dados na lista
    public void salvarDados(TransacaoDTO trasancaoRequest){
        // Optional: normalize null timestamp to server time
        if (trasancaoRequest.dataHora() == null) {
            var normalized = new TransacaoDTO(trasancaoRequest.valor(), java.time.OffsetDateTime.now());
            listaDeTransacoes.add(normalized);
            logger.info("[TransacaoRepository] Saved (normalized timestamp): {}", normalized);
        } else {
            listaDeTransacoes.add(trasancaoRequest);
            logger.info("[TransacaoRepository] Saved: {}", trasancaoRequest);
        }
        // Do not clean here; cleaning runs periodically to avoid removing recently added items
    }
    // Remove transações com mais de 60s
    @org.springframework.scheduling.annotation.Scheduled(fixedRate = 1000)
    public void limparDados(){
        var cutoff = java.time.OffsetDateTime.now().minusSeconds(60);
        int before = listaDeTransacoes.size();
        listaDeTransacoes.removeIf(t -> t.dataHora() == null || t.dataHora().isBefore(cutoff));
        int after = listaDeTransacoes.size();
        if (before != after) {
            logger.info("[TransacaoRepository] cleaned: removed {} old transactions, remaining={}", (before - after), after);
        }
    }
    // Apagar todos os dados
    public void deletarDados(){
        listaDeTransacoes.clear();
    }
    public List<TransacaoDTO> buscarTodos() {
        return listaDeTransacoes;
    }

}
