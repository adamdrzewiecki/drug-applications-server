package org.example.openfda.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Product(
    @JsonProperty("product_number") String productNumber,
    @JsonProperty("reference_drug") String referenceDrug,
    @JsonProperty("brand_name") String brandName,
    @JsonProperty("active_ingredients") List<ActiveIngredient> activeIngredients,
    @JsonProperty("reference_standard") String referenceStandard,
    @JsonProperty("dosage_form") String dosageForm,
    @JsonProperty("route") String route,
    @JsonProperty("marketing_status") String marketingStatus,
    @JsonProperty("te_code") String teCode) {
}
