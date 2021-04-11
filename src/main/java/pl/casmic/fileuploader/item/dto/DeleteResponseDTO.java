package pl.casmic.fileuploader.item.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"success", "message"})
public class DeleteResponseDTO {

    private boolean success;
    private String message;
}
