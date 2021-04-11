package pl.casmic.fileuploader.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.casmic.fileuploader.item.dto.DeleteResponseDTO;
import pl.casmic.fileuploader.item.dto.ItemDTO;
import pl.casmic.fileuploader.item.dto.ItemsDTO;
import pl.casmic.fileuploader.item.dto.UploadResponseDTO;
import pl.casmic.fileuploader.item.service.ItemServiceImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@AllArgsConstructor
//@RestController
@RequestMapping("/")
public class ItemController {

    private final ItemServiceImpl itemService;

    @PostMapping(value = "/upload",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UploadResponseDTO upload(@RequestPart(name = "file") final MultipartFile file,
                                    @RequestParam(name = "description") final Optional<String> description) throws IOException {

        UploadResponseDTO uploadResponseDTO = new UploadResponseDTO();

        if (file.getSize() != 0) {
            ItemDTO itemDTO = ItemDTO.builder()
                    .name(file.getOriginalFilename())
                    .data(file.getBytes())
                    .size(file.getSize())
                    .description(description.orElseGet(() -> "not provided"))
                    .uploadDate(LocalDate.now())
                    .build();
            ItemDTO savedItemDTO = itemService.store(itemDTO);
            uploadResponseDTO.setSuccess(true);
            uploadResponseDTO.setItemDTO(savedItemDTO);
        }
        return uploadResponseDTO;
    }

    @GetMapping(value = "/items", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ItemsDTO items() {
        return new ItemsDTO(itemService.findAll());
    }

    @GetMapping("/items/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity item(@PathVariable(name = "id") final String id) {

        return itemService.findById(id)
                .map(itemDTO -> ResponseEntity.ok(itemDTO))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/items/{id}/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity delete(@PathVariable(name = "id") final String id) {

        DeleteResponseDTO deleteResponseDTO = new DeleteResponseDTO();

        return itemService.findById(id).map(itemDTO -> {
            itemService.delete(id);
            deleteResponseDTO.setSuccess(true);
            deleteResponseDTO.setMessage("File deleted");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(deleteResponseDTO);
        }).orElseGet(() -> {
            deleteResponseDTO.setSuccess(false);
            deleteResponseDTO.setMessage("Deletion failed");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(deleteResponseDTO);
        });
    }

    @GetMapping("/items/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable(name = "id") final String id) {

        return itemService.findById(id)
                .map(itemDTO -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + itemDTO.getName() + "\"")
                        .body(itemDTO.getData()))
                .orElse(ResponseEntity.notFound().build());
    }
}
