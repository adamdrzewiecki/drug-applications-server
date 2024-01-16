package org.example.openfda.service;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.openfda.model.OpenFdaResult;
import org.example.openfda.model.OpenFdaResultPage;
import org.example.openfda.utils.OpenFdaResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.NoSuchElementException;

@Service
public class OpenFdaRestClient implements OpenFdaClient{

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenFdaRestClient.class);

    private final RestClient restClient;
    private final String openfdaDrugsUrl;

    public OpenFdaRestClient(@Value("${openfda.drugs.url}") String openfdaDrugsUrl) {
        restClient = RestClient.create();
        this.openfdaDrugsUrl = openfdaDrugsUrl;
    }

    @Override
    public OpenFdaResultPage searchDrugApplicationInOpenFdaApi(String manufacturerName, @Nullable String brandName, @Nullable Integer pageSize, @Nullable String searchAfter) {
        validateMandatoryFields(manufacturerName);

        ResponseEntity<OpenFdaResult> openFdaResultResponseEntity = restClient.get()
            .uri(buildUrl(manufacturerName, brandName, pageSize, searchAfter))
            .retrieve()
            .toEntity(OpenFdaResult.class);

        LOGGER.debug("OpenFdaResult: {}", openFdaResultResponseEntity);
        return OpenFdaResultUtils.entityToPage(openFdaResultResponseEntity);
    }

    private String buildUrl(String manufacturerName, String brandName, Integer pageSize, String searchAfter) {
        StringBuilder urlToBuild = new StringBuilder(openfdaDrugsUrl)
            .append("?search=openfda.manufacturer_name:%s".formatted(manufacturerName));

        if (StringUtils.isNotEmpty(brandName)) {
            urlToBuild.append(" AND openfda.brand_name:%s".formatted(brandName));
        }

        if (pageSize != null && pageSize>0) {
            urlToBuild.append("&limit=%s".formatted(pageSize.toString()));
        }

        if (StringUtils.isNotEmpty(searchAfter)) {
            urlToBuild.append("&search_after=%s".formatted(searchAfter));
        }

        LOGGER.debug("Built url: {}", urlToBuild);
        return urlToBuild.toString();
    }

    private static void validateMandatoryFields(String manufacturerName) {
        if (ObjectUtils.isEmpty(manufacturerName)) {
            throw new NoSuchElementException("Mandatory field (manufacturerName) is missing");
        }
    }
}
