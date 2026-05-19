package com.viniciusdev.desafio_itau.Transacao.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TransacaoDTO(BigDecimal valor,
                           OffsetDateTime dataHora) {
}
