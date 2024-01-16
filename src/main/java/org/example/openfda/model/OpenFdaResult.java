package org.example.openfda.model;

import java.util.List;

public record OpenFdaResult(FdaMeta meta, List<FdaResult> results) {
}
