package ru.smartel.aggregator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smartel.aggregator.dto.SourceDto;
import ru.smartel.aggregator.entity.Source;
import ru.smartel.aggregator.repository.SourceRepository;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@Transactional
@RequiredArgsConstructor
public class SourceService {
    private final SourceRepository repository;

    public void add(SourceDto source) {
        var sourceEntityBuilder = Source.builder()
                .url(source.getUrl())
                .sourceType(source.getParseRule().getSourceType());

        switch (source.getParseRule().getSourceType()) {
            case RSS:
                break;
            case HTML:
                sourceEntityBuilder = sourceEntityBuilder
                        .elementSelector(source.getParseRule().getElementSelector())
                        .titleSelector(source.getParseRule().getTitleSelector())
                        .descriptionSelector(source.getParseRule().getDescriptionSelector())
                        .imageSelector(source.getParseRule().getImageSelector())
                        .linkSelector(source.getParseRule().getLinkSelector());
                break;
            default: throw new IllegalArgumentException("Возможна обработка только RSS и HTML");
        }

        repository.save(sourceEntityBuilder.build());
    }

    public List<SourceDto> getAll() {
        return repository.findAll().stream()
                .map(SourceDto::from)
                .collect(Collectors.toList());
    }
}
