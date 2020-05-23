package ru.smartel.aggregator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smartel.aggregator.dto.NewsDto;
import ru.smartel.aggregator.dto.SourceDto;
import ru.smartel.aggregator.entity.News;
import ru.smartel.aggregator.parser.NewsParser;
import ru.smartel.aggregator.repository.NewsRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository repository;
    private final SourceService sourceService;
    private final List<NewsParser> newsParsers;

    public List<NewsDto> getAllByTitleSubstring(String substring) {
        return repository.findAllByTitleContaining(substring).stream()
                .map(NewsDto::from)
                .collect(toList());
    }

    /**
     * Парсить новости из источников через определенные промежутки времени (5 минут по-умолчанию)
     */
    @Scheduled(fixedRateString = "${poll-rate:300000}")
    void synchronize() {
        sourceService.getAll().forEach(this::parseAndSave);
    }

    /**
     * Извлечь новости из источника и сохранить в базу
     *
     * @param sourceDto источник
     */
    private void parseAndSave(SourceDto sourceDto) {
        for (var newsParser : newsParsers) {
            if (newsParser.suitable(sourceDto)) {
                var news = newsParser.parse(sourceDto);
                save(news);
                return;
            }
        }
    }

    /**
     * Сохраняет в базу только те новости, заголовки которых ещё не сохранены
     * (наивная мера для избежания дублирования новостей)
     *
     * @param news новости для сохранения
     */
    private void save(List<NewsDto> news) {
        repository.saveAll(news.stream()
                .filter(n -> !repository.existsByTitle(n.getTitle()))
                .map(n -> News.builder()
                        .title(n.getTitle())
                        .imageUrl(n.getImageUrl())
                        .description(n.getDescription())
                        .link(n.getLink())
                        .build())
                .collect(toList()));
    }
}
