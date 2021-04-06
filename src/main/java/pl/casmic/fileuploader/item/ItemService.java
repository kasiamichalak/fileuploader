package pl.casmic.fileuploader.item;

import java.util.List;

public interface ItemService {

    Item store(ItemDTO itemDto);
    Item findById(String id);
    List<ItemDTO> findAll();
}
