package org.example.openfda.service;

import jakarta.validation.constraints.NotBlank;
import org.example.openfda.model.OpenFdaResultPage;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public class OpenFdaService {

    private final OpenFdaClient openFdaClient;

    public OpenFdaService(OpenFdaClient openFdaClient) {
        this.openFdaClient = openFdaClient;
    }

    public OpenFdaResultPage searchDrugApplicationInOpenFdaApi(@NotBlank String manufacturerName,
                                                               @Nullable String brandName,
                                                               @Nullable Integer pageSize,
                                                               @Nullable String searchAfter) {
        return openFdaClient.searchDrugApplicationInOpenFdaApi(manufacturerName, brandName, pageSize, searchAfter);
    }
}
