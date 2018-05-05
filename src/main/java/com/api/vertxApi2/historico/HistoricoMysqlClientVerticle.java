package com.api.vertxApi2.historico;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLClient;

public class HistoricoMysqlClientVerticle extends AbstractVerticle{
	
//	private SQLClient cliente;
	private AsyncSQLClient client;
	
	@Override
	public void start(Future<Void> future) {
		JsonObject mySQLClientConfig = new JsonObject()
				.put("host", "127.0.0.1")
				.put("username", "root")
				.putNull("password")
				.put("database","api"); 
			
		this.client = MySQLClient.createNonShared(vertx, mySQLClientConfig);
		
		
	}
	
	
	
}
