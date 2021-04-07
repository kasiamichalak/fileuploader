package pl.casmic.fileuploader.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({"success", "itemDTO"})
public class UploadResponseDTO {

    private boolean success = false;
    @JsonProperty("item")
    private ItemDTO itemDTO;

}
