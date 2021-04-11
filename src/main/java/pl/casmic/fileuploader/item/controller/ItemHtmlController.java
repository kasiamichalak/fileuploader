package pl.casmic.fileuploader.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.casmic.fileuploader.item.domain.Item;
import pl.casmic.fileuploader.item.dto.*;
import pl.casmic.fileuploader.item.service.ItemServiceImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
@RequestMapping("/")
public class ItemHtmlController {

    private final ItemServiceImpl itemService;

    @PostMapping(value = "/upload",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.TEXT_HTML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String upload(@RequestPart(name = "file") final MultipartFile file,
                         @RequestParam(name = "description") final Optional<String> description, Model model) throws IOException {

        ItemDTO itemDTO = new ItemDTO();
        boolean success = false;

        if (file.getSize() != 0) {
            itemDTO = ItemDTO.builder()
                    .name(file.getOriginalFilename())
                    .data(file.getBytes())
                    .size(file.getSize())
                    .description(description.orElseGet(() -> "not provided"))
                    .uploadDate(LocalDate.now())
                    .build();
            itemDTO = itemService.store(itemDTO);
            success = true;
        }
        model.addAttribute("success", success);
        model.addAttribute("itemDTO", itemDTO);

        return "item/uploadresponse";
    }

    @GetMapping(value = "/items",
            produces = {MediaType.TEXT_HTML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public String items(Model model) {

        List<ItemListDTO> itemsDTO = itemService.findAll();

        model.addAttribute("items", itemsDTO);

        return "item/items";
    }

    @GetMapping(value = "/items/{id}",
            produces = {MediaType.TEXT_HTML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public String item(@PathVariable(name = "id") final String id, Model model) {

        ItemDTO itemDTO = itemService.findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        model.addAttribute("itemDTO", itemDTO);

        return "item/item";
    }
//
//    @DeleteMapping(value = "/items/{id}/delete", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity delete(@PathVariable(name = "id") final String id) {
//
//        DeleteResponseDTO deleteResponseDTO = new DeleteResponseDTO();
//
//        return itemService.findById(id).map(itemDTO -> {
//            itemService.delete(id);
//            deleteResponseDTO.setSuccess(true);
//            deleteResponseDTO.setMessage("File deleted");
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(deleteResponseDTO);
//        }).orElseGet(() -> {
//            deleteResponseDTO.setSuccess(false);
//            deleteResponseDTO.setMessage("Deletion failed");
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(deleteResponseDTO);
//        });
//    }
//
//    @GetMapping("/items/{id}/download")
//    public ResponseEntity<byte[]> download(@PathVariable(name = "id") final String id) {
//
//        return itemService.findById(id)
//                .map(itemDTO -> ResponseEntity.ok()
//                        .header(HttpHeaders.CONTENT_DISPOSITION,
//                        "attachment; filename=\"" + itemDTO.getName() + "\"")
//                        .body(itemDTO.getData()))
//                .orElse(ResponseEntity.notFound().build());
//    }
}
