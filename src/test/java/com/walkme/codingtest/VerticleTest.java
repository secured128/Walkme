package com.walkme.codingtest;


import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(VertxUnitRunner.class)
public class VerticleTest {

    private Vertx vertx;
    int port = 8080;
    DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("http.port", port));

    @Before
    public void setUp(TestContext context) throws IOException {

        vertx = Vertx.vertx();
        vertx.deployVerticle(MainVerticle.class.getName(), options,
            context.asyncAssertSuccess());
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testMyApplication(TestContext context) {
        final Async async = context.async();
        vertx.createHttpClient().getNow(port, "localhost", "/my-mibile-platform-api/campaigns?user_id=1",
            response -> {
                response.handler(body -> {
                    context.assertTrue(body != null && body.length() > 0);
                    System.out.println(body.toString());
                    async.complete();
                });
            });
    }
}
