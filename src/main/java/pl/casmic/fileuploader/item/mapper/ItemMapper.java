package pl.casmic.fileuploader.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.casmic.fileuploader.item.domain.Item;
import pl.casmic.fileuploader.item.dto.ItemDTO;
import pl.casmic.fileuploader.item.dto.ItemListDTO;

@Mapper
public interface ItemMapper {

    ItemMapper ITEM_MAPPER = Mappers.getMapper(ItemMapper.class);

    ItemDTO itemToItemDTO(Item item);
    ItemListDTO itemToItemListDTO(Item item);
    Item itemDTOToItem(ItemDTO itemDTO);

}
