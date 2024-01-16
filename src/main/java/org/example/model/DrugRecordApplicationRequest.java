package org.example.model;

import java.util.List;

public record DrugRecordApplicationRequest(
    String applicationNumber,
    String manufacturerName,
    String substanceName,
    List<String> productNumbers) {
}
