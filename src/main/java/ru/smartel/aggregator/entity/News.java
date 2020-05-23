package ru.smartel.aggregator.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "news")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class News extends BaseEntity {
    @Id
    @Builder.Default
    UUID id = UUID.randomUUID();
    String title;
    String imageUrl;
    @Column(columnDefinition = "TEXT")
    String description;
    String link;
}
