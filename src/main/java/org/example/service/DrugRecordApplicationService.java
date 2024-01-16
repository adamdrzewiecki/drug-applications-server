package org.example.service;

import org.example.model.DrugRecordApplication;
import org.example.model.DrugRecordApplicationDto;
import org.example.model.DrugRecordApplicationRequest;
import org.example.openfda.utils.DrugRecordApplicationMapper;
import org.example.repository.DrugRecordApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DrugRecordApplicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DrugRecordApplicationService.class);

    private final DrugRecordApplicationRepository drugRecordApplicationRepository;

    public DrugRecordApplicationService(DrugRecordApplicationRepository drugRecordApplicationRepository) {
        this.drugRecordApplicationRepository = drugRecordApplicationRepository;
    }

    public String storeDrugRecordApplication(DrugRecordApplicationRequest drugRecordApplicationRequest) {
        LOGGER.debug("Storing new drug record application request: {}", drugRecordApplicationRequest);
        DrugRecordApplication drugRecordApplication = DrugRecordApplicationMapper.requestToDocument(drugRecordApplicationRequest);
        DrugRecordApplication saved = drugRecordApplicationRepository.save(drugRecordApplication);
        LOGGER.debug("Stored with id: {}", saved.applicationNumber());
        return saved.applicationNumber();
    }

    public List<DrugRecordApplicationDto> getAllDrugRecordApplications() {
        return drugRecordApplicationRepository.findAll().stream().map(DrugRecordApplicationMapper::documentToDto).toList();
    }

    public DrugRecordApplicationDto getDrugRecordApplicationById(String id) {
        return drugRecordApplicationRepository.findById(id)
            .map(DrugRecordApplicationMapper::documentToDto)
            .orElseThrow(() -> new NoSuchElementException("Cannot find drug record application with id: %s".formatted(id)));
    }
}
