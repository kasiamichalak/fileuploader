package pl.casmic.fileuploader.item.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import java.time.Instant;

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
    @JsonIgnore
    private Instant uploadDate;

    @JsonGetter
    @JsonProperty("date")
    private Long getEpochTime() {
        return this.uploadDate != null ? this.uploadDate.toEpochMilli() : null;
    }

    @JsonSetter
    @JsonProperty("date")
    private void setEpochTime(Long time) {
        this.uploadDate = Instant.ofEpochMilli(time);
    }
}
