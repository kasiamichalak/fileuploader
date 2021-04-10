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
import pl.casmic.fileuploader.item.ItemGeneratorForTests;
import pl.casmic.fileuploader.item.domain.Item;
import pl.casmic.fileuploader.item.dto.ItemDTO;
import pl.casmic.fileuploader.item.service.ItemService;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.casmic.fileuploader.item.ItemGeneratorForTests.*;

@SpringBootTest
class ItemControllerTestIT extends AbstractRestControllerTest implements ItemGeneratorForTests {

    @Mock
    private ItemService itemService;
    @InjectMocks
    private ItemController itemController;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    private static final String DESCRIPTION_PARAM_EXISTING = "description";
    private static final String DESCRIPTION_PARAM_NULL = null;
    private static final String DESCRIPTION_DEFAULT = "not provided";
    private static final String ID = UUIDGenerator.buildSessionFactoryUniqueIdentifierGenerator().toString();
    private static final Item ITEM = getExpectedItem(ID);
    private final static ItemDTO ITEM_DTO_WITH_DESCRIPTION = getExpectedItemDTOFromItem(ITEM);
    private final static ItemDTO ITEM_DTO_DEFAULT_DESCRIPTION = getExpectedItemDTOFromItemWithDefaultDescription(ITEM, DESCRIPTION_DEFAULT);
    private final static ItemDTO ITEM_DTO_FIELDS_NULL = new ItemDTO();
    private static final boolean UPLOAD_SUCCESS_TRUE = true;
    private static final boolean UPLOAD_SUCCESS_FALSE = false;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldUploadFileWithDescriptionAndSaveItToDB() throws Exception {

        MockMultipartFile mockFile = getFileModel(ITEM_DTO_WITH_DESCRIPTION);

        when(itemService.store(any(ItemDTO.class))).thenReturn(ITEM_DTO_WITH_DESCRIPTION);

        mockMvc.perform(multipart("/upload")
                .file(mockFile)
                .param("description", DESCRIPTION_PARAM_EXISTING))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.success", equalTo(UPLOAD_SUCCESS_TRUE)))
                .andExpect(jsonPath("$.item.description", equalTo(ITEM_DTO_WITH_DESCRIPTION.getDescription())));
    }

    @Test
    void shouldUploadFileWithNoDescriptionAndSaveItToDB() throws Exception {

        MockMultipartFile mockFile = getFileModel(ITEM_DTO_DEFAULT_DESCRIPTION);

        when(itemService.store(any(ItemDTO.class))).thenReturn(ITEM_DTO_DEFAULT_DESCRIPTION);

        mockMvc.perform(multipart("/upload")
                .file(mockFile)
                .param("description", DESCRIPTION_PARAM_NULL))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.success", equalTo(UPLOAD_SUCCESS_TRUE)))
                .andExpect(jsonPath("$.item.description", equalTo(ITEM_DTO_DEFAULT_DESCRIPTION.getDescription())));
    }

    @Test
    void shouldReturnSuccessFalseAndItemNullWhenNoFileNoDescriptionUploaded() throws Exception {

        MockMultipartFile mockFile = getEmptyFileModel();

        mockMvc.perform(multipart("/upload")
                .file(mockFile)
                .param("description", DESCRIPTION_PARAM_NULL))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.success", equalTo(UPLOAD_SUCCESS_FALSE)))
                .andExpect(jsonPath("$.item", is(nullValue())));

        verify(itemService, times(0)).store(any(ItemDTO.class));
    }

    @Test
    void shouldReturnSuccessFalseAndItemNullWhenNoFileButDescriptionUploaded() throws Exception {

        MockMultipartFile mockFile = getEmptyFileModel();

        mockMvc.perform(multipart("/upload")
                .file(mockFile)
                .param("description", DESCRIPTION_PARAM_EXISTING))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.success", equalTo(UPLOAD_SUCCESS_FALSE)))
                .andExpect(jsonPath("$.item", is(nullValue())));

        verify(itemService, times(0)).store(any(ItemDTO.class));
    }

    private MockMultipartFile getFileModel(ItemDTO itemDTO) throws JsonProcessingException {
        return new MockMultipartFile("file", itemDTO.getName(), MediaType.TEXT_PLAIN_VALUE, asJSONString(itemDTO).getBytes());
    }

    private MockMultipartFile getEmptyFileModel() throws JsonProcessingException {
        return new MockMultipartFile("file", "", MediaType.TEXT_PLAIN_VALUE, new byte[] {});
    }
}