package com.example.manager.domain.dto.stub;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadStubInitDTO {
    private String fileName;
    private String fileSize;
    private String chunkSize;
    private String fileHash;
}
