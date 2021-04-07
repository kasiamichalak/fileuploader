package pl.casmic.fileuploader.item;

import pl.casmic.fileuploader.item.dto.ItemDTO;
import pl.casmic.fileuploader.item.dto.ItemListDTO;
import pl.casmic.fileuploader.item.dto.ItemsDTO;

import java.util.List;

public interface ItemService {

    Item store(ItemDTO itemDTO);
    ItemDTO findById(String id);
    List<ItemListDTO> findAll();
    void delete(String id);
}
