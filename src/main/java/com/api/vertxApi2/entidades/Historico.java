package com.api.vertxApi2.entidades;

import io.vertx.core.json.JsonObject;

public class Historico {

	private int id;
	private int idUsuario;
	private String action;
	private String dataAction;
	
	public Historico(JsonObject data) {
		this.id = data.getInteger("id");
		this.idUsuario = data.getInteger("idUsuario");
		this.action = data.getString("action");
		this.dataAction = data.getString("dataAction");
	}
	
	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Historico() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDataAction() {
		return dataAction;
	}

	public void setDataAction(String dataAction) {
		this.dataAction = dataAction;
	}
	
	
	
}
