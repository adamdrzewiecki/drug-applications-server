package org.example.openfda.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Submission(
    @JsonProperty("submission_type") String type,
    @JsonProperty("submission_number") String number,
    @JsonProperty("submission_status") String status,
    @JsonProperty("submission_status_date") String statusDate,
    @JsonProperty("submission_class_code") String classCode,
    @JsonProperty("submission_class_code_description") String classCodeDescription) {
}
