package com.viniciusdev.desafio_itau.Estatistica.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstatisticaModel {

    private long count;
    private Double avg;
    private Double max;
    private Double min;
    private Double sum;
}
