package pl.casmic.fileuploader.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.casmic.fileuploader.item.domain.Item;
import pl.casmic.fileuploader.item.dto.ItemDTO;
import pl.casmic.fileuploader.item.dto.ItemListDTO;
import pl.casmic.fileuploader.item.mapper.ItemMapper;
import pl.casmic.fileuploader.item.repository.ItemRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Transactional
    @Override
    public ItemDTO store(final ItemDTO itemDTO) {

        Item createdItem = itemMapper.itemDTOToItem(itemDTO);
        itemRepository.save(createdItem);
        return itemMapper.itemToItemDTO(createdItem);
    }

    @Override
    public List<ItemListDTO> findAll() {
        return itemRepository.findAll()
                .stream()
                .map(itemMapper::itemToItemListDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ItemDTO> findById(final String id) {
        return itemRepository.findById(id)
                .map(itemMapper::itemToItemDTO);
    }

    @Override
    public void delete(final String id) {
        itemRepository.deleteById(id);
    }
}

