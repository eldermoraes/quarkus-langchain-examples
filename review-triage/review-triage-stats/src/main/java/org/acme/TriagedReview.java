package org.acme;

import com.fasterxml.jackson.annotation.JsonCreator;

public record TriagedReview(Evaluation evaluation, String message) {

    @JsonCreator
    public TriagedReview {
    }

}
