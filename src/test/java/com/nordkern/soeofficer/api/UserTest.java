package com.nordkern.soeofficer.api;

import static io.dropwizard.testing.FixtureHelpers.*;
import static org.fest.assertions.api.Assertions.assertThat;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

public class UserTest {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
/*
    @Test
    public void serializesToJSON() throws Exception {
        final User user = new User("TestUser", "testuser@test.com");
        assertThat(MAPPER.writeValueAsString(user))
                .isEqualTo(fixture("fixtures/user.json"));
    }
    */
}
