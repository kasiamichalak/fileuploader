package pl.casmic.fileuploader.item.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import pl.casmic.fileuploader.item.ItemGeneratorForTests;
import pl.casmic.fileuploader.item.domain.Item;
import pl.casmic.fileuploader.item.dto.ItemDTO;
import pl.casmic.fileuploader.item.dto.ItemListDTO;
import pl.casmic.fileuploader.item.service.ItemServiceImpl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.casmic.fileuploader.item.ItemGeneratorForTests.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ItemHtmlController.class)
class ItemControllerJsonResponseTest extends AbstractItemControllerTest implements ItemGeneratorForTests {

    @MockBean
    private ItemServiceImpl itemService;
    @Autowired
    private MockMvc mockMvc;

    private static final String RESPONSE_FORMAT = "f";
    private static final String JSON = "json";
    private static final String ID = UUID.randomUUID().toString();
    private static final Instant UPLOAD_DATE = Instant.EPOCH;
    private static final Item ITEM = getExpectedItem(ID, UPLOAD_DATE);
    private static final ItemDTO ITEM_DTO = getExpectedItemDTOFromItem(ITEM);
    private static final String ITEM_DTO_ID = ITEM_DTO.getId();
    private static final String ITEM_DTO_URL = "/items/" + ID;
    private static final ItemListDTO ITEM_LIST_DTO = getExpectedItemListDTOFromItem(ITEM);
    private static final boolean DELETE_SUCCESS_TRUE = true;
    private static final boolean DELETE_SUCCESS_FALSE = false;
    private static final String DELETE_MESSAGE_TRUE = "File deleted";
    private static final String DELETE_MESSAGE_FALSE = "Resource not found";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldDisplayListOfAllFilesUploadedAndSavedInDBJson() throws Exception {

        List<ItemListDTO> itemsList = new ArrayList<>();
        itemsList.add(ITEM_LIST_DTO);
        itemsList.add(ITEM_LIST_DTO);

        when(itemService.findAll()).thenReturn(itemsList);

        mockMvc.perform(get("/items")
                .param(RESPONSE_FORMAT, JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(2)));

        verify(itemService, times(1)).findAll();
    }

    @Test
    void shouldDisplayItemByIdWhenItemExistsInDBJson() throws Exception {

        when(itemService.findById(anyString())).thenReturn(Optional.of(ITEM_DTO));

        mockMvc.perform(get("/items/" + ITEM_DTO_ID)
                .param(RESPONSE_FORMAT, JSON)
                .content(String.valueOf(ITEM_DTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.itemID", equalTo(ITEM_DTO.getId())))
                .andExpect(jsonPath("$.item.itemName", equalTo(ITEM_DTO.getName())))
                .andExpect(jsonPath("$.item.description", equalTo(ITEM_DTO.getDescription())));
//                .andExpect(jsonPath("$.item.date", equalTo(String.valueOf(ITEM_DTO.getUploadDate()))));

        verify(itemService, times(1)).findById(anyString());
    }

    @Test
    void shouldDisplayNotFoundWhenItemByIdDoesNotExistInDBJson() throws Exception {

        when(itemService.findById(anyString())).thenReturn(Optional.ofNullable(null));

        mockMvc.perform(get(ITEM_DTO_URL)
                .param(RESPONSE_FORMAT, JSON))
                .andExpect(status().isNotFound());

        verify(itemService, times(1)).findById(anyString());
    }

    @Test
    void shouldReturnSuccessTrueForDeleteItemByIdWhenItemExistsInDBJson() throws Exception {

        when(itemService.findById(anyString())).thenReturn(Optional.ofNullable(ITEM_DTO));

        mockMvc.perform(get(ITEM_DTO_URL + "/delete")
                .param(RESPONSE_FORMAT, JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", equalTo(DELETE_SUCCESS_TRUE)))
                .andExpect(jsonPath("$.message", equalTo(DELETE_MESSAGE_TRUE)));

        verify(itemService, times(1)).findById(anyString());
        verify(itemService, times(1)).delete(anyString());
    }

    @Test
    void shouldReturnSuccessFalseForDeleteItemByIdWhenItemDoesNotExistInDBJson() throws Exception {

        when(itemService.findById(anyString())).thenReturn(Optional.ofNullable(null));

        mockMvc.perform(get(ITEM_DTO_URL + "/delete")
                .param(RESPONSE_FORMAT, JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", equalTo(DELETE_SUCCESS_FALSE)))
                .andExpect(jsonPath("$.message", equalTo(DELETE_MESSAGE_FALSE)));

        verify(itemService, times(1)).findById(anyString());
        verify(itemService, times(0)).delete(anyString());
    }

    @Test
    void shouldDownloadFileForItemByIdWhenItemExistsInDBJson() throws Exception {

        when(itemService.findById(anyString())).thenReturn(Optional.of(ITEM_DTO));

        mockMvc.perform(get(ITEM_DTO_URL + "/download")
                .param(RESPONSE_FORMAT, JSON)
                .content(ITEM_DTO.getData()))
                .andExpect(status().isOk());

        verify(itemService, times(1)).findById(anyString());
    }

    @Test
    void shouldReturnNotFoundWhenDownloadFileForItemByIdWhenItemDoesNotExistInDBJson() throws Exception {

        when(itemService.findById(anyString())).thenReturn(Optional.ofNullable(null));

        mockMvc.perform(get(ITEM_DTO_URL + "/download")
                .param(RESPONSE_FORMAT, JSON))
                .andExpect(status().isNotFound());

        verify(itemService, times(1)).findById(anyString());
    }
}