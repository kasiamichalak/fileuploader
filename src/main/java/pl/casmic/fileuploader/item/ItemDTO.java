package pl.casmic.fileuploader.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ItemDTO {
    private String name;
    @JsonIgnore
    private byte[] data;
    @JsonIgnore
    private Long size;
    private String description;
}
