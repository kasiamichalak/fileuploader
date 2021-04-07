package pl.casmic.fileuploader.item;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.casmic.fileuploader.item.dto.ItemDTO;
import pl.casmic.fileuploader.item.dto.ItemListDTO;
import pl.casmic.fileuploader.item.dto.ItemsDTO;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class ItemController {

    ItemServiceImpl itemService;
    ItemMapper itemMapper;

    @PostMapping(value = "/upload", consumes = {MediaType.APPLICATION_JSON_VALUE,
                                                    MediaType.MULTIPART_FORM_DATA_VALUE},
                                        produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ItemDTO upload(@RequestPart (name = "file") MultipartFile file,
                                 @RequestParam (name = "description") Optional<String> description) {
        ItemDTO itemDTO = new ItemDTO();
        try {
            itemDTO.setData(file.getBytes());
        } catch (IOException e) {
            System.out.print("uppp, sth went wrong ");
        }
        itemDTO.setName(file.getOriginalFilename());
        itemDTO.setSize(file.getSize());
        itemDTO.setDescription(description.orElseGet(() -> "not provided"));
        Item itemCreated = itemService.store(itemDTO);
        return itemMapper.itemToItemDTO(itemCreated);
    }

    @GetMapping("/items")
    @ResponseStatus(HttpStatus.OK)
    public ItemsDTO items() {
        return new ItemsDTO(itemService.findAll());
    }

    @GetMapping(value = "/items/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDTO item(@PathVariable (name = "id") String id) {
        return itemService.findById(id);
    }
}
