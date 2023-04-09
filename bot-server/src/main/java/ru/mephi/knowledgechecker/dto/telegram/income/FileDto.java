package ru.mephi.knowledgechecker.dto.telegram.income;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileDto {
    @JsonProperty(value = "file_id")
    private String fileId;
    @JsonProperty(value = "file_unique_id")
    private String mimeType;
    @JsonProperty(value = "file_size")
    private Long fileSize;
    @JsonProperty(value = "file_path")
    private String filePath;
}
