package com.api.vertxApi2.server;

import java.awt.List;
import java.util.HashSet;
import java.util.Set;

import com.api.vertxApi2.entidades.Historico;
import com.api.vertxApi2.entidades.Usuario;
import com.api.vertxApi2.usuario.UsuarioController;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

public class ServerVerticle extends AbstractVerticle {

	private UsuarioController usuarioController;
	private EventBus eventBus;

	@Override
	public void start(Future<Void> future) {
		this.usuarioController = new UsuarioController(vertx);
		this.eventBus = getVertx().eventBus();

		Router router = Router.router(vertx);
		
		
		
		Set<String> allowedHeaders = new HashSet<>();
		
		allowedHeaders.add("x-requested-with");
		allowedHeaders.add("Access-Control-Allow-Origin");
		allowedHeaders.add("origin");
		allowedHeaders.add("Content-Type");
		allowedHeaders.add("accept");
		allowedHeaders.add("X-PINGARUNER");
		
		Set<HttpMethod> allowedMethods = new HashSet<>();
	    allowedMethods.add(HttpMethod.GET);
	    allowedMethods.add(HttpMethod.POST);
	    allowedMethods.add(HttpMethod.OPTIONS);
	    allowedMethods.add(HttpMethod.DELETE);
	    allowedMethods.add(HttpMethod.PATCH);
	    allowedMethods.add(HttpMethod.PUT);
	    
	    router.route().handler(CorsHandler.create("*").allowedHeaders(allowedHeaders).allowedMethods(allowedMethods));
	    

		router.route("/api/usuario*").handler(BodyHandler.create());
		router.post("/api/usuario").handler(this::addUsuario);
		router.get("/api/usuario").handler(this::getAllUsuario);
		router.get("/api/usuario/:id").handler(this::getUsuarioById);
		router.put("/api/usuario/:id").handler(this::updateUsuario);
		router.delete("/api/usuario/:id").handler(this::deleteUsuario);

		router.route("/api/historico*").handler(BodyHandler.create());
		router.post("/api/historico/:idUsuario").handler(this::addHistorico);
		router.get("/api/historico/:idUsuario").handler(this::getAllHistorico);
		router.delete("/api/historico/:id").handler(this::deleteHistorico);

		vertx.createHttpServer().requestHandler(router::accept).listen(config().getInteger("http.port", 9000),
				result -> {
					if (result.succeeded()) {
						future.complete();
					} else {
						future.fail(result.cause());
					}
				});
	}

	private void addUsuario(RoutingContext routingContext) {
		Usuario usuario = Json.decodeValue(routingContext.getBodyAsString(), Usuario.class);

		this.usuarioController.add(usuario, ar -> {
			if (ar.failed()) {
				routingContext.response().setStatusCode(400).end(Json.encodePrettily(ar.cause()));
			}
			routingContext.response().setStatusCode(201).end();
		});
	}

	private void getAllUsuario(RoutingContext routingContext) {
		this.usuarioController.getAll(ar -> {
			if (ar.failed()) {
				routingContext.response().setStatusCode(400).end(Json.encodePrettily(ar.cause()));
			}

			routingContext.response().setStatusCode(201).end(Json.encodePrettily(ar.result()));
		});
	}

	private void getUsuarioById(RoutingContext routingContext) {
		this.usuarioController.getById(routingContext.request(), ar -> {
			if (ar.failed()) {
				routingContext.response().setStatusCode(400).end(Json.encode(ar.cause()));
			}

			routingContext.response().setStatusCode(201).end(Json.encodePrettily(ar.result()));
		});
	}

	private void updateUsuario(RoutingContext routingContext) {
		Usuario usuario = Json.decodeValue(routingContext.getBodyAsString(), Usuario.class);
		
		this.usuarioController.update(usuario, ar -> {
			if (ar.failed()) {
				routingContext.response().setStatusCode(400).end(Json.encodePrettily(ar.cause()));
			}

			routingContext.response().setStatusCode(201).end();
		});
	}

	private void deleteUsuario(RoutingContext routingContext) {
		this.usuarioController.delete(routingContext.request(), ar -> {
			if (ar.failed()) {
				routingContext.response().setStatusCode(400).end(Json.encodePrettily(ar.cause()));
			}

			routingContext.response().setStatusCode(201).end();
		});
	}

	private void addHistorico(RoutingContext routingContext) {
		JsonObject json = routingContext.getBodyAsJson();		
		eventBus.<JsonObject>send("addHistorico", 
				json, 
				reply -> {

					if (reply.failed()) {
						routingContext.response().setStatusCode(400).end(Json.encodePrettily(reply.cause()));
					}

					routingContext.response().setStatusCode(201).end(Json.encode(reply.result().body()));

				});
	}

	private void getAllHistorico(RoutingContext routingContext) {
		eventBus.send("getAllHistorico",
				new JsonObject().put("idUsuario", routingContext.request().getParam("idUsuario")), reply -> {
					if (reply.failed()) {
						routingContext.response().setStatusCode(400).end(Json.encodePrettily(reply.cause()));
					}
					JsonObject json = new JsonObject(Json.encode(reply.result().body()));
					json.getJsonArray("t1");
					routingContext.response().setStatusCode(201).end(Json.encode(json.getJsonArray("t1")));
				});
	}

	private void deleteHistorico(RoutingContext routingContext) {
		eventBus.send("deleteHistorico", new JsonObject().put("id", routingContext.request().getParam("id")), reply -> {

			if (reply.failed()) {
				routingContext.response().setStatusCode(400).end(Json.encodePrettily(reply.cause()));
			}

			routingContext.response().setStatusCode(201).end();
		});
	}

}
