package model.entities;

import java.util.Objects;

public class Buyer {
	//Vamos criar uma entidade exatamente igual a entidade do banco de dados

	private Integer id;
	private String name;
	private String cpf;

	public Buyer(Integer id, String name, String cpf) {
		this.id = id;
		this.name = name.trim();
		this.cpf = cpf.trim();
	}

	public Buyer(String name, String cpf) {
		this.name = name.trim();
		this.cpf = cpf.trim();
	}
	
	//Criamos um construtor sem id pois quando a gente for inserir um valor nao vamos usar o id, porque ele esta como
	//Auto incrementado no banco de dados

	@Override
	public String toString() {
		return "Id: " + id + ", Name: " + name + ", Cpf: " + cpf;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (this.getClass() != obj.getClass()) return false;
		Buyer other = (Buyer) obj;
		return id == other.id;
	}

}
