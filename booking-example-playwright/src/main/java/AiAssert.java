import org.assertj.core.api.AbstractAssert;

import jakarta.enterprise.inject.spi.CDI;

public class AiAssert extends AbstractAssert<AiAssert, String> {

    ValidateResponse validateResponse;


    protected AiAssert(String actual) {
        super(actual, AiAssert.class);

        // Injects AI Service from LangChain4J
        validateResponse = CDI.current().select(ValidateResponse.class).stream().findFirst().get();
    }

    public static AiAssert assertThat(String actual) {
        return new AiAssert(actual);
    }

    public AiAssert isSimilarTo(String expect) {
        // check that actual we want to make assertions on is not null.
        isNotNull();
    
        // check condition

        if (!validateResponse.isSimilar(actual, expect)) {
          failWithMessage("Expected meaning to be '<%s>' for '%s' be similar but was not", expect, actual);
        }
    
        // return the current assertion for method chaining
        return this;
      }
    
}
