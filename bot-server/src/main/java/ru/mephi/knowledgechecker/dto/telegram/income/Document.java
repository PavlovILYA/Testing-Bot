package ru.mephi.knowledgechecker.dto.telegram.income;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Document {
    @JsonProperty(value = "file_id")
    private String fileId;
    @JsonProperty(value = "file_unique_id")
    private String fileUniqueId;
    @JsonProperty(value = "file_name")
    private String fileName;
    @JsonProperty(value = "mime_type")
    private String mimeType;
    @JsonProperty(value = "file_size")
    private Long fileSize;
}
