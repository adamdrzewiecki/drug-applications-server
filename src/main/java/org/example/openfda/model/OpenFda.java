package org.example.openfda.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OpenFda(
    @JsonProperty("application_number") List<String> applicationNumbers,
    @JsonProperty("brand_name") List<String> brandNames,
    @JsonProperty("generic_name") List<String> genericNames,
    @JsonProperty("manufacturer_name") List<String> manufacturerNames,
    @JsonProperty("product_ndc") List<String> productNdcs,
    @JsonProperty("product_type") List<String> productType,
    @JsonProperty("route") List<String> routes,
    @JsonProperty("substance_name") List<String> substanceNames,
    @JsonProperty("rxcui") List<String> rxcui,
    @JsonProperty("spl_id") List<String> splId,
    @JsonProperty("spl_set_id") List<String> splSetId,
    @JsonProperty("package_ndc") List<String> packageNdc,
    @JsonProperty("nui") List<String> nui,
    @JsonProperty("pharm_class_moa") List<String> pharmClassMoa,
    @JsonProperty("pharm_class_cs") List<String> pharmClassCs,
    @JsonProperty("pharm_class_epc") List<String> pharmClassEpc,
    @JsonProperty("unii") List<String> unii) {
}
