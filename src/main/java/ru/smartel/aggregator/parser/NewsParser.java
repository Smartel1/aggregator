package ru.smartel.aggregator.parser;

import ru.smartel.aggregator.dto.NewsDto;
import ru.smartel.aggregator.dto.SourceDto;
import ru.smartel.aggregator.entity.News;

import java.util.List;

public interface NewsParser {
    /**
     * Определить, подходит ли данная реализация парсера для указанного источника
     *
     * @param sourceDto источник новости
     * @return true, если парсер может обработать источник
     */
    boolean suitable(SourceDto sourceDto);

    /**
     * Извлечь из источника список новостей
     *
     * @param sourceDto источник новости
     * @return список новостей, которые удалось извлечь из источника. Пустой список, если не удалось извлечь
     */
    List<NewsDto> parse(SourceDto sourceDto);
}
