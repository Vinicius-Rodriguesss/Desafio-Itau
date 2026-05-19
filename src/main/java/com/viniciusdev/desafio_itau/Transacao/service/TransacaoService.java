package com.viniciusdev.desafio_itau.Transacao.service;

import com.viniciusdev.desafio_itau.Transacao.dto.TransacaoDTO;
import com.viniciusdev.desafio_itau.Transacao.model.Transacao;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
public class TransacaoService {

    public void validarTransacao(TransacaoDTO trasancaoRequest){
        // Convertendo meu DTO
        Transacao transacao = new Transacao(
                trasancaoRequest.valor(),
                trasancaoRequest.dataHora()
        );


        // Primeiro, validações de nulidade para evitar NullPointerException
        if (transacao.getValor() == null) {
            throw new RuntimeException("Valor obrigatório");
        }
        if (transacao.getDataHora() == null){
            throw new RuntimeException("A data e hora é obrigatório");
        }

        // Agora validações de conteúdo
        if (transacao.getValor().compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Erro: transação devem ter o valor maior que zero");
        }
        if(transacao.getDataHora().isAfter(OffsetDateTime.now())){
            throw new IllegalArgumentException("Erro: na data da transação");
        }

    }

}
