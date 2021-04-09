package pl.casmic.fileuploader.item.service;

import org.hibernate.id.UUIDGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.casmic.fileuploader.item.domain.Item;
import pl.casmic.fileuploader.item.dto.ItemDTO;
import pl.casmic.fileuploader.item.dto.ItemListDTO;
import pl.casmic.fileuploader.item.mapper.ItemMapper;
import pl.casmic.fileuploader.item.repository.ItemRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class ItemServiceImplTest {

    @Mock
    ItemRepository itemRepository;
    ItemServiceImpl itemService;
    ItemMapper itemMapper = ItemMapper.ITEM_MAPPER;

    private static final Item ITEM = getExpectedItem();
    private static final String ITEM_ID = ITEM.getId();
    private static final String ID_NON_EXISTING = UUIDGenerator.buildSessionFactoryUniqueIdentifierGenerator().toString();
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

//    TODO: jeszcze nie działa
    @Test
    void shouldReturnItemDTOWhenFindByIdHappyPath() {

        when(itemRepository.findById(anyString())).thenReturn(Optional.of(ITEM));

        ItemDTO expected = ITEM_DTO;
        ItemDTO actual = itemService.findById(ITEM_ID).get();

        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getData(), actual.getData());
        assertEquals(expected.getDescription(), actual.getDescription());

        verify(itemRepository, times(1)).findById(anyString());
    }

    @Test
    void shouldReturnEmptyOptionalWhenFindByIdForIdNonExistingInDB() {

        when(itemRepository.findById(anyString())).thenReturn(Optional.ofNullable(null));

        Optional<ItemDTO> actual = itemService.findById(ID_NON_EXISTING);

        assert (actual.isEmpty());

        verify(itemRepository, times(1)).findById(anyString());
    }

    @Test
    void shouldDeleteByIdHappyPath() {

        itemService.delete(ITEM_ID);

        verify(itemRepository, times(1)).deleteById(anyString());
    }

    private static Item getExpectedItem() {
        return Item.builder()
                .id(UUIDGenerator.buildSessionFactoryUniqueIdentifierGenerator().toString())
                .name("file.jpg")
                .data("this is file".getBytes())
                .description("description")
                .size(Long.valueOf(3457))
                .uploadDate(LocalDate.of(2021, 4, 8))
                .build();
    }

    private static ItemDTO getExpectedItemDTOFromItem(Item item) {
        return ItemDTO.builder()
                .id(ITEM.getId())
                .name(ITEM.getName())
                .data(ITEM.getData())
                .size(ITEM.getSize())
                .description(ITEM.getDescription())
                .uploadDate(ITEM.getUploadDate())
                .build();
    }

    private static List<ItemListDTO> getExpectedListOfItemListDTOs() {
        List<ItemListDTO> items = new ArrayList<>();
        items.add(new ItemListDTO());
        items.add(new ItemListDTO());
        return items;
    }

    private static List<Item> getExpectedListOfItems() {
        List<Item> items = new ArrayList<>();
        items.add(new Item());
        items.add(new Item());
        return items;
    }
}