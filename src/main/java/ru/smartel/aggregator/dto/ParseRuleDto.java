package ru.smartel.aggregator.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@Builder
public class ParseRuleDto {
    @NotNull
    SourceType sourceType;
    String elementSelector; // css-селектор для нахождения списка новостей
    String titleSelector; // css-селектор для нахождения заголовка новости (относительно элемента новости)
    String descriptionSelector; // css-селектор для нахождения описания новости (относительно элемента новости)
    String imageSelector; // css-селектор для нахождения изображения (относительно элемента новости)
    String linkSelector; // css-селектор для нахождения ссылки (относительно элемента новости)

    public enum SourceType {
        RSS, HTML;
    }
}
