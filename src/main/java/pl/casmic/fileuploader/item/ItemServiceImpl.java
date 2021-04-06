package pl.casmic.fileuploader.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Item store(final ItemDTO itemDto) {

        final Item createdItem = Item.builder()
                .name(itemDto.getName())
                .data(itemDto.getData())
                .description(itemDto.getDescription())
                .size(itemDto.getSize())
                .uploadDate(LocalDate.now())
                .build();

        return itemRepository.save(createdItem);
    }

    @Override
    public List<ItemDTO> findAll() {
        return itemRepository.findAll()
                .stream()
                .map(itemMapper::itemToItemDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Item findById(String id) {
        return itemRepository.findById(id).orElseThrow(ItemNotFoundException::new);
    }
}

