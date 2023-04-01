package ru.mephi.knowledgechecker.dto.telegram.outcome;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.mephi.knowledgechecker.common.TextType;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageEntity {
    private String type;
    private Integer offset;
    private Integer length;
    private String language;

    public MessageEntity(TextType type, Integer offset, Integer length) {
        this.type = type.name().toLowerCase();
        this.offset = offset;
        this.length = length;
    }
}
