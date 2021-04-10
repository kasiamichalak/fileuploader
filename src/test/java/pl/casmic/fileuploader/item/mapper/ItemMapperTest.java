package pl.casmic.fileuploader.item.mapper;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.id.UUIDGenerator;
import org.junit.jupiter.api.Test;
import pl.casmic.fileuploader.item.ItemGeneratorForTests;
import pl.casmic.fileuploader.item.domain.Item;
import pl.casmic.fileuploader.item.dto.ItemDTO;
import pl.casmic.fileuploader.item.dto.ItemListDTO;

import static pl.casmic.fileuploader.item.ItemGeneratorForTests.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemMapperTest implements ItemGeneratorForTests {

    private ItemMapper itemMapper = ItemMapper.ITEM_MAPPER;

    private static final String ID = UUIDGenerator.buildSessionFactoryUniqueIdentifierGenerator().toString();
    private static final Item ITEM = getExpectedItem(ID);
    private static final ItemDTO ITEM_DTO = getExpectedItemDTOFromItem(ITEM);
    private static final ItemListDTO ITEM_LIST_DTO = getExpectedItemListDTOFromItem(ITEM);

    @Test
    void testItemToItemDTOHappyPath() {
        ItemDTO expected = ITEM_DTO;
        ItemDTO actual = itemMapper.itemToItemDTO(ITEM);

        assertEquals(actual.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getData().length, actual.getData().length);
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getSize(), actual.getSize());
        assertEquals(expected.getUploadDate(), actual.getUploadDate());
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
        assertEquals(expected.getData().length, actual.getData().length);
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getSize(), actual.getSize());
        assertEquals(expected.getUploadDate(), actual.getUploadDate());
    }
}