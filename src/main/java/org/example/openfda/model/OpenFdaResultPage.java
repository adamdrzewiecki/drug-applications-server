package org.example.openfda.model;

import java.util.Map;

public record OpenFdaResultPage(OpenFdaResult result, Map<String, String> nextPageRequestParams) {
}
