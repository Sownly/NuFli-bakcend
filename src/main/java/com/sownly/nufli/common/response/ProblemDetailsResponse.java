package com.sownly.nufli.common.response;

import java.time.Instant;
import java.util.Map;

public record ProblemDetailsResponse(
    String type,
    String title,
    int status,
    String detail,
    String instance,
    Instant timestamp,
    Map<String, String> errors
) {
    public static ProblemDetailsResponse of(int status, String title, String detail, String instance) {
        return new ProblemDetailsResponse(
            "https://api.nufli.sownly.com/errors/" + title.toLowerCase().replace(' ', '-'),
            title,
            status,
            detail,
            instance,
            Instant.now(),
            null
        );
    }

    public static ProblemDetailsResponse ofValidation(int status, String detail, String instance, Map<String, String> errors) {
        return new ProblemDetailsResponse(
            "https://api.nufli.sownly.com/errors/validation-failed",
            "Validation Failed",
            status,
            detail,
            instance,
            Instant.now(),
            errors
        );
    }
}
