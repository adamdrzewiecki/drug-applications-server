package org.example.openfda.service;

import org.example.openfda.model.OpenFdaResultPage;
import org.springframework.lang.Nullable;

public interface OpenFdaClient {

    OpenFdaResultPage searchDrugApplicationInOpenFdaApi(String manufacturerName, @Nullable String brandName, Integer pageSize, @Nullable String searchAfter);
}
