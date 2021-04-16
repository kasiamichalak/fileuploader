package pl.casmic.fileuploader.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.casmic.fileuploader.item.ItemGeneratorForTests;
import pl.casmic.fileuploader.item.domain.Item;
import pl.casmic.fileuploader.item.dto.ItemDTO;
import pl.casmic.fileuploader.item.dto.ItemListDTO;
import pl.casmic.fileuploader.item.mapper.ItemMapper;
import pl.casmic.fileuploader.item.repository.ItemRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pl.casmic.fileuploader.item.ItemGeneratorForTests.*;

class ItemServiceImplTest implements ItemGeneratorForTests {

    @Mock
    private ItemRepository itemRepository;
    private ItemServiceImpl itemService;
    private ItemMapper itemMapper = ItemMapper.ITEM_MAPPER;

    private static final String ID = UUID.randomUUID().toString();
    private static final Instant UPLOAD_DATE = Instant.EPOCH;
    private static final Item ITEM = getExpectedItem(ID, UPLOAD_DATE);
    private static final String ITEM_ID = ITEM.getId();
    private static final ItemDTO ITEM_DTO = getExpectedItemDTOFromItem(ITEM);
    private static final List<ItemListDTO> LIST_ITEM_LIST_DTOS = getExpectedListOfItemListDTOs();
    private static final List<Item> LIST_ITEMS = getExpectedListOfItems();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        itemService = new ItemServiceImpl(itemRepository, itemMapper);
    }

    @Test
    void shouldSaveItemDTOToDBAndReturnSavedItemDTOWhenStoreHappyPath() {

        when(itemRepository.save(any(Item.class))).thenReturn(ITEM);

        ItemDTO expected = ITEM_DTO;
        ItemDTO actual = itemService.store(ITEM_DTO);

        assertEquals(actual.getId(), expected.getId());
        assertEquals(actual.getName(), expected.getName());
        assertEquals(actual.getSize(), expected.getSize());
        assertEquals(actual.getUploadDate(), expected.getUploadDate());

        verify(itemRepository, times(1)).save(any());
    }

    @Test
    void shouldReturnListOfItemListDTOsWhenFindAllHappyPath() {

        when(itemRepository.findAll()).thenReturn(LIST_ITEMS);

        List<ItemListDTO> expected = LIST_ITEM_LIST_DTOS;
        List<ItemListDTO> actual = itemService.findAll();

        assertEquals(actual.size(), expected.size());

        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnItemDTOWhenFindByIdHappyPath() {

        when(itemRepository.findById(anyString())).thenReturn(Optional.of(ITEM));

        ItemDTO expected = ITEM_DTO;
        ItemDTO actual = itemService.findById(ITEM_ID).get();

        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getData().length, actual.getData().length);
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getSize(), actual.getSize());
        assertEquals(expected.getUploadDate(), actual.getUploadDate());

        verify(itemRepository, times(1)).findById(anyString());
    }

    @Test
    void shouldReturnEmptyOptionalWhenFindByIdForIdNonExistingInDB() {

        when(itemRepository.findById(anyString())).thenReturn(Optional.ofNullable(null));

        Optional<ItemDTO> actual = itemService.findById(ID);

        assert (actual.isEmpty());

        verify(itemRepository, times(1)).findById(anyString());
    }

    @Test
    void shouldDeleteByIdHappyPath() {

        itemService.delete(ITEM_ID);

        verify(itemRepository, times(1)).deleteById(anyString());
    }
}