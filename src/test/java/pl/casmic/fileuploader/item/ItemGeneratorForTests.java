package pl.casmic.fileuploader.item;

import pl.casmic.fileuploader.item.domain.Item;
import pl.casmic.fileuploader.item.dto.ItemDTO;
import pl.casmic.fileuploader.item.dto.ItemListDTO;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public interface ItemGeneratorForTests {

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
