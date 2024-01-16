package org.example.web;

import org.example.model.DrugRecordApplicationDto;
import org.example.model.DrugRecordApplicationRequest;
import org.example.service.DrugRecordApplicationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/drugs-applications")
public class DrugRecordApplicationsController {

    private final DrugRecordApplicationService drugRecordApplicationService;

    public DrugRecordApplicationsController(DrugRecordApplicationService drugRecordApplicationService) {
        this.drugRecordApplicationService = drugRecordApplicationService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveDrugRecordApplication(@RequestBody DrugRecordApplicationRequest drugRecordApplicationRequest) {
        String id = drugRecordApplicationService.storeDrugRecordApplication(drugRecordApplicationRequest);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri()).build();
    }

    @GetMapping
    public List<DrugRecordApplicationDto> getAllDrugRecordApplications() {
        return drugRecordApplicationService.getAllDrugRecordApplications();
    }

    @GetMapping("/{id}")
    public DrugRecordApplicationDto getDrugRecordApplicationById(@PathVariable String id) {
        return drugRecordApplicationService.getDrugRecordApplicationById(id);
    }
}
