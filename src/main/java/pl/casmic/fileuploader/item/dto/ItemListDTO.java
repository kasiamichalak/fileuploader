package pl.casmic.fileuploader.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "size"})
public class ItemListDTO {

    @JsonProperty("itemID")
    private String id;
    @JsonProperty("itemName")
    private String name;
    @JsonProperty("itemSize")
    private Long size;

}
