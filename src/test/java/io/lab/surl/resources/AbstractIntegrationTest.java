package io.lab.surl.resources;

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.util.Duration;
import io.lab.surl.db.DatabaseManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

public abstract class AbstractIntegrationTest {

    private static Client client;
    private static DatabaseManager manager;

    @BeforeClass
    public static void beforeClass() {
        client = getClient();
        manager = new DatabaseManager(
            ApplicationIntegrationRule.getInstance().getConfiguration(),
            ApplicationIntegrationRule.getInstance().getEnvironment()
        );
    }

    @ClassRule
    public static final TestRule TEST_RULE = RuleChain.outerRule(ApplicationIntegrationRule.getInstance());

    protected static Client getClient() {
        if (client == null) {
            JerseyClientConfiguration configuration = new JerseyClientConfiguration();
            configuration.setTimeout(Duration.seconds(5L));
            client =
                new JerseyClientBuilder(
                    ApplicationIntegrationRule.getInstance().getEnvironment())
                    .using(configuration)
                    .build("");
        }
        return client;
    }

    protected WebTarget createWebTarget() {
        return getClient().target("http://localhost:" + ApplicationIntegrationRule.getInstance().getLocalPort());
    }

    protected DatabaseManager getDatabaseManager(){
        return manager;
    }


}
