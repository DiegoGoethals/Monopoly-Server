package be.howest.ti.monopoly.web;

import be.howest.ti.monopoly.logic.ServiceAdapter;
import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.GameManager;
import be.howest.ti.monopoly.logic.implementation.MonopolyService;
import be.howest.ti.monopoly.logic.implementation.Player;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;


class OpenApiTurnManagementTests extends OpenApiTestsBase {

    @Test
    void rollDice(final VertxTestContext testContext) {
        service.setDelegate(new ServiceAdapter() {
            @Override
            public Game rollDice(String gameId, String playerName) {
                Game game = new Game("group24", 2, new MonopolyService().getTiles());
                GameManager manager = new GameManager();
                manager.addGame(game);
                manager.joinGame(new Player("Alice"), "group24_0");
                manager.joinGame(new Player("Diego"), "group24_0");
                return game;
            }
        });
        post(
                testContext,
                "/games/group24_0/players/Alice/dice",
                "group24_0-Alice",
                this::assertOkResponse
        );
    }

    @Test
    void rollDiceUnauthorized(final VertxTestContext testContext) {
        post(
                testContext,
                "/games/game-id/players/Alice/dice",
                null,
                response -> assertErrorResponse(response, 401)
        );
    }

    @Test
    void declareBankruptcy(final VertxTestContext testContext) {
        service.setDelegate(new ServiceAdapter() {
            @Override
            public Game goBankrupt(String gameId, String playerName) {
                Game game = new Game("group24", 2, new MonopolyService().getTiles());
                GameManager manager = new GameManager();
                manager.addGame(game);
                manager.joinGame(new Player("Alice"), "group24_0");
                manager.joinGame(new Player("Diego"), "group24_0");
                manager.goBankrupt("group24_0", "Alice");
                return game;
            }
        });
        post(
                testContext,
                "/games/group24_0/players/Alice/bankruptcy",
                "group24_0-Alice",
                this::assertOkResponse
        );
    }

    @Test
    void declareBankruptcyUnauthorized(final VertxTestContext testContext) {
        post(
                testContext,
                "/games/game-id/players/Alice/bankruptcy",
                null,
                response -> assertErrorResponse(response, 401)
        );
    }
}
