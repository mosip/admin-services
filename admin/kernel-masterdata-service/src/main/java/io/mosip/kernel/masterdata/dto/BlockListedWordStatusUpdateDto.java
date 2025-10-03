package io.mosip.kernel.masterdata.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.mosip.kernel.masterdata.validator.AlphabeticValidator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlockListedWordStatusUpdateDto {

    @NotNull
    @Size(min = 1, max = 128)
    @AlphabeticValidator(message = "Blocklisted word cannot contain numbers and special characters")
    private String word;

    @NotNull
    private Boolean isActive;
}
