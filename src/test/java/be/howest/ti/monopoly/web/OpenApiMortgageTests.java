package be.howest.ti.monopoly.web;

import be.howest.ti.monopoly.logic.ServiceAdapter;
import be.howest.ti.monopoly.logic.implementation.*;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;


class OpenApiMortgageTests extends OpenApiTestsBase {

    @Test
    void takeMortgage(final VertxTestContext testContext) {
        service.setDelegate(new ServiceAdapter() {
             @Override
            public Game takeMortgage(String gameId, String playerName, String propertyName) {
                 GameManager games = new GameManager();
                 games.addGame(new Game("group24", 2, new MonopolyService().getTiles()));
                 games.joinGame(new Player("Alice"), "group24_0");
                 games.joinGame(new Player("Test"), "group24_0");
                 return games.getGameStorage().get(0);
             }
        });
        post(
                testContext,
                "/games/group24_0/players/Alice/properties/some-property/mortgage",
                "group24_0-Alice",
                this::assertOkResponse
        );
    }

    @Test
    void takeMortgageUnauthorized(final VertxTestContext testContext) {
        post(
                testContext,
                "/games/game-id/players/Alice/properties/some-property/mortgage",
                null,
                response -> assertErrorResponse(response, 401)
        );
    }

    @Test
    void settleMortgage(final VertxTestContext testContext) {
        service.setDelegate(new ServiceAdapter() {
            @Override
            public Game settleMortgage(String gameId, String playerName, String propertyName) {
                GameManager games = new GameManager();
                games.addGame(new Game("group24", 2, new MonopolyService().getTiles()));
                games.joinGame(new Player("Alice"), "group24_0");
                games.joinGame(new Player("Test"), "group24_0");
                return games.getGameStorage().get(0);
            }
        });
        delete(
                testContext,
                "/games/group24_0/players/Alice/properties/some-property/mortgage",
                "group24_0-Alice",
                this::assertOkResponse
        );
    }

    @Test
    void settleMortgageUnauthorized(final VertxTestContext testContext) {
        delete(
                testContext,
                "/games/game-id/players/Alice/properties/some-property/mortgage",
                null,
                response -> assertErrorResponse(response, 401)
        );
    }
}
