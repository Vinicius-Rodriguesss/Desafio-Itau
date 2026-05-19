package com.viniciusdev.desafio_itau.Estatistica.Service;

import com.viniciusdev.desafio_itau.Estatistica.dto.EstatisticaDTO;
import com.viniciusdev.desafio_itau.Transacao.repository.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EstatisticaService {

    private final TransacaoRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(EstatisticaService.class);

    public EstatisticaService(
            TransacaoRepository repository
    ) {
        this.repository = repository;
    }

    public EstatisticaDTO gerarEstatistica() {

        // Capture current time once to avoid tiny timing issues during the stream processing
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime cutoff = now.minusSeconds(60);

        // Collect all transactions and filter explicitly so we can inspect counts for debugging
        List<?> all = repository.buscarTodos();
        long total = all.size();

        var recent = repository.buscarTodos()
                .stream()
                .filter(t -> t.dataHora() != null && !t.dataHora().isBefore(cutoff))
                .collect(Collectors.toList());

        long recentCount = recent.size();

        // Log debug info
        logger.info("[EstatisticaService] now={} cutoff={} total={} recent={}", now, cutoff, total, recentCount);

        var summary = recent
                .stream()
                .mapToDouble(t -> t.valor().doubleValue())
                .summaryStatistics();

        return summary.getCount() == 0
                ? new EstatisticaDTO(
                0,
                0.0,
                0.0,
                0.0,
                0.0
        )
                : new EstatisticaDTO(
                summary.getCount(),
                summary.getAverage(),
                summary.getMax(),
                summary.getMin(),
                summary.getSum()
        );
    }
}