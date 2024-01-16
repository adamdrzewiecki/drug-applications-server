package org.example.openfda.utils;

import org.example.model.DrugRecordApplication;
import org.example.model.DrugRecordApplicationDto;
import org.example.model.DrugRecordApplicationRequest;

public class DrugRecordApplicationMapper {

    private DrugRecordApplicationMapper() {
        // Empty
    }

    public static DrugRecordApplication requestToDocument(DrugRecordApplicationRequest request) {
        return new DrugRecordApplication(request.applicationNumber(), request.manufacturerName(), request.substanceName(), request.productNumbers());
    }

    public static DrugRecordApplicationDto documentToDto(DrugRecordApplication document) {
        return new DrugRecordApplicationDto(document.applicationNumber(), document.manufacturerName(), document.substanceName(), document.productNumbers());
    }
}
