package com.github.joschi.dropwizard;

import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.core.MediaType;
import java.net.URISyntaxException;

import static io.dropwizard.util.Resources.getResource;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(DropwizardExtensionsSupport.class)
class AppTest {
    private final DropwizardAppExtension<App.Config> app =
            new DropwizardAppExtension<>(App.class, getResource("config.yml").getPath());

    AppTest() throws URISyntaxException {
    }

    @Test
    void test() {
        String uri = String.format("http://localhost:%d/", app.getLocalPort());
        String response = app.client().target(uri)
                .request(MediaType.TEXT_PLAIN_TYPE)
                .get(String.class);
        assertEquals(response, "Error while running SQL statement");
    }
}