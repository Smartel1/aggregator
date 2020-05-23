package ru.smartel.aggregator.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class NewsDto {
    String title;
    String description;
    String imageUrl;
    String link;

    public static NewsDto from(ru.smartel.aggregator.entity.News news) {
        return new NewsDto(news.getTitle(), news.getImageUrl(), news.getDescription(), news.getLink());
    }
}
