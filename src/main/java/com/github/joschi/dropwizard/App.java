package com.github.joschi.dropwizard;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.setup.Environment;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;

public class App extends Application<App.Config> {
    public static void main(final String[] args) throws Exception {
        new App().run(args);
    }

    @Override
    public void run(final Config configuration,
                    final Environment environment) {
        ManagedDataSource dataSource = configuration.getDb().build(environment.metrics(), "");
        environment.jersey().register(new Resource(dataSource));
    }

    public static class Config extends Configuration {
        private final DataSourceFactory db;

        @JsonCreator
        public Config(@JsonProperty("db") DataSourceFactory db) {
            this.db = db;
        }

        public DataSourceFactory getDb() {
            return db;
        }
    }

    @Path("/")
    public static class Resource {
        private final ManagedDataSource dataSource;

        public Resource(ManagedDataSource dataSource) {
            this.dataSource = dataSource;
        }

        @GET
        @Produces(MediaType.TEXT_PLAIN)
        public String test() throws SQLException {
            try (final Connection connection = dataSource.getConnection()) {
                return Boolean.toString(connection.createStatement().execute("SELECT 1"));
            } catch (Exception e) {
                return "Error while running SQL statement";
            }
        }
    }
}
