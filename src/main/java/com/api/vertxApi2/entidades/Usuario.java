package com.api.vertxApi2.entidades;

import io.vertx.core.json.JsonObject;

public class Usuario {
	
	private int	id;
	private String nome;
	private String email;
	private String telefone;
	
	public Usuario(JsonObject data) {
		this.id = data.getInteger("id");
		this.nome = data.getString("nome");
		this.email = data.getString("email");
		this.telefone = data.getString("telefone");
	}
	
	public Usuario() {
		
	}

	public String valuesForQuery() {
		return "'"+ this.nome +"'" +","+"'"+ this.email +"'"+","+"'"+ this.telefone+"'";
	}
	
	public String getNome() {
		return nome;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
}
