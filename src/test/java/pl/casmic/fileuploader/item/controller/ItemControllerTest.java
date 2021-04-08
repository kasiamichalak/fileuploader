package pl.casmic.fileuploader.item.controller;

import org.hibernate.id.UUIDGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.casmic.fileuploader.item.dto.ItemDTO;
import pl.casmic.fileuploader.item.dto.ItemListDTO;
import pl.casmic.fileuploader.item.service.ItemServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ItemControllerTest extends AbstractRestControllerTest {

    @Mock
    ItemServiceImpl itemService;
    @InjectMocks
    ItemController itemController;
    MockMvc mockMvc;

    private final static ItemDTO ITEM_DTO = getExpectedItemDTO();
    private final static String ITEM_DTO_ID = ITEM_DTO.getId();
    private final static String ITEM_DTO_URL = "/items/" + ITEM_DTO_ID;
    private final static String ID_NON_EXISTING = UUIDGenerator.buildSessionFactoryUniqueIdentifierGenerator().toString();
    private final static String ITEM_DTO_URL_ID_NON_EXISTING = "/items/" + ID_NON_EXISTING;
    private final static ItemListDTO ITEM_LIST_DTO = getExpectedItemListDTO();
    private final static boolean DELETE_SUCCESS_TRUE = true;
    private final static boolean DELETE_SUCCESS_FALSE = false;
    private final static String DELETE_MESSAGE_TRUE = "File deleted";
    private final static String DELETE_MESSAGE_FALSE = "Deletion failed";
    private final static ItemDTO ITEM_DTO_FIELDS_NULL = getExpectedItemDTOWithNullFields();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
    }


    @Test
    void shouldDisplayListOfAllFilesUploadedAndSavedInDB() throws Exception {

        List<ItemListDTO> itemsList = new ArrayList<>();
        itemsList.add(getExpectedItemListDTO());
        itemsList.add(getExpectedItemListDTO());

        when(itemService.findAll()).thenReturn(itemsList);

        mockMvc.perform(get("/items")
                .accept(APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(2)));

        verify(itemService, times(1)).findAll();

    }

    @Test
    void shouldDisplayItemByIdWhenItemExistsInDB() throws Exception {

        when(itemService.findById(anyString())).thenReturn(Optional.of(ITEM_DTO));

        mockMvc.perform(get(ITEM_DTO_URL)
                .accept(APPLICATION_JSON)
                .content(String.valueOf(ITEM_LIST_DTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemService, times(1)).findById(anyString());

    }

    @Test
    void shouldDisplayNotFoundWhenItemByIdDoesNotExistInDB() throws Exception {

        mockMvc.perform(get(ITEM_DTO_URL_ID_NON_EXISTING)
                .accept(APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(itemService, times(1)).findById(anyString());

    }

    @Test
    void shouldReturnSuccessTrueForDeleteItemByIdWhenItemExistsInDB() throws Exception {

        mockMvc.perform(delete(ITEM_DTO_URL + "/delete")
                .accept(APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.success", equalTo(DELETE_SUCCESS_TRUE)))
                .andExpect(jsonPath("$.message", equalTo(DELETE_MESSAGE_TRUE)));

        verify(itemService, times(1)).delete(anyString());
    }

    @Test
    void shouldDownloadFileForItemByIdWhenItemExistsInDB() throws Exception {

        when(itemService.findById(anyString())).thenReturn(Optional.of(ITEM_DTO));

        mockMvc.perform(get(ITEM_DTO_URL + "/download")
                .accept(APPLICATION_JSON)
                .content(ITEM_DTO.getData())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemService, times(1)).findById(anyString());
    }

    @Test
    void shouldReturnNotFoundWhenDownloadFileForItemByIdWhenItemDoesNotExistInDB() throws Exception {

        when(itemService.findById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get(ITEM_DTO_URL_ID_NON_EXISTING + "/download")
                .accept(APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(itemService, times(1)).findById(anyString());
    }


    private static ItemDTO getExpectedItemDTO() {
        return ItemDTO.builder()
                .id(new UUIDGenerator().toString())
                .name("file.jpg")
                .data("this is file".getBytes())
                .description("description")
                .size(Long.valueOf(3457))
                .uploadDate(LocalDate.of(2021, 4, 8))
                .build();
    }

    private static ItemListDTO getExpectedItemListDTO() {
        return ItemListDTO.builder()
                .id(getExpectedItemDTO().getId())
                .name("file.jpg")
                .size(Long.valueOf(3457))
                .build();
    }

    private static ItemDTO getExpectedItemDTOWithNullFields() {
        return ItemDTO.builder()
                .id(null)
                .name(null)
                .data(null)
                .description(null)
                .size(null)
                .uploadDate(null)
                .build();
    }
}