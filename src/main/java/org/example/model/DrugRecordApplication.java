package org.example.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public record DrugRecordApplication(
    @Id String applicationNumber,
    String manufacturerName,
    String substanceName,
    List<String> productNumbers) {
}
