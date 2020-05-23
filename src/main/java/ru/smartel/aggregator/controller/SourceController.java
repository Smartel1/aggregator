package ru.smartel.aggregator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.smartel.aggregator.dto.SourceDto;
import ru.smartel.aggregator.service.SourceService;

@RestController
@RequestMapping("/api/sources")
@RequiredArgsConstructor
public class SourceController {
    private final SourceService sourceService;

    @PostMapping
    public void add(@Validated @RequestBody SourceDto source) {
        sourceService.add(source);
    }
}
