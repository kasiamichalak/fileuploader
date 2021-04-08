package pl.casmic.fileuploader.item.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.hibernate.id.UUIDGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.casmic.fileuploader.item.dto.ItemDTO;
import pl.casmic.fileuploader.item.service.ItemService;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ItemControllerTestIT extends AbstractRestControllerTest {

    @Mock
    ItemService itemService;
    @InjectMocks
    ItemController itemController;
    @Autowired
    WebApplicationContext webApplicationContext;
    MockMvc mockMvc;

    private final static boolean UPLOAD_SUCCESS_TRUE = true;
    private final static boolean UPLOAD_SUCCESS_FALSE = false;
    private final static String WITH_DESCRIPTION_PARAM = "description";
    private final static String NULL_DESCRIPTION_PARAM = null;
    private final static ItemDTO ITEM_DTO_WITH_DESCRIPTION = getExpectedItemDTOWithDescription();
    private final static ItemDTO ITEM_DTO_NO_DESCRIPTION = getExpectedItemDTOWithNoDescription();
    private final static ItemDTO ITEM_DTO_FIELDS_NULL = getExpectedItemDTOWithNullFields();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldUploadFileWithDescriptionAndSaveItToDB() throws Exception {

        MockMultipartFile mockFile = getFileModel(ITEM_DTO_WITH_DESCRIPTION);

        when(itemService.store(any(ItemDTO.class))).thenReturn(ITEM_DTO_WITH_DESCRIPTION);

        mockMvc.perform(multipart("/upload")
                .file(mockFile)
                .param("description", WITH_DESCRIPTION_PARAM))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.success", equalTo(UPLOAD_SUCCESS_TRUE)))
                .andExpect(jsonPath("$.item.description", equalTo(ITEM_DTO_WITH_DESCRIPTION.getDescription())));
//                .andExpect(jsonPath("$.item.date", equalTo(ITEM_DTO_WITH_DESCRIPTION.getUploadDate())));
    }

    @Test
    void shouldUploadFileWithNoDescriptionAndSaveItToDB() throws Exception {

        MockMultipartFile mockFile = getFileModel(ITEM_DTO_NO_DESCRIPTION);

        when(itemService.store(any(ItemDTO.class))).thenReturn(ITEM_DTO_NO_DESCRIPTION);

        mockMvc.perform(multipart("/upload")
                .file(mockFile)
                .param("description", NULL_DESCRIPTION_PARAM))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.success", equalTo(UPLOAD_SUCCESS_TRUE)))
                .andExpect(jsonPath("$.item.description", equalTo(ITEM_DTO_NO_DESCRIPTION.getDescription())));
//                .andExpect(jsonPath("$.item.date", equalTo(ITEM_DTO_NO_DESCRIPTION.getUploadDate())));
    }

    @Test
    void shouldReturnSuccessFalseAndItemNullWhenNoFileNoDescriptionUploaded() throws Exception {

        MockMultipartFile mockFile = getEmptyFileModel();

        when(itemService.store(any(ItemDTO.class))).thenReturn(ITEM_DTO_FIELDS_NULL);

        mockMvc.perform(multipart("/upload")
                .file(mockFile)
                .param("description", NULL_DESCRIPTION_PARAM))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.success", equalTo(UPLOAD_SUCCESS_FALSE)))
                .andExpect(jsonPath("$.item", is(nullValue())));
    }

    @Test
    void shouldReturnSuccessFalseAndItemNullWhenNoFileButDescriptionUploaded() throws Exception {

        MockMultipartFile mockFile = getEmptyFileModel();

        when(itemService.store(any(ItemDTO.class))).thenReturn(ITEM_DTO_FIELDS_NULL);

        mockMvc.perform(multipart("/upload")
                .file(mockFile)
                .param("description", WITH_DESCRIPTION_PARAM))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.success", equalTo(UPLOAD_SUCCESS_FALSE)))
                .andExpect(jsonPath("$.item", is(nullValue())));
    }

    @Test
    void shouldDisplayListOfAllFilesUploadedAndSavedInDB() {
    }

    @Test
    void item() {
    }

    @Test
    void delete() {
    }

    @Test
    void download() {
    }

    private MockMultipartFile getFileModel(ItemDTO dto) throws JsonProcessingException {
        return new MockMultipartFile("file", dto.getName(), MediaType.TEXT_PLAIN_VALUE, asJSONString(dto).getBytes());
    }

    private MockMultipartFile getEmptyFileModel() throws JsonProcessingException {
        return new MockMultipartFile("file", "", MediaType.TEXT_PLAIN_VALUE, new byte[] {});
    }

    private static ItemDTO getExpectedItemDTOWithDescription() {
        return ItemDTO.builder()
                .id(new UUIDGenerator().toString())
                .name("file.jpg")
                .data("this is file".getBytes())
                .description("description")
                .size(Long.valueOf(3457))
                .uploadDate(LocalDate.of(2021, 4, 8))
                .build();
    }

    private static ItemDTO getExpectedItemDTOWithNoDescription() {
        return ItemDTO.builder()
                .id(new UUIDGenerator().toString())
                .name("file.jpg")
                .data("this is file".getBytes())
                .description("not provided")
                .size(Long.valueOf(3457))
                .uploadDate(LocalDate.of(2021, 4, 8))
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