package io.quarkiverse.langchain4j.sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Path("/review")
public class ReviewResource {

    @Channel("reviews")
    Emitter<String> reviewsChannel;

    record Review(String review) {
    }

    record Message(String message) {
        @JsonCreator
        public Message {
        }
    }

    @POST
    public Message triage(Review review) {
        reviewsChannel.send(review.review());
        return new Message("Thank you very much for your feedback");
    }

}
