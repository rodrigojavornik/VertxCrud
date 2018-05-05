package com.api.vertxApi2.usuario;

import com.api.vertxApi2.entidades.Usuario;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.MySQLClient;
//import io.vertx.ext.jdbc.JDBCClient;
//import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLClient;
//import io.vertx.ext.sql.SQLConnection;

//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Statement;
import java.util.List;

public class UsuarioMysqlClient {
	
	final SQLClient client;
	
	public UsuarioMysqlClient(Vertx vertx) {
		JsonObject mySQLClientConfig = new JsonObject()
				.put("host", "127.0.0.1")
				.put("username", "root")
				.putNull("password")
				.put("database","api"); 
		
		this.client = MySQLClient.createNonShared(vertx, mySQLClientConfig);
		
	}
	
	public void add(Usuario usuario, Handler<AsyncResult<Void>> handler) {
		this.client.getConnection(conn -> {			
			conn.result().execute("INSERT INTO usuarios(nome,email,telefone) VALUES("+ usuario.valuesForQuery() +")", h -> {
				if(h.failed()) {
					handler.handle(Future.failedFuture("error"));
					conn.result().close();
				}
				
				handler.handle(Future.succeededFuture());
			});
		});
	}
	
	public void getAll(Handler<AsyncResult<List<JsonObject>>> handler) {
		this.client.getConnection(conn -> {			
			conn.result().query("SELECT * FROM usuarios", select -> {
				if(select.failed()) {
					handler.handle(Future.failedFuture("error"));
				}
				
				List<JsonObject> ja = select.result().getRows();
				handler.handle(Future.succeededFuture(ja));
			});
		});
	}
	
	public void getById(String idUsuario, Handler<AsyncResult<JsonObject>> handler) {
		this.client.getConnection(conn -> {			
			conn.result().query("SELECT * FROM usuarios WHERE id = '"+idUsuario+"'", select -> {
				if(select.failed()) {
					handler.handle(Future.failedFuture("error"));
				}
				
				JsonObject ja = select.result().getRows().get(0);
				handler.handle(Future.succeededFuture(ja));
			});
		});
	}
	
	public void update(Usuario usuario, Handler<AsyncResult<String>> handler) {
		this.client.getConnection(conn -> {			
			conn.result().query("UPDATE usuarios SET nome = '"+ usuario.getNome() +"', email = '"+ usuario.getEmail() +"', telefone = '"+ usuario.getTelefone() +"' WHERE id = "+ usuario.getId(), update-> {
				if(update.failed()) {
					System.out.println("jdbc failed");
					handler.handle(Future.failedFuture("error"));
				}
				
				handler.handle(Future.succeededFuture());
			});
		});
	}
	
	public void delete(String idUsuario, Handler<AsyncResult<List<JsonObject>>> handler) {
		this.client.getConnection(conn -> {			
			conn.result().query("DELETE FROM usuarios WHERE id = '"+idUsuario+"'", delete -> {
				if(delete.failed()) {
					handler.handle(Future.failedFuture("error"));
				}
				
				handler.handle(Future.succeededFuture());
			});
		});
	}
	
	
}
