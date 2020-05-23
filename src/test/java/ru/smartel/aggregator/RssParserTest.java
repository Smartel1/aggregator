package ru.smartel.aggregator;


import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.smartel.aggregator.dto.NewsDto;
import ru.smartel.aggregator.dto.ParseRuleDto;
import ru.smartel.aggregator.dto.SourceDto;
import ru.smartel.aggregator.parser.RssNewsParser;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class RssParserTest {
    @Mock
    private RssNewsParser.ReaderFactory readerFactory;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private SyndFeedInput syndFeed;
    @InjectMocks
    private RssNewsParser sut;


    @ParameterizedTest
    @MethodSource("suitableTestSource")
    public void testSuitable(ParseRuleDto.SourceType type, Boolean expectedResult) {
        var source = SourceDto.builder().parseRule(ParseRuleDto.builder().sourceType(type).build()).build();
        assertThat(sut.suitable(source)).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> suitableTestSource() {
        return Stream.of(
                Arguments.of(ParseRuleDto.SourceType.RSS, true),
                Arguments.of(ParseRuleDto.SourceType.HTML, false),
                Arguments.of(null, false)
        );
    }

    @ParameterizedTest
    @MethodSource("parseTestSource")
    public void whenParse_thenReturnNews(List<SyndEntry> entries, List<NewsDto> expectedNews) throws FeedException {
        given(syndFeed.build(any(XmlReader.class)).getEntries()).willReturn(entries);
        assertThat(sut.parse(SourceDto.builder().url("http://someUrl").build())).containsExactlyInAnyOrderElementsOf(expectedNews);
    }

    private static Stream<Arguments> parseTestSource() {
        var content = new SyndContentImpl();
        content.setValue("description");

        var entry1 = new SyndEntryImpl();
        entry1.setTitle("title");
        entry1.setDescription(content);
        entry1.setLink("link");

        var news1 = NewsDto.builder().title("title").description("description").link("link").build();

        var content2 = new SyndContentImpl();
        content2.setValue("description2");

        var entry2 = new SyndEntryImpl();
        entry2.setTitle("title2");
        entry2.setDescription(content2);
        entry2.setLink("link2");

        var news2 = NewsDto.builder().title("title2").description("description2").link("link2").build();

        return Stream.of(
                Arguments.of(List.of(entry1, entry2), List.of(news1, news2)),
                Arguments.of(List.of(), List.of())
        );
    }
}
