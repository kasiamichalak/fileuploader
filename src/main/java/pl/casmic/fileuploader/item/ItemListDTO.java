package pl.casmic.fileuploader.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ItemListDTO {
    private List<ItemDTO> items;
}
