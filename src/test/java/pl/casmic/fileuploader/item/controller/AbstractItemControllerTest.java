package pl.casmic.fileuploader.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

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
