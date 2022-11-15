package be.howest.ti.monopoly.web;

import be.howest.ti.monopoly.logic.ServiceAdapter;
import be.howest.ti.monopoly.logic.implementation.turnmanagement.BuyStatus;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;


class OpenApiBuyingPropertyTests extends OpenApiTestsBase {

    @Test
    void buyProperty(final VertxTestContext testContext) {
        service.setDelegate(new ServiceAdapter() {
            @Override
            public BuyStatus buyProperty(String gameId, String playerName, String propertyName) {
                return new BuyStatus("Some property", "Bought successfully");
            }
        });
        post(
                testContext,
                "/games/group24_0/players/Alice/properties/some-property",
                "group24_0-Alice",
                this::assertOkResponse
        );
    }

    @Test
    void buyPropertyUnauthorized(final VertxTestContext testContext) {
        post(
                testContext,
                "/games/game-id/players/Alice/properties/some-property",
                null,
                response -> assertErrorResponse(response, 401)
        );
    }

    @Test
    void dontBuyProperty(final VertxTestContext testContext) {
        service.setDelegate(new ServiceAdapter() {
            @Override
            public BuyStatus dontBuyProperty(String gameId, String playerName, String propertyName) {
                return new BuyStatus("Some property", "You decided to not buy this property");
            }
        });
        delete(
                testContext,
                "/games/group24_0/players/Alice/properties/some-property",
                "group24_0-Alice",
                this::assertOkResponse
        );
    }

    @Test
    void dontBuyPropertyUnauthorized(final VertxTestContext testContext) {
        delete(
                testContext,
                "/games/game-id/players/Alice/properties/some-property",
                null,
                response -> assertErrorResponse(response, 401)
        );
    }
}
