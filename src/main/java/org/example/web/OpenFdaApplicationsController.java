package org.example.web;

import jakarta.validation.constraints.NotBlank;
import org.example.openfda.model.OpenFdaResultPage;
import org.example.openfda.service.OpenFdaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/openfda")
public class OpenFdaApplicationsController {

    private final OpenFdaService openFdaService;

    public OpenFdaApplicationsController(OpenFdaService openFdaService) {
        this.openFdaService = openFdaService;
    }

    @GetMapping
    public OpenFdaResultPage searchDrugApplicationInOpenFdaApi(@NotBlank @RequestParam String manufacturerName,
                                                               @RequestParam(required = false) String brandName,
                                                               @RequestParam(required = false) Integer pageSize,
                                                               @RequestParam(required = false) String searchAfter) {
        return openFdaService.searchDrugApplicationInOpenFdaApi(manufacturerName, brandName, pageSize, searchAfter);
    }
}
