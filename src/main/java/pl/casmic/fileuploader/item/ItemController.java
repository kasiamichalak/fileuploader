package pl.casmic.fileuploader.item;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.casmic.fileuploader.item.dto.*;

import java.io.FileNotFoundException;
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
    public UploadResponseDTO upload(@RequestPart (name = "file") MultipartFile file,
                                 @RequestParam (name = "description") Optional<String> description) throws IOException {
        ItemDTO itemDTO = new ItemDTO();
        UploadResponseDTO uploadResponseDTO = new UploadResponseDTO();
        itemDTO.setData(file.getBytes());
        if (file.getSize() == 0) {
            return uploadResponseDTO;
        } else {
            itemDTO.setName(file.getOriginalFilename());
            itemDTO.setSize(file.getSize());
            itemDTO.setDescription(description.orElseGet(() -> "not provided"));
            Item itemCreated = itemService.store(itemDTO);
            itemDTO = itemMapper.itemToItemDTO(itemCreated);
            uploadResponseDTO.setSuccess(true);
            uploadResponseDTO.setItemDTO(itemDTO);
            return uploadResponseDTO;
        }
    }

    @GetMapping("/items")
    @ResponseStatus(HttpStatus.OK)
    public ItemsDTO items() {
        return new ItemsDTO(itemService.findAll());
    }

    @GetMapping("/items/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDTO item(@PathVariable (name = "id") String id) {
        return itemService.findById(id);
    }

    @DeleteMapping ("/items/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public DeleteResponseDTO delete(@PathVariable (name = "id") String id) {
        DeleteResponseDTO deleteResponseDTO = new DeleteResponseDTO();
        deleteResponseDTO.setSuccess(false);
        deleteResponseDTO.setMessage("Deletion failed");
        itemService.delete(id);
        try {
            itemService.findById(id);
        } catch (ItemNotFoundException e) {
            deleteResponseDTO.setSuccess(true);
            deleteResponseDTO.setMessage("File deleted");
            return deleteResponseDTO;
        }
        return deleteResponseDTO;
    }

}
