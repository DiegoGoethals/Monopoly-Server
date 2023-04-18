package be.howest.ti.monopoly.web;

import be.howest.ti.monopoly.logic.IService;
import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;
import be.howest.ti.monopoly.logic.exceptions.InsufficientFundsException;
import be.howest.ti.monopoly.logic.exceptions.MonopolyResourceNotFoundException;
import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.MonopolyService;
import be.howest.ti.monopoly.logic.implementation.tiles.Tile;
import be.howest.ti.monopoly.web.exceptions.ForbiddenAccessException;
import be.howest.ti.monopoly.web.exceptions.InvalidRequestException;
import be.howest.ti.monopoly.web.exceptions.NotYetImplementedException;
import be.howest.ti.monopoly.web.tokens.MonopolyUser;
import be.howest.ti.monopoly.web.tokens.PlainTextTokens;
import be.howest.ti.monopoly.web.tokens.TokenManager;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BearerAuthHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.openapi.RouterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MonopolyApiBridge {

    private static final Logger LOGGER = Logger.getLogger(MonopolyApiBridge.class.getName());

    private final IService service;
    private final TokenManager tokenManager;

    public MonopolyApiBridge(IService service, TokenManager tokenManager) {
        this.service = service;
        this.tokenManager = tokenManager;
    }

    public MonopolyApiBridge() {
        this(
                new MonopolyService(),
                new PlainTextTokens()
        );
    }


    public Router buildRouter(RouterBuilder routerBuilder) {
        LOGGER.log(Level.INFO, "Installing CORS handlers");
        routerBuilder.rootHandler(createCorsHandler());

        LOGGER.log(Level.INFO, "Installing security handlers");
        routerBuilder.securityHandler("playerAuth", BearerAuthHandler.create(tokenManager));


        LOGGER.log(Level.INFO, "Installing Failure handlers");
        routerBuilder.operations().forEach(op -> op.failureHandler(this::onFailedRequest));

        LOGGER.log(Level.INFO, "Installing Actual handlers");

        // General Game and API Info
        routerBuilder.operation("getInfo").handler(this::getInfo);
        routerBuilder.operation("getTiles").handler(this::getTiles);
        routerBuilder.operation("getTile").handler(this::getTile);
        routerBuilder.operation("getChance").handler(this::getChance);
        routerBuilder.operation("getCommunityChest").handler(this::getCommunityChest);

        // Managing Games
        routerBuilder.operation("clearGameList").handler(this::clearGameList);
        routerBuilder.operation("getGames").handler(this::getGames);
        routerBuilder.operation("createGame").handler(this::createGame);
        routerBuilder.operation("joinGame").handler(this::joinGame);

        // Game Info
        routerBuilder.operation("getGame").handler(this::getGame);

        // Turn Management
        routerBuilder.operation("rollDice").handler(this::rollDice);
        routerBuilder.operation("declareBankruptcy").handler(this::declareBankruptcy);

        // Buying property
        routerBuilder.operation("buyProperty").handler(this::buyProperty);
        routerBuilder.operation("dontBuyProperty").handler(this::dontBuyProperty);

        // Improving property
        routerBuilder.operation("buyHouse").handler(this::buyHouse);
        routerBuilder.operation("sellHouse").handler(this::sellHouse);
        routerBuilder.operation("buyHotel").handler(this::buyHotel);
        routerBuilder.operation("sellHotel").handler(this::sellHotel);

        // Mortgage
        routerBuilder.operation("takeMortgage").handler(this::takeMortgage);
        routerBuilder.operation("settleMortgage").handler(this::settleMortgage);

        // Prison
        routerBuilder.operation("getOutOfJailFine").handler(this::getOutOfJailFine);
        routerBuilder.operation("getOutOfJailFree").handler(this::getOutOfJailFree);

        // Trading
        routerBuilder.operation("trade").handler(this::trade);

        LOGGER.log(Level.INFO, "All handlers are installed");
        return routerBuilder.createRouter();
    }

    private void getInfo(RoutingContext ctx) {
        Response.sendJsonResponse(ctx, 200, new JsonObject()
                .put("name", "monopoly")
                .put("version", service.getVersion())
        );
    }

    private void getTiles(RoutingContext ctx) {
        Response.sendJsonResponse(ctx, 200, service.getTiles());
    }

    private void getTile(RoutingContext ctx) {
        Request request = Request.from(ctx);
        Tile tile;

        if (request.isTileIndex()) {
            tile = service.getTile(request.getTileIndex());
        } else {
            tile = service.getTile(request.getTileName());
        }

        Response.sendJsonResponse(ctx, 200, tile);
    }

    private void getChance(RoutingContext ctx) {
        Response.sendJsonResponse(ctx, 200, service.getChance());
    }

    private void getCommunityChest(RoutingContext ctx) {
        Response.sendJsonResponse(ctx, 200, service.getCommunity());
    }

    private void createGame(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String prefix = request.getBodyPrefix();
        int numberOfPlayers = request.getBodyNumberOfPlayers();

        Response.sendJsonResponse(ctx, 200, service.createGame(prefix, numberOfPlayers));
    }

    private void clearGameList(RoutingContext ctx) {
        Request request = Request.from(ctx);
        Response.sendJsonResponse(ctx, 200, service.clearGames());
    }

    private void getGames(RoutingContext ctx) {
        Request request = Request.from(ctx);

        List<Game> games = service.getGames();
        List<Game> toSend = new ArrayList<>();
        for (Game game : games) {
            if (request.hasStarted() && request.getStarted() == game.isStarted() && !request.hasQueryNumberOfPlayers() &&
                !request.hasQueryPrefix()) {
                toSend.add(game);
            }
            else if (!request.hasStarted() && request.hasQueryNumberOfPlayers() && request.getQueryNumberOfPlayers() == game.getNumberOfPlayers() &&
                    !request.hasQueryPrefix()) {
                toSend.add(game);
            }
            else if (!request.hasStarted() && !request.hasQueryNumberOfPlayers() && request.hasQueryPrefix() &&
                    request.getQueryPrefix().equals(game.getId().substring(0, 7))) {
                toSend.add(game);
            }
            else if (request.hasStarted() && request.getStarted() == game.isStarted() && request.hasQueryNumberOfPlayers() &&
                    request.getQueryNumberOfPlayers() == game.getNumberOfPlayers() && !request.hasQueryPrefix()) {
                toSend.add(game);
            }
            else if (!request.hasStarted() && request.hasQueryNumberOfPlayers() && request.getQueryNumberOfPlayers() == game.getNumberOfPlayers() &&
                request.hasQueryPrefix() && request.getQueryPrefix().equals(game.getId().substring(0, 7))) {
                toSend.add(game);
            }
            else if (request.hasStarted() && request.getStarted() == game.isStarted() && !request.hasQueryNumberOfPlayers() &&
                request.hasQueryPrefix() && request.getQueryPrefix().equals(game.getId().substring(0, 7))) {
                toSend.add(game);
            }
            else if (request.hasStarted() && request.getStarted() == game.isStarted() && request.hasQueryNumberOfPlayers() &&
                request.getQueryNumberOfPlayers() == game.getNumberOfPlayers() && request.hasQueryPrefix() &&
                request.getQueryPrefix().equals(game.getId().substring(0, 7))) {
                toSend.add(game);
            }
            else if (!request.hasStarted() && !request.hasQueryNumberOfPlayers() && !request.hasQueryPrefix()) {
                toSend = games;
            }
        }
        Response.sendJsonResponse(ctx, 200, toSend);
    }

    private void joinGame(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String playerName = request.getBodyPlayerName();
        String gameId = request.getGameId();

        service.joinGame(playerName, gameId);

        String token = tokenManager.createToken(new MonopolyUser(gameId, playerName));

        Response.sendJsonResponse(ctx, 200, new JsonObject().put("token", token));
    }

    private void getGame(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String gameId = request.getGameId();

        Response.sendJsonResponse(ctx, 200, service.getGame(gameId));
    }

    private void rollDice(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String gameId = request.getGameId();
        String playerName = request.getPathPlayerName();

        if (!request.isAuthorized(gameId, playerName)) {
            throw new ForbiddenAccessException("You are not authorized to do this");
        }

        Response.sendJsonResponse(ctx, 200, service.rollDice(gameId, playerName));
    }

    private void declareBankruptcy(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String gameId = request.getGameId();
        String playerName = request.getPathPlayerName();

        if (!request.isAuthorized(gameId, playerName)) {
            throw new ForbiddenAccessException("You are not authorized to do this");
        }

        Response.sendJsonResponse(ctx, 200, service.goBankrupt(gameId, playerName));
    }

    private void buyProperty(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String gameId = request.getGameId();
        String playerName = request.getPathPlayerName();
        String propertyName = request.getPropertyName();

        if (!request.isAuthorized(gameId, playerName)) {
            throw new ForbiddenAccessException("You are not authorized to do this");
        }

        Response.sendJsonResponse(ctx, 200, service.buyProperty(gameId, playerName, propertyName));
    }

    private void dontBuyProperty(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String gameId = request.getGameId();
        String playerName = request.getPathPlayerName();
        String propertyName = request.getPropertyName();

        if (!request.isAuthorized(gameId, playerName)) {
            throw new ForbiddenAccessException("You are not authorized to do this");
        }

        Response.sendJsonResponse(ctx, 200, service.dontBuyProperty(gameId, playerName, propertyName));
    }

    private void takeMortgage(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String gameId = request.getGameId();
        String playerName = request.getPathPlayerName();
        String propertyName = request.getPropertyName();

        if (!request.isAuthorized(gameId, playerName)) {
            throw new ForbiddenAccessException("You are not authorized to do this");
        }

        Response.sendJsonResponse(ctx, 200, service.takeMortgage(gameId, playerName, propertyName));
    }

    private void settleMortgage(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String gameId = request.getGameId();
        String playerName = request.getPathPlayerName();
        String propertyName = request.getPropertyName();

        if (!request.isAuthorized(gameId, playerName)) {
            throw new ForbiddenAccessException("You are not authorized to do this");
        }

        Response.sendJsonResponse(ctx, 200, service.settleMortgage(gameId, playerName, propertyName));
    }

    private void buyHouse(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String gameId = request.getGameId();
        String playerName = request.getPathPlayerName();
        String propertyName = request.getPropertyName();

        if (!request.isAuthorized(gameId, playerName)) {
            throw new ForbiddenAccessException("You are not authorized to do this");
        }
        service.buyHouse(gameId, playerName, propertyName);

        Response.sendJsonResponse(ctx, 200, new JsonObject());
    }

    private void sellHouse(RoutingContext ctx) {
      Request request = Request.from(ctx);

      String gameId = request.getGameId();
      String playerName = request.getPathPlayerName();
      String propertyName = request.getPropertyName();

      if (!request.isAuthorized(gameId, playerName)) {
          throw new ForbiddenAccessException("You are not authorized to do this");
      }
      service.sellHouse(gameId, playerName, propertyName);

      Response.sendJsonResponse(ctx, 200, new JsonObject());
    }

    private void buyHotel(RoutingContext ctx) {
      Request request = Request.from(ctx);

      String gameId = request.getGameId();
      String playerName = request.getPathPlayerName();
      String propertyName = request.getPropertyName();

      if (!request.isAuthorized(gameId, playerName)) {
        throw new ForbiddenAccessException("You are not authorized to do this");
      }
      service.buyHotel(gameId, playerName, propertyName);

      Response.sendJsonResponse(ctx, 200, new JsonObject());
    }

    private void sellHotel(RoutingContext ctx) {
      Request request = Request.from(ctx);

      String gameId = request.getGameId();
      String playerName = request.getPathPlayerName();
      String propertyName = request.getPropertyName();

      if (!request.isAuthorized(gameId, playerName)) {
        throw new ForbiddenAccessException("You are not authorized to do this");
      }
      service.sellHotel(gameId, playerName, propertyName);

      Response.sendJsonResponse(ctx, 200, new JsonObject());
    }

    private void getOutOfJailFine(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String gameId = request.getGameId();
        String playerName = request.getPathPlayerName();

        if (!request.isAuthorized(gameId, playerName)) {
            throw new ForbiddenAccessException("You are not authorized to do this");
        }
        service.getOutOfJailFine(gameId, playerName);

        Response.sendJsonResponse(ctx, 200, new JsonObject());
    }

    private void getOutOfJailFree(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String gameId = request.getGameId();
        String playerName = request.getPathPlayerName();

        if (!request.isAuthorized(gameId, playerName)) {
            throw new ForbiddenAccessException("You are not authorized to do this");
        }
        service.getOutOfJailFree(gameId, playerName);

        Response.sendJsonResponse(ctx, 200, new JsonObject());
    }

    private void trade(RoutingContext ctx) {
      throw new NotYetImplementedException("trade");
    }

    private void onFailedRequest(RoutingContext ctx) {
        Throwable cause = ctx.failure();
        int code = ctx.statusCode();
        String quote = Objects.isNull(cause) ? "" + code : cause.getMessage();

        // Map custom runtime exceptions to a HTTP status code.
        LOGGER.log(Level.INFO, "Failed request", cause);
        if (cause instanceof InvalidRequestException) {
            code = 400;
        } else if (cause instanceof IllegalArgumentException) {
            code = 400;
        } else if (cause instanceof InsufficientFundsException) {
            code = 402;
        } else if (cause instanceof ForbiddenAccessException) {
            code = 403;
        } else if (cause instanceof MonopolyResourceNotFoundException) {
            code = 404;
        } else if (cause instanceof IllegalMonopolyActionException) {
            code = 409;
        } else if (cause instanceof NotYetImplementedException) {
            code = 501;
        } else {
            LOGGER.log(Level.WARNING, "Failed request", cause);
        }

        Response.sendFailure(ctx, code, quote);
    }

    private CorsHandler createCorsHandler() {
        return CorsHandler.create(".*.")
                .allowedHeader("x-requested-with")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("origin")
                .allowedHeader("Content-Type")
                .allowedHeader("accept")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedMethod(HttpMethod.PATCH)
                .allowedMethod(HttpMethod.DELETE)
                .allowedMethod(HttpMethod.PUT);
    }
}
