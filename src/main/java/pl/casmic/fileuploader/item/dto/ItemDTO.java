package pl.casmic.fileuploader.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Lob;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "description", "date"})
public class ItemDTO {

    @JsonProperty("itemID")
    private String id;
    @JsonProperty("itemName")
    private String name;
    @JsonIgnore
    @Lob
    private byte[] data;
    @JsonIgnore
    private Long size;
    private String description;
    @JsonProperty("date")
    private LocalDate uploadDate;
}
