package org.acme;

import static dev.langchain4j.data.message.UserMessage.userMessage;
import static dev.langchain4j.model.openai.OpenAiModelName.GPT_3_5_TURBO;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.Tokenizer;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.model.output.Response;

@Path("/code")
public class DeveloperResource {

    @Inject
    private ChatLanguageModel model;

    @GET
    @Path("/rest")
    @Produces(MediaType.TEXT_PLAIN)
    public void createRestEndpoint() {

        Tokenizer tokenizer = new OpenAiTokenizer(GPT_3_5_TURBO);
        ChatMemory chatMemory = TokenWindowChatMemory.withMaxTokens(1000, tokenizer);

        UserMessage userMessage1 = userMessage(
                "How to write a REST endpoint in Java? ");
        chatMemory.add(userMessage1);

        System.out.println("[User]: " + userMessage1.text() + System.lineSeparator());

        final Response<AiMessage> response1 = model.generate(chatMemory.messages());
        chatMemory.add(response1.content());

        System.out.println("[LLM]: " + response1.content().text() + System.lineSeparator());

        UserMessage userMessage2 = userMessage(
                "Create a test of the first point? " +
                        "Be short, 15 lines of code maximum.");
        chatMemory.add(userMessage2);

        System.out.println("[User]: " + userMessage2.text() + System.lineSeparator());

        final Response<AiMessage> response2 = model.generate(chatMemory.messages());

        System.out.println("[LLM]: " + response2.content().text() + System.lineSeparator());

    }

    @GET
    @Path("/k8s")
    @Produces(MediaType.TEXT_PLAIN)
    public void generateKubernetes() {

        ConversationalChain chain = ConversationalChain.builder()
                .chatLanguageModel(model)
                .build();

        String userMessage1 = "Can you give a brief explanation of Kubernetes, 3 lines max?";
        System.out.println("[User]: " + userMessage1 + System.lineSeparator());

        String answer1 = chain.execute(userMessage1);
        System.out.println("[LLM]: " + answer1 + System.lineSeparator());

        String userMessage2 = "Can you give me a YAML example to deploy an application for that?";
        System.out.println("[User]: " + userMessage2 + System.lineSeparator());

        String answer2 = chain.execute(userMessage2);
        System.out.println("[LLM]: " + answer2);

    }

    @Inject
    Assistant assistant;

    @GET
    @Path("/guess")
    @Produces(MediaType.TEXT_PLAIN)
    public void guessWho() {

        System.out.println(assistant.chat(1, "Hello, my name is Klaus"));

        System.out.println(assistant.chat(2, "Hello, my name is Francine"));

        System.out.println(assistant.chat(1, "What is my name?"));

        System.out.println(assistant.chat(2, "What is my name?"));

    }
}
