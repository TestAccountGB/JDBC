package jdbc.resultSet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jdbc.connection.Connector;
import model.entities.Buyer;

public class BuyerSet {
	
	public static Integer insertBuyer(Buyer buyer) {
		if(buyer == null || buyer.getName() == null || buyer.getCpf() == null) {
			throw new RuntimeException("Comprador invalido");
		}
		
		String query = "INSERT INTO buyer(name, cpf) values ('"  + buyer.getName() +  "', '" + buyer.getCpf() + "');";
		
		try(Connection conn = Connector.getConnection();
				Statement stmt = conn.createStatement()) {
			//Vamos colocar em um try with resources para nao precisar fechar ele
			
			//A classe statement tem tres metodos principais:
			//1 - executeUpdate, Ele faz as operacoes de inserir, atualizar e deletar um dado do banco de dados e retona a
			//Quantidade de linhas afetadas.
			//2 - executeQuery, Ele faz as operacoes de SELECT do banco de dados e retorna um ResultSet para manipular
			//Esses dados
			//3 - execute, Ele e uma versao generica dos dois metodos acima, ele retorna TRUE ou FALSE, se for usar para uma
			//Operacao de SELECT ele vai retornar TRUE e entao vai guardar um ResultSet, ai tem que usar o metodo
			//getResultSet para pegar o mesmo, e se for uma operacao do executeUpdate ele vai retornar FALSE e entao vai
			//Ter que usar o metodo getUpdateCount para retornar as linhas afetadas.
			
			int linhasAfetadas = stmt.executeUpdate(query);//Como essa query vai inserir um valor entao devemos usar
			//O metodo executeUpdate
			System.out.println("Comprador inserido com sucesso");
			return linhasAfetadas;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Integer updateBuyer(Buyer buyer) {
		if(buyer == null || buyer.getName() == null || buyer.getCpf() == null || buyer.getId() == null) {
			throw new RuntimeException("Comprador invalido");
		}
		
		String query = "UPDATE BUYER SET name = '"+ buyer.getName() +"', cpf = '"+ buyer.getCpf() +"' WHERE (id = '"+ buyer.getId() +"');";
		
		try(Connection conn = Connector.getConnection();
				Statement stmt = conn.createStatement()) {
			
			int linhasAfetadas = stmt.executeUpdate(query);
			System.out.println("Comprador atualizado com sucesso");
			return linhasAfetadas;
			//A logica de atualizar e a mesma do que a de inserir
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Integer deleteBuyer(Buyer buyer) {
		if(buyer == null || buyer.getId() == null) {
			throw new RuntimeException("Comprador invalido");
		}
		
		String query = "DELETE FROM BUYER WHERE (id = " +  buyer.getId() + ");";
		
		try(Connection conn = Connector.getConnection();
				Statement stmt = conn.createStatement()) {
			
			int linhasAfetadas = stmt.executeUpdate(query);
			System.out.println("Comprador deletado com sucesso");
			return linhasAfetadas;
			//A logica de deletar e a mesma do que a de atualizar e inserir
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<Buyer> getAllFromBuyer() {
		
		String query = "SELECT id, name, cpf FROM BUYER";
		List<Buyer> buyerList = new ArrayList<>();//Vou colocar todos os dados retornados em um Array
		
		try(Connection conn = Connector.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet resultSet = stmt.executeQuery(query)) {//Estou colocando tudo em try-with-resources para nao precisar fechar
			//Como e uma acao que vai retornar dados temos que usar o "executeQuery" e ele retorna um ResultSet com
			//Os dados
			
			while(resultSet.next()) {//Ou seja, enquanto tiver dados continue...
				//Para pegar os dados vamos usar os comandos "get(tipo do dado())", nao precisa ser na ordem do SELECT, pois
				//Podemos colocar como parametro o nome da coluna ou o seu indice
				int id = resultSet.getInt(1);//Comeca no indice 1, nao e igual a array
				String name = resultSet.getString("name");//Nome da coluna
				String cpf = resultSet.getString(3);
				
				buyerList.add(new Buyer(id, name, cpf));
			}
			
			return buyerList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
