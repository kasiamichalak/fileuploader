package pl.casmic.fileuploader.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.casmic.fileuploader.exception.NotFoundException;
import pl.casmic.fileuploader.item.dto.ItemDTO;
import pl.casmic.fileuploader.item.dto.ItemListDTO;
import pl.casmic.fileuploader.item.service.ItemServiceImpl;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/")
public class ItemHtmlController {

    private final ItemServiceImpl itemService;

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String displayUploadForm() {
        return "item/uploadform";
    }

    @PostMapping(value = "/upload",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.TEXT_HTML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String upload(@RequestPart(name = "file") final MultipartFile file,
                         @RequestParam(name = "description", required = false) @Nullable final String description, Model model) throws IOException {

        ItemDTO itemDTO = new ItemDTO();

        boolean success = false;
        String fileDescription = description;

        if(description == null || description.isBlank()) {
            fileDescription = "not provided";
        }

        if (file.getSize() != 0) {
            itemDTO = ItemDTO.builder()
                    .name(file.getOriginalFilename())
                    .data(file.getBytes())
                    .size(file.getSize())
                    .description(fileDescription)
                    .uploadDate(Instant.now())
                    .build();
            itemDTO = itemService.store(itemDTO);
            success = true;
        }
        model.addAttribute("success", success);
        model.addAttribute("item", itemDTO);

        return "item/upload";
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

       return itemService.findById(id)
               .map(itemDTO -> {
                   model.addAttribute("item", itemDTO);
                   return "item/item";
               })
               .orElseThrow(NotFoundException::new);
        }

    @GetMapping(value = "/items/{id}/delete",
            produces = {MediaType.TEXT_HTML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public String delete(@PathVariable(name = "id") final String id, Model model) {

        return itemService.findById(id)
                .map(itemDTO -> {
                    itemService.delete(itemDTO.getId());

                    Boolean success = true;
                    String message = "File deleted";

                    model.addAttribute("success", success);
                    model.addAttribute("message", message);

                    return "item/delete";
                })
                .orElseThrow(NotFoundException::new);
    }

    @GetMapping("/items/{id}/download")
    @ResponseBody
    public ResponseEntity<byte[]> download(@PathVariable(name = "id") final String id) {

        return itemService.findById(id)
                .map(itemDTO -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + itemDTO.getName() + "\"")
                        .body(itemDTO.getData()))
                .orElseThrow(NotFoundException::new);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(Exception exception, Model model) {

        boolean success = false;
        String message = "Resource not found";
        model.addAttribute("success", success);
        model.addAttribute("message", message);

        return "exception/404error";
    }
}
