package com.api.vertxApi2.main;

import com.api.vertxApi2.historico.HistoricoVerticle;
import com.api.vertxApi2.server.ServerVerticle;
 
import io.vertx.core.Vertx;
 
public class Run {
    
    public static void main(String[] args) {
       Vertx vertx = Vertx.vertx();
 	   vertx.deployVerticle(new ServerVerticle());
 	   vertx.deployVerticle(new HistoricoVerticle());
 	   System.out.println(">>> Verticle iniciado e rodando na porta 9000");
    }
 
}