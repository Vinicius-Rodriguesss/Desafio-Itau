package com.viniciusdev.desafio_itau.Estatistica.dto;

public record EstatisticaDTO(
        long count,
        Double avg,
        Double max,
        Double min,
        Double sum
) {
}