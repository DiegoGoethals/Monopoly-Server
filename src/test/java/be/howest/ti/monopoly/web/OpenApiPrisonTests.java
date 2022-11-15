package be.howest.ti.monopoly.web;

import be.howest.ti.monopoly.logic.ServiceAdapter;
import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.GameManager;
import be.howest.ti.monopoly.logic.implementation.MonopolyService;
import be.howest.ti.monopoly.logic.implementation.Player;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;


class OpenApiPrisonTests extends OpenApiTestsBase {

    @Test
    void getOutOfJailFine(final VertxTestContext testContext) {
        service.setDelegate(new ServiceAdapter() {
            @Override
            public void getOutOfJailFine(String gameId, String playerName) {
                GameManager games = new GameManager();
                games.addGame(new Game("group24", 2, new MonopolyService().getTiles()));
                games.joinGame(new Player("Alice"), "group24_0");
                games.joinGame(new Player("a"), "group24_0");
                games.getOutOfJailFine("group24_0", "Alice");
            }
        });
        post(
                testContext,
                "/games/group24_0/prison/Alice/fine",
                "group24_0-Alice",
                response -> assertOkResponse(response)
        );
    }

    @Test
    void getOutOfJailFineUnauthorized(final VertxTestContext testContext) {
        post(
                testContext,
                "/games/game-id/prison/Alice/fine",
                null,
                response -> assertErrorResponse(response, 401)
        );
    }

    @Test
    void getOutOfJailFree(final VertxTestContext testContext) {
        service.setDelegate(new ServiceAdapter() {
            @Override
            public void getOutOfJailFree(String gameId, String playerName) {
                GameManager games = new GameManager();
                games.addGame(new Game("group24", 2, new MonopolyService().getTiles()));
                games.joinGame(new Player("Alice"), "group24_0");
                games.joinGame(new Player("a"), "group24_0");
                games.getOutOfJailFree("group24_0", "Alice");
            }
        });
        post(
                testContext,
                "/games/group24_0/prison/Alice/free",
                "group24_0-Alice",
                response -> assertOkResponse(response)
        );
    }

    @Test
    void getOutOfJailFreeUnauthorized(final VertxTestContext testContext) {
        post(
                testContext,
                "/games/game-id/prison/Alice/free",
                null,
                response -> assertErrorResponse(response, 401)
        );
    }
}
