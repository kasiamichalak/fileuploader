package pl.casmic.fileuploader.item;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class ItemController {

    ItemServiceImpl itemService;

    @PostMapping(value = "/upload", consumes = {MediaType.APPLICATION_JSON_VALUE,
                                                    MediaType.MULTIPART_FORM_DATA_VALUE},
                                        produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity upload(@RequestPart (name = "file") MultipartFile file,
                                           @RequestParam (name = "description") Optional<String> description) {
        ItemDTO itemDto = new ItemDTO();
        try {
            itemDto.setData(file.getBytes());
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
        itemDto.setName(file.getOriginalFilename());
        itemDto.setSize(file.getSize());
        itemDto.setDescription(description.orElseGet(() -> "not provided"));
        final Item itemCreated = itemService.store(itemDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(itemCreated);
    }

    @GetMapping("/items")
    @ResponseStatus(HttpStatus.OK)
    public ItemListDTO items() {
        return new ItemListDTO(itemService.findAll());
    }

    @GetMapping(value = "/items/{id}")
    public ResponseEntity item(@PathVariable (name = "id") String id) {
        return ResponseEntity.status(HttpStatus.OK).body(itemService.findById(id));
    }
}
