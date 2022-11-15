package be.howest.ti.monopoly.web;

import be.howest.ti.monopoly.logic.ServiceAdapter;
import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.GameManager;
import be.howest.ti.monopoly.logic.implementation.MonopolyService;
import be.howest.ti.monopoly.logic.implementation.Player;
import be.howest.ti.monopoly.logic.implementation.tiles.Property;
import be.howest.ti.monopoly.logic.implementation.tiles.Street;
import be.howest.ti.monopoly.logic.implementation.tiles.Tile;
import be.howest.ti.monopoly.logic.implementation.turnmanagement.BuyStatus;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;

import java.util.List;


class OpenApiInteractionWithOtherPlayerTests extends OpenApiTestsBase {

    @Test
    void collectDebt(final VertxTestContext testContext) {
        service.setDelegate(new ServiceAdapter() {
            @Override
            public BuyStatus collectRent(String gameId, String playerName, String propertyName, String debtorName) {
                return new BuyStatus(propertyName, "success");
            }
        });
        delete(
                testContext,
                "/games/group24_0/players/Alice/properties/some-property/visitors/Bob/rent",
                "group24_0-Alice",
                this::assertOkResponse
        );
    }

    @Test
    void collectDebtUnauthorized(final VertxTestContext testContext) {
        delete(
                testContext,
                "/games/game-id/players/Alice/properties/some-property/visitors/Bob/rent",
                null,
                response -> assertErrorResponse(response, 401)
        );
    }


}
