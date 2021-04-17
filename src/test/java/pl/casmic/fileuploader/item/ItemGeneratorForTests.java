package pl.casmic.fileuploader.item;

import pl.casmic.fileuploader.item.domain.Item;
import pl.casmic.fileuploader.item.dto.ItemDTO;
import pl.casmic.fileuploader.item.dto.ItemListDTO;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface ItemGeneratorForTests {

    String RESPONSE_FORMAT = "f";
    String JSON = "json";
    String HTML = "html";
    String DESCRIPTION_PARAM_EXISTING = "description";
    String DESCRIPTION_PARAM_NULL = null;
    String DESCRIPTION_DEFAULT = "not provided";
    String ID = UUID.randomUUID().toString();
    Instant UPLOAD_DATE = Instant.EPOCH;
    Item ITEM = getExpectedItem(ID, UPLOAD_DATE);
    String ITEM_ID = ITEM.getId();
    ItemDTO ITEM_DTO = getExpectedItemDTOFromItem(ITEM);
    String ITEM_DTO_ID = ITEM_DTO.getId();
    String ITEM_DTO_URL = "/items/" + ID;
    ItemListDTO ITEM_LIST_DTO = getExpectedItemListDTOFromItem(ITEM);
    ItemDTO ITEM_DTO_WITH_DESCRIPTION = getExpectedItemDTOFromItem(ITEM);
    ItemDTO ITEM_DTO_DEFAULT_DESCRIPTION = getExpectedItemDTOFromItemWithDefaultDescription(ITEM, DESCRIPTION_DEFAULT);
    ItemDTO ITEM_DTO_FIELDS_NULL = new ItemDTO();
    List<ItemListDTO> LIST_ITEM_LIST_DTOS = getExpectedListOfItemListDTOs();
    List<Item> LIST_ITEMS = getExpectedListOfItems();
    boolean UPLOAD_SUCCESS_TRUE = true;
    boolean UPLOAD_SUCCESS_FALSE = false;
    boolean DELETE_SUCCESS_TRUE = true;
    boolean DELETE_SUCCESS_FALSE = false;
    String DELETE_MESSAGE_TRUE = "File deleted";
    String DELETE_MESSAGE_FALSE = "Resource not found";

    static Item getExpectedItem(String id, Instant date) {
        return Item.builder()
                .id(id)
                .name("file.jpg")
                .data("this is file".getBytes())
                .description("description")
                .size(Long.valueOf(3457))
                .uploadDate(date)
                .build();
    }

    static ItemDTO getExpectedItemDTOFromItem(Item item) {

        return ItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .data(item.getData())
                .description(item.getDescription())
                .size(item.getSize())
                .uploadDate(item.getUploadDate())
                .build();
    }

    static ItemDTO getExpectedItemDTOFromItemWithDefaultDescription(Item item, String description) {

        return ItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .data(item.getData())
                .description(description)
                .size(item.getSize())
                .uploadDate(item.getUploadDate())
                .build();
    }

    static ItemListDTO getExpectedItemListDTOFromItem(Item item) {

        return ItemListDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .size(item.getSize())
                .build();
    }

    static List<ItemListDTO> getExpectedListOfItemListDTOs() {
        List<ItemListDTO> items = new ArrayList<>();
        items.add(new ItemListDTO());
        items.add(new ItemListDTO());
        return items;
    }

    static List<Item> getExpectedListOfItems() {
        List<Item> items = new ArrayList<>();
        items.add(new Item());
        items.add(new Item());
        return items;
    }
}
