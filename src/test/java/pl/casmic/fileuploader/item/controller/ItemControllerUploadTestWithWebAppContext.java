package pl.casmic.fileuploader.item.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.hibernate.id.UUIDGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.casmic.fileuploader.config.JsonViewResolver;
import pl.casmic.fileuploader.config.WebConfig;
import pl.casmic.fileuploader.item.ItemGeneratorForTests;
import pl.casmic.fileuploader.item.domain.Item;
import pl.casmic.fileuploader.item.dto.ItemDTO;
import pl.casmic.fileuploader.item.service.ItemServiceImpl;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static pl.casmic.fileuploader.item.ItemGeneratorForTests.*;

@SpringBootTest
class ItemControllerUploadTestWithWebAppContext extends AbstractItemControllerTest implements ItemGeneratorForTests {

    @Mock
    private ItemServiceImpl itemService;
    @InjectMocks
    private ItemHtmlController itemController;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    private static final String RESPONSE_FORMAT = "f";
    private static final String JSON = "json";
    private static final String HTML = "html";
    private static final String DESCRIPTION_PARAM_EXISTING = "description";
    private static final String DESCRIPTION_PARAM_NULL = null;
    private static final String DESCRIPTION_DEFAULT = "not provided";
    private static final String ID = UUID.randomUUID().toString();
    private static final LocalDate UPLOAD_DATE = LocalDate.of(2021, 04, 13);
    private static final Item ITEM = getExpectedItem(ID, UPLOAD_DATE);
    private final static ItemDTO ITEM_DTO_WITH_DESCRIPTION = getExpectedItemDTOFromItem(ITEM);
    private final static ItemDTO ITEM_DTO_DEFAULT_DESCRIPTION = getExpectedItemDTOFromItemWithDefaultDescription(ITEM, DESCRIPTION_DEFAULT);
    private final static ItemDTO ITEM_DTO_FIELDS_NULL = new ItemDTO();
    private static final boolean UPLOAD_SUCCESS_TRUE = true;
    private static final boolean UPLOAD_SUCCESS_FALSE = false;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }
@Disabled
    @Test
    void shouldUploadFileWithDescriptionAndSaveItToDBHtml() throws Exception {

        MockMultipartFile mockFile = getFileModel(ITEM_DTO_WITH_DESCRIPTION);

        when(itemService.store(any(ItemDTO.class))).thenReturn(ITEM_DTO_WITH_DESCRIPTION);

        mockMvc.perform(multipart("/upload")
                .file(mockFile)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param(RESPONSE_FORMAT, HTML)
                .param("description", DESCRIPTION_PARAM_EXISTING))
                .andExpect(status().isAccepted())
                .andExpect(view().name("item/upload"))
                .andExpect(model().attribute("success", UPLOAD_SUCCESS_TRUE))
                .andExpect(model().attribute("item", ITEM_DTO_WITH_DESCRIPTION))
                .andExpect(model().attributeExists("item"));

        verify(itemService, times(1)).store(any(ItemDTO.class));
    }
@Disabled
    @Test
    void shouldUploadFileWithDescriptionAndSaveItToDBJson() throws Exception {

        MockMultipartFile mockFile = getFileModel(ITEM_DTO_WITH_DESCRIPTION);

        when(itemService.store(any(ItemDTO.class))).thenReturn(ITEM_DTO_WITH_DESCRIPTION);

        mockMvc.perform(multipart("/upload")
                .file(mockFile)
                .param(RESPONSE_FORMAT, JSON)
                .param("description", DESCRIPTION_PARAM_EXISTING))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.success", equalTo(UPLOAD_SUCCESS_TRUE)))
                .andExpect(jsonPath("$.item.itemID", equalTo(ITEM_DTO_WITH_DESCRIPTION.getId())))
                .andExpect(jsonPath("$.item.itemName", equalTo(ITEM_DTO_WITH_DESCRIPTION.getName())))
                .andExpect(jsonPath("$.item.description", equalTo(ITEM_DTO_WITH_DESCRIPTION.getDescription())))
                .andExpect(jsonPath("$.item.date", equalTo(String.valueOf(ITEM_DTO_WITH_DESCRIPTION.getUploadDate()))));

        verify(itemService, times(1)).store(any(ItemDTO.class));
    }
@Disabled
    @Test
    void shouldUploadFileWithNoDescriptionAndSaveItToDBHtml() throws Exception {

        MockMultipartFile mockFile = getFileModel(ITEM_DTO_DEFAULT_DESCRIPTION);

        when(itemService.store(any(ItemDTO.class))).thenReturn(ITEM_DTO_DEFAULT_DESCRIPTION);

        mockMvc.perform(multipart("/upload")
                .file(mockFile)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param(RESPONSE_FORMAT, HTML)
                .param("description", DESCRIPTION_PARAM_NULL))
                .andExpect(status().isAccepted())
                .andExpect(view().name("item/upload"))
                .andExpect(model().attribute("success", UPLOAD_SUCCESS_TRUE))
                .andExpect(model().attribute("item", ITEM_DTO_DEFAULT_DESCRIPTION))
                .andExpect(model().attributeExists("item"));
    }
@Disabled
    @Test
    void shouldUploadFileWithNoDescriptionAndSaveItToDBJson() throws Exception {

        MockMultipartFile mockFile = getFileModel(ITEM_DTO_DEFAULT_DESCRIPTION);

        when(itemService.store(any(ItemDTO.class))).thenReturn(ITEM_DTO_DEFAULT_DESCRIPTION);

        mockMvc.perform(multipart("/upload")
                .file(mockFile)
                .param(RESPONSE_FORMAT, JSON)
                .param("description", DESCRIPTION_PARAM_NULL))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.success", equalTo(UPLOAD_SUCCESS_TRUE)))
                .andExpect(jsonPath("$.item.itemID", equalTo(ITEM_DTO_DEFAULT_DESCRIPTION.getId())))
                .andExpect(jsonPath("$.item.itemName", equalTo(ITEM_DTO_DEFAULT_DESCRIPTION.getName())))
                .andExpect(jsonPath("$.item.description", equalTo(ITEM_DTO_DEFAULT_DESCRIPTION.getDescription())))
                .andExpect(jsonPath("$.item.date", equalTo(String.valueOf(ITEM_DTO_DEFAULT_DESCRIPTION.getUploadDate()))));

        verify(itemService, times(1)).store(any(ItemDTO.class));
    }

    @Test
    void shouldReturnSuccessFalseAndItemNullWhenNoFileNoDescriptionUploadedHtml() throws Exception {

        MockMultipartFile mockFile = getEmptyFileModel();

        mockMvc.perform(multipart("/upload")
                .file(mockFile)
                .param(RESPONSE_FORMAT, HTML)
                .param("description", DESCRIPTION_PARAM_NULL))
                .andExpect(status().isAccepted())
                .andExpect(view().name("item/upload"))
                .andExpect(model().attribute("success", UPLOAD_SUCCESS_FALSE))
                .andExpect(model().attributeExists("item"));

        verify(itemService, times(0)).store(any(ItemDTO.class));
    }

    @Test
    void shouldReturnSuccessFalseAndItemNullWhenNoFileNoDescriptionUploadedJson() throws Exception {

        MockMultipartFile mockFile = getEmptyFileModel();

        mockMvc.perform(multipart("/upload")
                .file(mockFile)
                .param(RESPONSE_FORMAT, JSON)
                .param("description", DESCRIPTION_PARAM_NULL))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.success", equalTo(UPLOAD_SUCCESS_FALSE)))
//                .andExpect(jsonPath("$.item.itemID", is(nullValue())))
                .andExpect(jsonPath("$.item.itemName", is(nullValue())))
                .andExpect(jsonPath("$.item.description", is(nullValue())))
                .andExpect(jsonPath("$.item.date", is(nullValue())));

        verify(itemService, times(0)).store(any(ItemDTO.class));
    }

    @Test
    void shouldReturnSuccessFalseAndItemNullWhenNoFileButDescriptionUploadedHtml() throws Exception {

        MockMultipartFile mockFile = getEmptyFileModel();

        mockMvc.perform(multipart("/upload")
                .file(mockFile)
                .param(RESPONSE_FORMAT, HTML)
                .param("description", DESCRIPTION_PARAM_EXISTING))
                .andExpect(status().isAccepted())
                .andExpect(model().attribute("success", UPLOAD_SUCCESS_FALSE))
                .andExpect(model().attributeExists("item"));

        verify(itemService, times(0)).store(any(ItemDTO.class));
    }

    @Test
    void shouldReturnSuccessFalseAndItemNullWhenNoFileButDescriptionUploadedJson() throws Exception {

        MockMultipartFile mockFile = getEmptyFileModel();

        mockMvc.perform(multipart("/upload")
                .file(mockFile)
                .param(RESPONSE_FORMAT, JSON)
                .param("description", DESCRIPTION_PARAM_EXISTING))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.success", equalTo(UPLOAD_SUCCESS_FALSE)))
//                .andExpect(jsonPath("$.item.itemID", is(nullValue())))
                .andExpect(jsonPath("$.item.itemName", is(nullValue())))
                .andExpect(jsonPath("$.item.description", is(nullValue())))
                .andExpect(jsonPath("$.item.date", is(nullValue())));

        verify(itemService, times(0)).store(any(ItemDTO.class));
    }

    private MockMultipartFile getFileModel(ItemDTO itemDTO) throws JsonProcessingException {
        return new MockMultipartFile("file", itemDTO.getName(), MediaType.TEXT_PLAIN_VALUE, itemDTO.getData());
    }

    private MockMultipartFile getEmptyFileModel() throws JsonProcessingException {
        return new MockMultipartFile("file", "", MediaType.TEXT_PLAIN_VALUE, new byte[] {});
    }
}