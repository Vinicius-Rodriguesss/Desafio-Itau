package com.viniciusdev.desafio_itau.Transacao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transacao {

    private BigDecimal valor;
    private OffsetDateTime dataHora;
}
