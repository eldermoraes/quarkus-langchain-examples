package org.acme;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import org.eclipse.microprofile.reactive.messaging.*;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReviewsMessagingApplication {

    private final MeterRegistry registry;
    private final Counter positiveCounter;
    private final Counter negativeCounter;

    public ReviewsMessagingApplication(MeterRegistry registry) {
        this.registry = registry;
        this.positiveCounter = this.registry.counter("reviews.positive", "type", "triage");
        this.negativeCounter = this.registry.counter("reviews.negative", "type", "triage");
    }

    @Inject
    TriageService triageService;

    @ActivateRequestContext
    @Incoming("reviews")
    @RunOnVirtualThread
    public void triage(String message) {
        final TriagedReview triagedReview = triageService.triage(message);

        switch(triagedReview.evaluation()) {
            case POSITIVE -> this.positiveCounter.increment();
            case NEGATIVE -> this.negativeCounter.increment();
        }

    }


}
