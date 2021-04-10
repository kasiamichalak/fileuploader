package pl.casmic.fileuploader.item.service;

import pl.casmic.fileuploader.item.dto.ItemDTO;
import pl.casmic.fileuploader.item.dto.ItemListDTO;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    ItemDTO store(ItemDTO itemDTO);
    Optional<ItemDTO> findById(String id);
    List<ItemListDTO> findAll();
    void delete(String id);
}
