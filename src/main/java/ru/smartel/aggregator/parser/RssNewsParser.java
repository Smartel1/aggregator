package ru.smartel.aggregator.parser;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.smartel.aggregator.dto.NewsDto;
import ru.smartel.aggregator.dto.SourceDto;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.smartel.aggregator.dto.ParseRuleDto.SourceType.RSS;

@Service
@Slf4j
@RequiredArgsConstructor
public class RssNewsParser implements NewsParser {
    private final SyndFeedInput syndFeedInput;
    private final ReaderFactory readerFactory;

    @Override
    public boolean suitable(SourceDto sourceDto) {
        return RSS.equals(sourceDto.getParseRule().getSourceType());
    }

    @Override
    public List<NewsDto> parse(SourceDto sourceDto) {
        try {
            URL feedSource = new URL(sourceDto.getUrl());
            return syndFeedInput.build(readerFactory.getReader(feedSource)).getEntries().stream()
                    .map(this::entryToNews)
                    .collect(toList());
        } catch (FeedException | IOException e) {
            log.warn("Error while parsing RSS feed {}", sourceDto.getUrl(), e);
            return Collections.emptyList();
        }
    }

    private NewsDto entryToNews(SyndEntry entry) {
        return NewsDto.builder()
                .title(entry.getTitle())
                .description(entry.getDescription().getValue())
                .link(entry.getLink())
                .build();
    }

    @Service
    public static class ReaderFactory {
        public XmlReader getReader(URL feedSource) throws IOException {
            return new XmlReader(feedSource);
        }
    }
}
