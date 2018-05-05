package com.api.vertxApi2.historico;

import java.util.List;

import com.api.vertxApi2.entidades.Historico;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLClient;

public class HistoricoVerticle extends AbstractVerticle {

	private EventBus eventBus;
	private SQLClient client;

	// @Override
	public void start(Future<Void> future) {

		JsonObject mySQLClientConfig = new JsonObject().put("host", "127.0.0.1").put("username", "root")
				.putNull("password").put("database", "api");
		this.client = MySQLClient.createNonShared(vertx, mySQLClientConfig);

		
		
		this.eventBus = getVertx().eventBus();

		this.eventBus.<JsonObject>consumer("addHistorico", msg -> {

			JsonObject historicoJson = msg.body();
			String query = "'" + historicoJson.getInteger("idUsuario") + "'" + "," + "'"
					+ historicoJson.getString("action") + "'" + "," + "'" + historicoJson.getString("dataAction") + "'";

			this.client.getConnection(conn -> {
				conn.result().execute("INSERT INTO historicos(idusuario, action, dataAction) VALUES(" + query + ")",
						insert -> {
							if (insert.failed()) {
								conn.result().close();
							}
							msg.reply(historicoJson);
						});
			});
		});

		this.eventBus.<JsonObject>consumer("getAllHistorico", msg -> {
			JsonObject historicoJson = msg.body();
			this.client.getConnection(conn -> {
				conn.result().query(
						"SELECT * from historicos WHERE idUsuario = " + historicoJson.getString("idUsuario"),
						select -> {
							if (select.failed()) {
								conn.result().close();
							}
//							
							JsonArray arrayResults = new JsonArray(select.result().getRows());
							JsonObject allResult = new JsonObject()
									.put("t1", arrayResults);
							msg.reply(allResult);
						});
			});
		});

		this.eventBus.<JsonObject>consumer("deleteHistorico", msg -> {
			JsonObject json = msg.body();
			this.client.getConnection(conn -> {			
				conn.result().query("DELETE FROM historicos WHERE id = "+json.getString("id"), delete -> {
					if(delete.failed()) {
						conn.result().close();
					}
					
					msg.reply("");
				});
			});
		});
	}
}
