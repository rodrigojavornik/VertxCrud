package com.api.vertxApi2.usuario;

import com.api.vertxApi2.entidades.Usuario;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.sql.*;
import java.util.List;

public class UsuarioController {

	private UsuarioMysqlClient mysqlClient;

	public UsuarioController(Vertx vertx) {
		this.mysqlClient = new UsuarioMysqlClient(vertx);
	}

	public void add(Usuario usuario, Handler<AsyncResult<Void>> handler) {	
		this.mysqlClient.add(usuario, result -> {
			if (result.failed()) {
				handler.handle(Future.failedFuture("failed"));
			}

			handler.handle(Future.succeededFuture());
		});
	}

	public void getAll(Handler<AsyncResult<List<JsonObject>>> handler) {
		this.mysqlClient.getAll(result -> {
			if (result.failed()) {
				handler.handle(Future.failedFuture("failed"));
			}

			handler.handle(Future.succeededFuture(result.result()));
		});
	}

	public void getById(HttpServerRequest request, Handler<AsyncResult<JsonObject>> handler) {
		String id = request.getParam("id");

		this.mysqlClient.getById(id, result -> {
			if (result.failed()) {
				handler.handle(Future.failedFuture("failed"));
			}

			handler.handle(Future.succeededFuture(result.result()));
		});

	}

	public void update(Usuario usuario, Handler<AsyncResult<String>> handler) {
		
		this.mysqlClient.update(usuario, result -> {
			if (result.failed()) {
				handler.handle(Future.failedFuture("failed"));
			}

			handler.handle(Future.succeededFuture());
		});
	}

	public void delete(HttpServerRequest request, Handler<AsyncResult<List<JsonObject>>> handler) {
		String id = request.getParam("id");

		this.mysqlClient.delete(id, result -> {
			if (result.failed()) {
				handler.handle(Future.failedFuture("failed"));
			}

			handler.handle(Future.succeededFuture(result.result()));
		});

	}
}
