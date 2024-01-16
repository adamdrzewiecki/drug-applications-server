package org.example.web;

import org.example.service.DrugRecordApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DrugRecordApplicationsController.class)
public class DrugRecordApplicationsControllerTest {

    private static final String DRUGS_APPLICATIONS_RESOURCE = "/drugs-applications";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DrugRecordApplicationService drugRecordApplicationService;

    @Test
    void shouldReturnUnauthorized() throws Exception {
        // then
        mockMvc.perform(get(DRUGS_APPLICATIONS_RESOURCE))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnBadRequestResponseWithMessageFromOpenFdaApi() throws Exception {
        // given
        String id = "123";
        when(drugRecordApplicationService.getDrugRecordApplicationById(any()))
            .thenThrow(new NoSuchElementException("Cannot find drug record application with id: %s".formatted(id)));
        // then
        mockMvc.perform(get(DRUGS_APPLICATIONS_RESOURCE + "/" + id).header("Authorization", "Basic dGVzdFVzZXI6bTZTNXZTRTd1Rg==")
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Cannot find drug record application with id: " + id));
    }

    @Test
    void shouldReturnInternalServerErrorBadRequestResponseWithMessageFromOpenFdaApi() throws Exception {
        // given
        when(drugRecordApplicationService.getAllDrugRecordApplications()).thenThrow(new RuntimeException("fatal error"));
        // then
        mockMvc.perform(get(DRUGS_APPLICATIONS_RESOURCE).header("Authorization", "Basic dGVzdFVzZXI6bTZTNXZTRTd1Rg==")
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is5xxServerError())
            .andExpect(content().string("fatal error"));
    }
}
