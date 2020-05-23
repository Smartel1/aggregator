package ru.smartel.aggregator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.smartel.aggregator.entity.Source;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SourceDto {
    @NotBlank
    String url;
    @Valid
    @NotNull
    ParseRuleDto parseRule;

    public static SourceDto from(Source source) {
        return SourceDto.builder()
                .url(source.getUrl())
                .parseRule(ParseRuleDto.builder()
                        .sourceType(source.getSourceType())
                        .elementSelector(source.getElementSelector())
                        .titleSelector(source.getTitleSelector())
                        .descriptionSelector(source.getDescriptionSelector())
                        .imageSelector(source.getImageSelector())
                        .linkSelector(source.getLinkSelector())
                        .build())
                .build();
    }
}
