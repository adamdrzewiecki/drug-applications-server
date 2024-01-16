package org.example.openfda.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FdaMeta(@JsonProperty("last_updated") String lastUpdated, @JsonProperty("results") FdaMetaResults fdaMetaResults) {
}
