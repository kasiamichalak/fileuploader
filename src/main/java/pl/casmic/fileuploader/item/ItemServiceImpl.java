package pl.casmic.fileuploader.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.casmic.fileuploader.item.dto.ItemDTO;
import pl.casmic.fileuploader.item.dto.ItemListDTO;
import pl.casmic.fileuploader.item.dto.ItemsDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Transactional
    @Override
    public Item store(final ItemDTO itemDTO) {

        final Item createdItem = Item.builder()
                .name(itemDTO.getName())
                .data(itemDTO.getData())
                .description(itemDTO.getDescription())
                .size(itemDTO.getSize())
                .uploadDate(LocalDate.now())
                .build();

        return itemRepository.save(createdItem);
    }

    @Override
    public List<ItemListDTO> findAll() {
        return itemRepository.findAll()
                .stream()
                .map(itemMapper::itemToItemListDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDTO findById(String id) {
        return itemRepository.findById(id)
                .map(itemMapper::itemToItemDTO)
                .orElseThrow(ItemNotFoundException::new);
    }

    @Override
    public void delete(String id) {
        itemRepository.deleteById(id);
    }
}

