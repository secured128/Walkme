package com.walkme.codingtest;

import com.walkme.codingtest.model.Campaign;
import com.walkme.codingtest.model.CampaignsCapsLayer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

/**
 * Implements entry point to HTTP requests in asynchronous way while validating correctness of the GET call parameters
 */
public class MainVerticle extends AbstractVerticle {


    @Override
    public void start(Future<Void> fut) {

        Router router = Router.router(vertx);

        router.get("/my-mibile-platform-api/campaigns?:user_id").handler(this::getCampaigns);

        vertx
            .createHttpServer()
            .requestHandler(router::accept)
            .listen(
                // Retrieve the port from the configuration,
                // default to 8080.
                config().getInteger("http.port", 8080),
                result -> {
                    if (result.succeeded()) {
                        fut.complete();
                    } else {
                        fut.fail(result.cause());
                    }
                }
            );
    }


    private void getCampaigns(RoutingContext routingContext) {
        final String user_id = routingContext.request().getParam("user_id");
        if (user_id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            final Integer userId = Integer.valueOf(user_id);
            List<Campaign> campaigns = CampaignsCapsLayer.getInstance().getCampains(userId);
            if (campaigns == null) {
                routingContext.response().setStatusCode(404).end();
            } else {
                routingContext.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(campaigns));
            }
        }
    }


}
