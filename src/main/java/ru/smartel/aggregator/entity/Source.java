package ru.smartel.aggregator.entity;

import lombok.*;
import ru.smartel.aggregator.dto.ParseRuleDto;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "source")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Source extends BaseEntity {
    @Id
    @Builder.Default
    UUID id = UUID.randomUUID();
    String url;
    @Enumerated(EnumType.STRING)
    ParseRuleDto.SourceType sourceType;
    String elementSelector; // css-селектор для нахождения списка новостей
    String titleSelector; // css-селектор для нахождения заголовка новости (относительно элемента новости)
    String descriptionSelector; // css-селектор для нахождения описания новости (относительно элемента новости)
    String imageSelector; // css-селектор для нахождения изображения (относительно элемента новости)
    String linkSelector; // css-селектор для нахождения ссылки (относительно элемента новости)
}
