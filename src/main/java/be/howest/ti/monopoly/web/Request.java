package be.howest.ti.monopoly.web;

import be.howest.ti.monopoly.web.tokens.MonopolyUser;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;

import java.util.Objects;


/**
 * The Request class is responsible for translating information that is part of the
 * request into Java.
 *
 * For every piece of information that you need from the request, you should provide a method here.
 * You can find information in:
 * - the request path: params.pathParameter("some-param-name")
 * - the query-string: params.queryParameter("some-param-name")
 * Both return a `RequestParameter`, which can contain a string or an integer in our case.
 * The actual data can be retrieved using `getInteger()` or `getString()`, respectively.
 * You can check if it is an integer (or not) using `isNumber()`.
 *
 * Finally, some requests have a body. If present, the body will always be in the json format.
 * You can acces this body using: `params.body().getJsonObject()`.
 *
 * **TIP:** Make sure that al your methods have a unique name. For instance, there is a request
 * that consists of more than one "player name". You cannot use the method `getPlayerName()` for both,
 * you will need a second one with a different name.
 */
public class Request {

    private final RoutingContext ctx;
    private final RequestParameters params;
    private final MonopolyUser user;

    private Request(RoutingContext ctx) {
        this.ctx = ctx;
        this.params = ctx.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        this.user = (MonopolyUser) ctx.user();
    }

    public static Request from(RoutingContext ctx) {
        return new Request(ctx);
    }

    public RoutingContext getRoutingContext() {
        return ctx;
    }

    public RequestParameters getRequestParameters() {
        return params;
    }

    public boolean isAuthorized(String expectedGameId, String expectedPlayerName) {
        return Objects.equals(expectedGameId, user.getGameId()) &&
                Objects.equals(expectedPlayerName, user.getPlayerName());
    }

    public boolean isTileIndex() {
        return params.pathParameter("tileId").isNumber();
    }

    public int getTileIndex() {
        return params.pathParameter("tileId").getInteger();
    }

    public String getTileName() {
        return params.pathParameter("tileId").getString();
    }

    public String getBodyPrefix() {
        return params.body().getJsonObject().getString("prefix");
    }

    public int getBodyNumberOfPlayers() {
        return params.body().getJsonObject().getInteger("numberOfPlayers");
    }

    public boolean hasStarted() {
        return params.queryParameter("started") != null;
    }

    public boolean hasQueryNumberOfPlayers() {
        return params.queryParameter("numberOfPlayers") != null;
    }

    public boolean hasQueryPrefix() {
        return params.queryParameter("prefix") != null;
    }

    public boolean getStarted() {
        return params.queryParameter("started").getBoolean();
    }

    public Integer getQueryNumberOfPlayers() {
        return params.queryParameter("numberOfPlayers").getInteger();
    }

    public String getQueryPrefix() {
        return params.queryParameter("prefix").getString();
    }

    public String getBodyPlayerName() {
        return params.body().getJsonObject().getString("playerName");
    }

    public String getGameId() {
        return params.pathParameter("gameId").getString();
    }

    public String getPathPlayerName() {
        return params.pathParameter("playerName").getString();
    }

    public String getPropertyName() {
        return params.pathParameter("propertyName").getString();
    }

    public String getDebtorName() {
        return params.pathParameter("debtorName").getString();
    }

    public String getBidder() { return params.body().getJsonObject().getString("bidder"); }

    public int getBid() {
        return params.body().getJsonObject().getInteger("amount");
    }

    public int getDuration() {
        return params.body().getJsonObject().getInteger("duration");
    }

    public String getTradePartnerName() {
        return params.body().getJsonObject().getString("player");
    }

    public String getOfferProperty() {
        return params.body().getJsonObject().getJsonArray("offer").getString(0);
    }

    public int getOfferMoney() {
        return params.body().getJsonObject().getJsonArray("offer").getInteger(1);
    }

    public String getReturnProperty() {
        return params.body().getJsonObject().getJsonArray("return").getString(0);
    }

    public int getReturnMoney() {
        return params.body().getJsonObject().getJsonArray("return").getInteger(1);
    }

    public int getStartBid() { return params.body().getJsonObject().getInteger("start-bid"); }
}
