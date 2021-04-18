package pl.casmic.fileuploader.item.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.casmic.fileuploader.exception.NotFoundException;
import pl.casmic.fileuploader.item.ItemGeneratorForTests;
import pl.casmic.fileuploader.item.dto.ItemListDTO;
import pl.casmic.fileuploader.item.service.ItemServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ItemHtmlControllerHtmlResponseTest implements ItemGeneratorForTests {

    @Mock
    private ItemServiceImpl itemService;
    @InjectMocks
    private ItemHtmlController itemController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
    }

    @Test
    void shouldDisplayUploadFormHtml() throws Exception {

        mockMvc.perform(get("/")
                .contentType(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(view().name("item/uploadform"));
    }

    @Test
    void shouldDisplayListOfAllFilesUploadedAndSavedInDBHtml() throws Exception {

        List<ItemListDTO> itemsList = new ArrayList<>();
        itemsList.add(ITEM_LIST_DTO);
        itemsList.add(ITEM_LIST_DTO);

        when(itemService.findAll()).thenReturn(itemsList);

        mockMvc.perform(get("/items")
                .contentType(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(view().name("item/items"))
                .andExpect(model().attributeExists("items"));

        verify(itemService, times(1)).findAll();
    }

    @Test
    void shouldDisplayItemByIdWhenItemExistsInDBHtml() throws Exception {

        when(itemService.findById(anyString())).thenReturn(Optional.of(ITEM_DTO));

        mockMvc.perform(get(ITEM_DTO_URL)
                .contentType(MediaType.TEXT_HTML_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("item/item"))
                .andExpect(model().attributeExists("item"));

        verify(itemService, times(1)).findById(anyString());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenItemByIdDoesNotExistInDBHtml() throws Exception {

        when(itemService.findById(anyString())).thenReturn(Optional.ofNullable(null));

        mockMvc.perform(get(ITEM_DTO_URL)
                .contentType(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));

        verify(itemService, times(1)).findById(anyString());
    }

    @Test
    void shouldReturnSuccessTrueForDeleteItemByIdWhenItemExistsInDBHtml() throws Exception {

        when(itemService.findById(anyString())).thenReturn(Optional.ofNullable(ITEM_DTO));

        mockMvc.perform(get(ITEM_DTO_URL + "/delete")
                .contentType(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(view().name("item/delete"))
                .andExpect(model().attribute("success", DELETE_SUCCESS_TRUE))
                .andExpect(model().attribute("message", DELETE_MESSAGE_TRUE));

        verify(itemService, times(1)).findById(anyString());
        verify(itemService, times(1)).delete(anyString());
    }

    @Test
    void shouldThrowNotFoundExceptionWithSuccessFalseForDeleteItemByIdWhenItemDoesNotExistInDBHtml() throws Exception {

        when(itemService.findById(anyString())).thenReturn(Optional.ofNullable(null));

        mockMvc.perform(get(ITEM_DTO_URL + "/delete")
                .contentType(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));

        verify(itemService, times(1)).findById(anyString());
        verify(itemService, times(0)).delete(anyString());
    }

    @Test
    void shouldDownloadFileForItemByIdWhenItemExistsInDBHtml() throws Exception {

        when(itemService.findById(anyString())).thenReturn(Optional.of(ITEM_DTO));

        mockMvc.perform(get(ITEM_DTO_URL + "/download")
                .contentType(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk());

        verify(itemService, times(1)).findById(anyString());
    }

    @Test
    void shouldReturnNotFoundWhenDownloadFileForItemByIdWhenItemDoesNotExistInDBHtml() throws Exception {

        when(itemService.findById(anyString())).thenReturn(Optional.ofNullable(null));

        mockMvc.perform(get(ITEM_DTO_URL + "/download")
                .contentType(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));

        verify(itemService, times(1)).findById(anyString());
    }
}