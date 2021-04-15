package pl.casmic.fileuploader.item.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.casmic.fileuploader.item.ItemGeneratorForTests;
import pl.casmic.fileuploader.item.domain.Item;
import pl.casmic.fileuploader.item.dto.ItemDTO;
import pl.casmic.fileuploader.item.dto.ItemListDTO;
import pl.casmic.fileuploader.item.service.ItemServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.casmic.fileuploader.item.ItemGeneratorForTests.*;


class ItemControllerHtmlResponseTest extends AbstractItemControllerTest implements ItemGeneratorForTests {

    @Mock
    private ItemServiceImpl itemService;
    @InjectMocks
    private ItemHtmlController itemController;
    private MockMvc mockMvc;

    private static final String JSON = "json";
    private static final String ID = UUID.randomUUID().toString();
    private static final LocalDate UPLOAD_DATE = LocalDate.of(2021, 04, 13);
    private static final Item ITEM = getExpectedItem(ID, UPLOAD_DATE);
    private static final ItemDTO ITEM_DTO = getExpectedItemDTOFromItem(ITEM);
    private static final String ITEM_DTO_ID = ITEM_DTO.getId();
    private static final String ITEM_DTO_URL = "/items/" + ITEM_DTO_ID;
    private static final ItemListDTO ITEM_LIST_DTO = getExpectedItemListDTOFromItem(ITEM);
    private static final boolean DELETE_SUCCESS_TRUE = true;
    private static final boolean DELETE_SUCCESS_FALSE = false;
    private static final String DELETE_MESSAGE_TRUE = "File deleted";
    private static final String DELETE_MESSAGE_FALSE = "Deletion failed";

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
    void shouldDisplayNotFoundWhenItemByIdDoesNotExistInDBHtml() throws Exception {

        when(itemService.findById(anyString())).thenReturn(Optional.ofNullable(null));

        mockMvc.perform(get(ITEM_DTO_URL)
                .contentType(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));

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
    void shouldReturnSuccessFalseForDeleteItemByIdWhenItemDoesNotExistInDBHtml() throws Exception {

        when(itemService.findById(anyString())).thenReturn(Optional.ofNullable(null));

        mockMvc.perform(get(ITEM_DTO_URL + "/delete")
                .contentType(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(view().name("item/delete"))
                .andExpect(model().attribute("success", DELETE_SUCCESS_FALSE))
                .andExpect(model().attribute("message", DELETE_MESSAGE_FALSE));

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
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));

        verify(itemService, times(1)).findById(anyString());
    }
}