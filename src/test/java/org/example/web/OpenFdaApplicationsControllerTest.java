package org.example.web;

import org.example.openfda.service.OpenFdaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.client.HttpClientErrorException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OpenFdaApplicationsController.class)
public class OpenFdaApplicationsControllerTest {

    private static final String OPENFDS_RESOURCE = "/openfda";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OpenFdaService openFdaService;

    @Test
    void shouldReturnUnauthorized() throws Exception {
        // then
        mockMvc.perform(get(OPENFDS_RESOURCE + "?manufacturerName="))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnBadRequestResponseWithValidationFailure() throws Exception {
        // then
        mockMvc.perform(get(OPENFDS_RESOURCE + "?manufacturerName=").header("Authorization", "Basic dGVzdFVzZXI6bTZTNXZTRTd1Rg=="))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isBadRequest())
            .andExpect(content().string("400 BAD_REQUEST \"Validation failure\""));
    }

    @Test
    void shouldReturnBadRequestResponseWithMessageFromOpenFdaApi() throws Exception {
        // given
        when(openFdaService.searchDrugApplicationInOpenFdaApi(any(), any(), any(), any()))
            .thenThrow(HttpClientErrorException.create("something went wrong", HttpStatus.BAD_REQUEST, "", null, null, null));
        // then
        mockMvc.perform(get(OPENFDS_RESOURCE + "?manufacturerName=test").header("Authorization", "Basic dGVzdFVzZXI6bTZTNXZTRTd1Rg==")
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isBadRequest())
            .andExpect(content().string("something went wrong"));
    }

    @Test
    void shouldReturnInternalServerErrorBadRequestResponseWithMessageFromOpenFdaApi() throws Exception {
        // given
        when(openFdaService.searchDrugApplicationInOpenFdaApi(any(), any(), any(), any())).thenThrow(new RuntimeException("test"));
        // then
        mockMvc.perform(get(OPENFDS_RESOURCE + "?manufacturerName=test").header("Authorization", "Basic dGVzdFVzZXI6bTZTNXZTRTd1Rg==")
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is5xxServerError())
            .andExpect(content().string("test"));
    }
}
