package org.example.openfda.utils;

import org.example.openfda.model.OpenFdaResult;
import org.example.openfda.model.OpenFdaResultPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class OpenFdaResultUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenFdaResultUtils.class);

    private OpenFdaResultUtils() {
        // Empty
    }

    public static OpenFdaResultPage entityToPage(ResponseEntity<OpenFdaResult> openFdaResultResponseEntity) {
        OpenFdaResult openFdaResult = Optional.ofNullable(openFdaResultResponseEntity.getBody()).orElseThrow(() -> new NoSuchElementException("OpenFda result body is empty"));

        Optional<String> nextPageLink = getNextPageLinkFromHeader(openFdaResultResponseEntity);
        Map<String, String> nextPageDetails = nextPageLink.map(OpenFdaResultUtils::buildNextPageRequestParams).orElse(null);

        return new OpenFdaResultPage(openFdaResult, nextPageDetails);
    }

    private static Map<String, String> buildNextPageRequestParams(String nextPageLink) {
        Map<String, String> nextPageRequestParams = new HashMap<>();

        Pattern manufacturerNamePattern = Pattern.compile("(?s)(?<=openfda.manufacturer_name%3A)(.*?)((?=&limit)|(?=%20AND%20)|(?=&skip)|(?=&search_after)|(?=>))");
        Matcher matcher1 = manufacturerNamePattern.matcher(nextPageLink);
        if (matcher1.find()) {
            LOGGER.debug("manufacturerName: {}", matcher1.group(1));
            nextPageRequestParams.put("manufacturerName", matcher1.group(1));
        }

        Pattern brandNamePattern = Pattern.compile("(?s)(?<=openfda.brand_name%3A)(.*?)((?=&limit)|(?=%20AND%20)|(?=&skip)|(?=&search_after)|(?=>))");
        Matcher matcher2 = brandNamePattern.matcher(nextPageLink);
        if (matcher2.find()) {
            LOGGER.debug("brandName: {}", matcher2.group(1));
            nextPageRequestParams.put("brandName", matcher2.group(1));
        }

        Pattern limitPattern = Pattern.compile("(?s)(?<=&limit=)(.*?)((?=%20AND%20)|(?=&skip)|(?=&search_after)|(?=>))");
        Matcher matcher3 = limitPattern.matcher(nextPageLink);
        if (matcher3.find()) {
            LOGGER.debug("limit: {}", matcher3.group(1));
            nextPageRequestParams.put("limit", matcher3.group(1));
        }

        Pattern searchAfterPattern = Pattern.compile("(?s)(?<=search_after=)(.*?)((?=>)|(?=&limit)|(?=&skip))");
        Matcher matcher4 = searchAfterPattern.matcher(nextPageLink);
        if (matcher4.find()) {
            LOGGER.debug("searchAfter: {}", matcher4.group(1));
            nextPageRequestParams.put("searchAfter", matcher4.group(1));
        }

        return nextPageRequestParams;
    }

    private static Optional<String> getNextPageLinkFromHeader(ResponseEntity<OpenFdaResult> responseEntity) {
        return responseEntity.getHeaders().entrySet().stream()
            .filter(entry -> entry.getKey().equalsIgnoreCase("Link"))
            .map(entry -> entry.getValue().get(0))
            .findFirst();
    }
}
