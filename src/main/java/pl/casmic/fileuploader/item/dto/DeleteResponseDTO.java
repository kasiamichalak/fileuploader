package pl.casmic.fileuploader.item.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({"success", "message"})
public class DeleteResponseDTO {

    private boolean success;
    private String message;
}
