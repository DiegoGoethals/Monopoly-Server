package be.howest.ti.monopoly.web;

import be.howest.ti.monopoly.logic.ServiceAdapter;
import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.GameManager;
import be.howest.ti.monopoly.logic.implementation.MonopolyService;
import be.howest.ti.monopoly.logic.implementation.Player;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;


class OpenApiGameInfoTests extends OpenApiTestsBase {

    @Test
    void getGame(final VertxTestContext testContext) {
        service.setDelegate(new ServiceAdapter() {
            @Override
            public Game getGame(String gameId) {
                Game game = new Game("group24", 2, new MonopolyService().getTiles());
                GameManager manager = new GameManager();
                manager.addGame(game);
                manager.joinGame(new Player("Diego"), "group24_0");
                return manager.getGameStorage().get(0);
            }
        });
        get(
                testContext,
                "/games/group24_0",
                "group24_0-Diego",
                this::assertOkResponse
        );
    }

    @Test
    void getGameUnauthorized(final VertxTestContext testContext) {
        get(
                testContext,
                "/games/game-id",
                null,
                response -> assertErrorResponse(response, 401)
        );
    }

    @Test
    void getDummyGame(final VertxTestContext testContext) {
        get(
                testContext,
                "/games/dummy",
                null,
                response -> assertNotYetImplemented(response, "getDummyGame")
        );
    }
}
