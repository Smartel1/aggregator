package ru.smartel.aggregator;


import org.jsoup.Jsoup;
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
import ru.smartel.aggregator.parser.HtmlNewsParser;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class HtmlParserTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private HtmlNewsParser.ConnectionFactory connectionFactory;
    @InjectMocks
    private HtmlNewsParser sut;


    @ParameterizedTest
    @MethodSource("suitableTestSource")
    public void testSuitable(ParseRuleDto.SourceType type, Boolean expectedResult) {
        var source = SourceDto.builder().parseRule(ParseRuleDto.builder().sourceType(type).build()).build();
        assertThat(sut.suitable(source)).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> suitableTestSource() {
        return Stream.of(
                Arguments.of(ParseRuleDto.SourceType.RSS, false),
                Arguments.of(ParseRuleDto.SourceType.HTML, true),
                Arguments.of(null, false)
        );
    }

    @ParameterizedTest
    @MethodSource("parseTestSource")
    public void whenParse_thenReturnNews(String html, ParseRuleDto rule, List<NewsDto> expectedNews) throws IOException {
        var source = SourceDto.builder()
                .url("http://someUrl")
                .parseRule(rule)
                .build();

        given(connectionFactory.getConnection(any()).get()).willReturn(Jsoup.parse(html));
        assertThat(sut.parse(source)).containsExactlyInAnyOrderElementsOf(expectedNews);
    }

    private static Stream<Arguments> parseTestSource() {
        var news1 = NewsDto.builder()
                .title("title").description("description").link("http://someUrl/link").imageUrl("http://someUrl/img")
                .build();
        var news2 = NewsDto.builder()
                .title("title2").description("description2").link("http://someUrl/link2").imageUrl("http://someUrl/img2")
                .build();
        var rule = ParseRuleDto.builder()
                .elementSelector(".elem")
                .titleSelector("h1")
                .descriptionSelector("div p")
                .imageSelector("img")
                .linkSelector("a")
                .build();

        var html = "<html><body>" +
                "<div class=\"elem\"><h1>title</h1><p>description<img src=\"/img\"/></p><a href=\"/link\"/></div></body>" +
                "<div class=\"elem\"><h1>title2</h1><p>description2<img src=\"/img2\"/></p><a href=\"/link2\"/></div>" +
                "</body></html>";

        return Stream.of(
                Arguments.of(html, rule, List.of(news1, news2)),
                Arguments.of("", rule, List.of())
        );
    }
}
