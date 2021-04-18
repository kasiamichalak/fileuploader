package pl.casmic.fileuploader.item.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import pl.casmic.fileuploader.item.ItemGeneratorForTests;
import pl.casmic.fileuploader.item.dto.ItemDTO;
import pl.casmic.fileuploader.item.service.ItemServiceImpl;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ItemHtmlController.class)
class ItemHtmlControllerUploadFileTest implements ItemGeneratorForTests {

    @MockBean
    private ItemServiceImpl itemService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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
                .andExpect(jsonPath("$.item.date", equalTo((int) ITEM_DTO.getUploadDate().toEpochMilli()), Integer.class));

        verify(itemService, times(1)).store(any(ItemDTO.class));
    }

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

        verify(itemService, times(1)).store(any(ItemDTO.class));
    }

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
                .andExpect(jsonPath("$.item.date", equalTo((int) ITEM_DTO.getUploadDate().toEpochMilli()), Integer.class));

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
                .andExpect(jsonPath("$.item.itemID", is(nullValue())))
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
                .andExpect(jsonPath("$.item.itemID", is(nullValue())))
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