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
    private String description;
    @JsonIgnore
    private Long size;
    @JsonProperty("date")
    private LocalDate uploadDate;
}
