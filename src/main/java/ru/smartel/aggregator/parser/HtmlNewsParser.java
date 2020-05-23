package ru.smartel.aggregator.parser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import ru.smartel.aggregator.dto.NewsDto;
import ru.smartel.aggregator.dto.SourceDto;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.smartel.aggregator.dto.ParseRuleDto.SourceType.HTML;

/**
 * Этот парсер извлекает данные из html-файла.
 * Новости на странице находятся с помощью css-селекторов.
 * Затем в каждой новости находится заголовок и описание (извлекается текст элемента),
 * ссылки на изображение и на новость (извлекаются src и href соответственно).
 * Ссылки преобразуются в абсолютные путём добавления адреса сайта
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class HtmlNewsParser implements NewsParser {
    private final ConnectionFactory connectionFactory;

    @Override
    public boolean suitable(SourceDto sourceDto) {
        return HTML.equals(sourceDto.getParseRule().getSourceType());
    }

    @Override
    public List<NewsDto> parse(SourceDto sourceDto) {
        if (isNull(sourceDto.getUrl()) || isNull(sourceDto.getParseRule().getElementSelector())){
            return Collections.emptyList();
        }
        try {
            var document = connectionFactory.getConnection(sourceDto.getUrl()).get();
            return document.select(sourceDto.getParseRule().getElementSelector()).stream()
                    .map(element -> parse(sourceDto, element))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.warn("Cannot connect to {}", sourceDto.getUrl());
            return Collections.emptyList();
        }
    }

    private NewsDto parse(SourceDto sourceDto, Element element) {
        var siteUrl = sourceDto.getUrl();
        var newsBuilder = NewsDto.builder();

        if (nonNull(sourceDto.getParseRule().getTitleSelector())) {
            newsBuilder = newsBuilder
                    .title(element.select(sourceDto.getParseRule().getTitleSelector()).text());
        }
        if (nonNull(sourceDto.getParseRule().getTitleSelector())) {
            newsBuilder = newsBuilder
                    .description(element.select(sourceDto.getParseRule().getDescriptionSelector()).text());
        }
        if (nonNull(sourceDto.getParseRule().getImageSelector())) {
            newsBuilder = newsBuilder
                    .imageUrl(toAbsoluteUrl(
                            siteUrl, element.select(sourceDto.getParseRule().getImageSelector()).attr("src")));
        }
        if (nonNull(sourceDto.getParseRule().getLinkSelector())) {
            newsBuilder = newsBuilder
                    .link(toAbsoluteUrl(
                            siteUrl, element.select(sourceDto.getParseRule().getLinkSelector()).attr("href")));
        }
        return newsBuilder.build();
    }

    private String toAbsoluteUrl(String siteUri, String resourceUri) {
        URI resource = URI.create(resourceUri);
        if (resource.isAbsolute()) {
            return resource.toString();
        }
        return URI.create(siteUri).resolve(resource).toString();
    }

    @Service
    public static class ConnectionFactory {
        public Connection getConnection(String sourceUrl) {
            return Jsoup.connect(sourceUrl);
        }
    }
}
