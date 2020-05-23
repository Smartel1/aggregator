package ru.smartel.aggregator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.smartel.aggregator.dto.NewsDto;
import ru.smartel.aggregator.service.NewsService;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @GetMapping
    public List<NewsDto> getNews(@RequestParam(required = false, defaultValue = "") String query) {
        return newsService.getAllByTitleSubstring(query);
    }
}
