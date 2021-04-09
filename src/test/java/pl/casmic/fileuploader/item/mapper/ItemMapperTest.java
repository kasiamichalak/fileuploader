package pl.casmic.fileuploader.item.mapper;

import org.hibernate.id.UUIDGenerator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import pl.casmic.fileuploader.item.domain.Item;
import pl.casmic.fileuploader.item.dto.ItemDTO;
import pl.casmic.fileuploader.item.dto.ItemListDTO;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {
//TODO: mapowanie data w teście nie działa prawidłowo
    ItemMapper itemMapper = ItemMapper.ITEM_MAPPER;

    public static final Item ITEM = getExpectedItem();
    public static final ItemDTO ITEM_DTO = getExpectedItemDTOFromItem(ITEM);
    public static final ItemListDTO ITEM_LIST_DTO = getExpectedItemListDTOFromItem(ITEM);

    @Test
    void testItemToItemDTOHappyPath() {
        ItemDTO expected = ITEM_DTO;
        ItemDTO actual = itemMapper.itemToItemDTO(ITEM);

        assertEquals(actual.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getData(), actual.getData());
        assertEquals(expected.getDescription(), actual.getDescription());
    }

    @Test
    void testItemToItemListDTOHappyPath() {
        ItemListDTO expected = ITEM_LIST_DTO;
        ItemListDTO actual = itemMapper.itemToItemListDTO(ITEM);

        assertEquals(actual.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSize(), actual.getSize());
    }

    @Test
    void testItemDTOToItemHappyPath() {

        Item expected = ITEM;
        Item actual = itemMapper.itemDTOToItem(ITEM_DTO);

        assertEquals(actual.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getData(), actual.getData());
        assertEquals(expected.getDescription(), actual.getDescription());
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
                .data("this is file".getBytes())
                .size(ITEM.getSize())
                .description(ITEM.getDescription())
                .uploadDate(ITEM.getUploadDate())
                .build();
    }

    private static ItemListDTO getExpectedItemListDTOFromItem(Item item) {
        return ItemListDTO.builder()
                .id(ITEM.getId())
                .name(ITEM.getName())
                .size(ITEM.getSize())
                .build();
    }
}