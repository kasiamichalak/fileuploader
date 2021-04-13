package pl.casmic.fileuploader.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hibernate.id.UUIDGenerator;
import org.junit.jupiter.api.Disabled;
import org.springframework.data.rest.webmvc.json.JacksonSerializers;
import org.springframework.hateoas.mediatype.collectionjson.Jackson2CollectionJsonModule;

public abstract class AbstractItemControllerTest {

    public static String asJSONString(final Object obj) {
        try {
            return new ObjectMapper()
//                    .registerModule(new JavaTimeModule())
                    .writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
