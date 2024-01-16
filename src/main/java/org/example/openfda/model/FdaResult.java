package org.example.openfda.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record FdaResult(
    @JsonProperty("submissions") List<Submission> submissions,
    @JsonProperty("application_number") String applicationNumber,
    @JsonProperty("sponsor_name") String sponsorName,
    @JsonProperty("openfda") OpenFda openFda,
    @JsonProperty("products") List<Product> products) {
}
