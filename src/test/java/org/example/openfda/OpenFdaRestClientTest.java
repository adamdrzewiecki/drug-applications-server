package org.example.openfda;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.example.openfda.model.OpenFdaResultPage;
import org.example.openfda.service.OpenFdaRestClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class OpenFdaRestClientTest {

    private static final String MANUFACTURER_NAME = "amneal";
    private static final String BRAND_NAME = "ibuprofen";

    private OpenFdaRestClient openFdaRestClient;
    private MockWebServer mockWebServer;

    @BeforeEach
    void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        String mockWebServerUrl = mockWebServer.url("/").url().toString();
        openFdaRestClient = new OpenFdaRestClient(mockWebServerUrl);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void shouldThrowNoSuchElementExceptionForEmptyManufacturerName() {
        // then
        assertThatExceptionOfType(NoSuchElementException.class)
            .isThrownBy(() -> openFdaRestClient.searchDrugApplicationInOpenFdaApi(null, null, null, null))
            .withMessage("Mandatory field (manufacturerName) is missing");
    }

    @Test
    void shouldThrowRuntimeExceptionOn400StatusCodeFromDrugsApi() {
        //given
        mockWebServer.enqueue(new MockResponse().setResponseCode(400).setBody("Bad request"));
        // then
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
            .isThrownBy(() -> openFdaRestClient.searchDrugApplicationInOpenFdaApi(MANUFACTURER_NAME, BRAND_NAME, null, null))
            .withMessage("400 Bad Request: \"Bad request\"");
    }

    @Test
    void shouldThrowRuntimeExceptionOn500StatusCodeFromDrugsApi() {
        //given
        mockWebServer.enqueue(new MockResponse().setResponseCode(500).setBody("Fatal error"));
        // then
        assertThatExceptionOfType(HttpServerErrorException.InternalServerError.class)
            .isThrownBy(() -> openFdaRestClient.searchDrugApplicationInOpenFdaApi(MANUFACTURER_NAME, BRAND_NAME, null, null))
            .withMessage("500 Internal Server Error: \"Fatal error\"");
    }

    @Test
    void shouldThrowNoSuchElementExceptionForEmptyManufacturerNameForMissingResponseBody() {
        // given
        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
        );
        // then
        assertThatExceptionOfType(NoSuchElementException.class)
            .isThrownBy(() -> openFdaRestClient.searchDrugApplicationInOpenFdaApi(MANUFACTURER_NAME, null, null, null))
            .withMessage("OpenFda result body is empty");
    }

    @Test
    void shouldReturnDrugApplicationsWithoutNextPageDetailsForMissingLinkHeader() {
        // given
        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
            .setBody(getTestResponse())
        );
        // when
        OpenFdaResultPage openFdaResultPage = openFdaRestClient.searchDrugApplicationInOpenFdaApi(MANUFACTURER_NAME, BRAND_NAME, null, null);
        // then
        assertThat(openFdaResultPage).isNotNull();
        assertThat(openFdaResultPage.result()).isNotNull();
        assertThat(openFdaResultPage.nextPageRequestParams()).isNull();
    }

    @Test
    void shouldReturnDrugApplicationsWithNextPageDetails() {
        // given
        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
            .setHeader(HttpHeaders.LINK, "<https://testServer.com?search=openfda.manufacturer_name%3A" + MANUFACTURER_NAME + "%20AND%20openfda.brand_name%3A" + BRAND_NAME + "&search_after=0%3DUFDXAI0BCI5zb-xTDTgb&skip=0&limit=1>; rel=\"next\"")
            .setBody(getTestResponse())
        );
        // when
        OpenFdaResultPage openFdaResultPage = openFdaRestClient.searchDrugApplicationInOpenFdaApi(MANUFACTURER_NAME, BRAND_NAME, null, null);
        // then
        assertThat(openFdaResultPage).isNotNull();
        assertThat(openFdaResultPage.result()).isNotNull();
        assertThat(openFdaResultPage.nextPageRequestParams()).isNotNull();
        assertThat(openFdaResultPage.nextPageRequestParams().size()).isEqualTo(4);
    }

    @Test
    void shouldReturnDrugApplicationsWithEmptyNextPageDetailsForBrokenLinkHeader() {
        // given
        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
            .setHeader(HttpHeaders.LINK, "<https://testServer.com?search=")
            .setBody(getTestResponse())
        );
        // when
        OpenFdaResultPage openFdaResultPage = openFdaRestClient.searchDrugApplicationInOpenFdaApi(MANUFACTURER_NAME, BRAND_NAME, null, null);
        // then
        assertThat(openFdaResultPage).isNotNull();
        assertThat(openFdaResultPage.result()).isNotNull();
        assertThat(openFdaResultPage.nextPageRequestParams()).isNotNull();
        assertThat(openFdaResultPage.nextPageRequestParams().size()).isEqualTo(0);
    }

    private String getTestResponse() {
        return "{\n" +
            "  \"meta\": {\n" +
            "    \"last_updated\": \"2024-01-09\",\n" +
            "    \"results\": {\n" +
            "      \"skip\": 0,\n" +
            "      \"limit\": 1,\n" +
            "      \"total\": 10\n" +
            "    }\n" +
            "  },\n" +
            "  \"results\": [\n" +
            "    {\n" +
            "      \"submissions\": [\n" +
            "        {\n" +
            "          \"submission_type\": \"SUPPL\",\n" +
            "          \"submission_number\": \"12\",\n" +
            "          \"submission_status\": \"AP\",\n" +
            "          \"submission_status_date\": \"19881011\",\n" +
            "          \"submission_class_code\": \"LABELING\",\n" +
            "          \"submission_class_code_description\": \"Labeling\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"submission_type\": \"SUPPL\",\n" +
            "          \"submission_number\": \"4\",\n" +
            "          \"submission_status\": \"AP\",\n" +
            "          \"submission_status_date\": \"19871209\",\n" +
            "          \"submission_class_code\": \"MANUF (CMC)\",\n" +
            "          \"submission_class_code_description\": \"Manufacturing (CMC)\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"application_number\": \"ANDA071334\",\n" +
            "      \"sponsor_name\": \"AMNEAL PHARMS NY\",\n" +
            "      \"openfda\": {\n" +
            "        \"application_number\": [\n" +
            "          \"ANDA071334\"\n" +
            "        ],\n" +
            "        \"brand_name\": [\n" +
            "          \"IBUPROFEN\"\n" +
            "        ],\n" +
            "        \"generic_name\": [\n" +
            "          \"IBUPROFEN\"\n" +
            "        ],\n" +
            "        \"manufacturer_name\": [\n" +
            "          \"Amneal Pharmaceuticals of New York LLC\"\n" +
            "        ],\n" +
            "        \"product_ndc\": [\n" +
            "          \"53746-131\"\n" +
            "        ],\n" +
            "        \"product_type\": [\n" +
            "          \"HUMAN PRESCRIPTION DRUG\"\n" +
            "        ],\n" +
            "        \"route\": [\n" +
            "          \"ORAL\"\n" +
            "        ],\n" +
            "        \"substance_name\": [\n" +
            "          \"IBUPROFEN\"\n" +
            "        ],\n" +
            "        \"rxcui\": [\n" +
            "          \"197805\"\n" +
            "        ],\n" +
            "        \"spl_id\": [\n" +
            "          \"56a6bf62-e671-4670-9ca6-7f3814c77a41\"\n" +
            "        ],\n" +
            "        \"spl_set_id\": [\n" +
            "          \"57cdd1df-a303-4265-a2d6-1d0b87105a28\"\n" +
            "        ],\n" +
            "        \"package_ndc\": [\n" +
            "          \"53746-131-01\",\n" +
            "          \"53746-131-05\"\n" +
            "        ],\n" +
            "        \"nui\": [\n" +
            "          \"N0000000160\",\n" +
            "          \"M0001335\",\n" +
            "          \"N0000175722\"\n" +
            "        ],\n" +
            "        \"pharm_class_moa\": [\n" +
            "          \"Cyclooxygenase Inhibitors [MoA]\"\n" +
            "        ],\n" +
            "        \"pharm_class_cs\": [\n" +
            "          \"Anti-Inflammatory Agents, Non-Steroidal [CS]\"\n" +
            "        ],\n" +
            "        \"pharm_class_epc\": [\n" +
            "          \"Nonsteroidal Anti-inflammatory Drug [EPC]\"\n" +
            "        ],\n" +
            "        \"unii\": [\n" +
            "          \"WK2XYI10QM\"\n" +
            "        ]\n" +
            "      },\n" +
            "      \"products\": [\n" +
            "        {\n" +
            "          \"product_number\": \"001\",\n" +
            "          \"reference_drug\": \"No\",\n" +
            "          \"brand_name\": \"IBUPROFEN\",\n" +
            "          \"active_ingredients\": [\n" +
            "            {\n" +
            "              \"name\": \"IBUPROFEN\",\n" +
            "              \"strangth\": null\n" +
            "            }\n" +
            "          ],\n" +
            "          \"reference_standard\": \"No\",\n" +
            "          \"dosage_form\": \"TABLET\",\n" +
            "          \"route\": \"ORAL\",\n" +
            "          \"marketing_status\": \"Prescription\",\n" +
            "          \"te_code\": \"AB\"\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";
    }
}
