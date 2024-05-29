package com.devcv.member.Presentation;

import com.devcv.restdocs.AbstractRestDocsTests;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(memberController.class)
class memberControllerTest extends AbstractRestDocsTests {

    @Test
    void RestDocsTest() throws Exception {
        mockMvc.perform(get("/restDocsTest")).andExpect(status().isOk());
    }
}