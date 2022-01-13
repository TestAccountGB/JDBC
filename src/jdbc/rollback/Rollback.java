package jdbc.rollback;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

import jdbc.connection.Connector;

public class Rollback {
	
	@SuppressWarnings("unused")
	public static void commit() throws SQLException {
		
		
		String query = "INSERT INTO buyer(name, cpf) values ('Rollback 1', '123.456.789-01')";
		String query2 = "INSERT INTO buyer(name, cpf) values ('Rollback 2', '109.876.543-21')";
		String query3 = "INSERT INTO buyer(name, cpf) values ('Rollback 3', '354.148.769-95')";
		
		Connection conn = Connector.getConnection();
		try(Statement stmt = conn.createStatement()) {
			
			//Antes de explicar o savepoint e o rollback, tenho que explicar que a cada execute, update ou qualquer outra
			//Alteracao que a gente faca usando o statement, a Connection faz um auto commit, mas o que e isso? Auto
			//Commit e basicamente uma atualizacao automatica, mas vamo se dizer que temos 3 inserts pra fazer, mas so 
			//Podemos fazer um commit quando os 3 inserts for executado corretamente e sem lancar excecoes, mas por
			//Causa do auto commit a cada insert ele vai dar um commit, ou seja, no meio desses commit pode ocorrer algum
			//Erro e mesmo assim ele vai dar o insert, mas podemos desativar usando isso...
			
			conn.setAutoCommit(false);//Nao vai dar mais o commit
			
			stmt.executeUpdate(query);
			stmt.executeUpdate(query2);
			
			if(true) {
				throw new SQLException();
				//Com isso ele nao vai commitar os inserts acima porque o auto commit esta desativado
			}
			
			stmt.executeUpdate(query3);
			
			conn.commit();//E so no final ele vai commitar
			
			System.out.println("Inserido com sucesso");
		} catch (SQLException e) {
			//e.printStackTrace(); //Vou tirar para nao ficar pouluido
			
			//E se caso der excecao a gente pode voltar ao estado sem alteracao do Connection com esse metodo...
			conn.rollback();//Isso lanca uma excecao
		} finally {
			Connector.close(conn);
		}
	}
	
	@SuppressWarnings("unused")
	public static void savepoint() throws SQLException {
		
		
		String query = "INSERT INTO buyer(name, cpf) values ('Rollback 1', '123.456.789-01')";
		String query2 = "INSERT INTO buyer(name, cpf) values ('Rollback 2', '109.876.543-21')";
		String query3 = "INSERT INTO buyer(name, cpf) values ('Rollback 3', '354.148.769-95')";
		Connection conn = Connector.getConnection();
		Savepoint savepoint = null;
		
		try(Statement stmt = conn.createStatement()) {
			
			conn.setAutoCommit(false);//Nao vai dar o auto commit
			
			//Vamos se dizer que mesmo que mesmo que der erro nos outros inserts, eu quero pelo menos dar o insert no
			//Primeiro
			stmt.executeUpdate(query);
			savepoint = conn.setSavepoint("One");//E podemos salvar um savepoint aqui, e dar um apelido para o mesmo
			
			if(true) {
				throw new SQLException();
			}
			
			stmt.executeUpdate(query2);
			stmt.executeUpdate(query3);
			
			conn.commit();//E so no final ele vai commitar
			
			System.out.println("Inserido com sucesso");
		} catch (SQLException e) {
			//e.printStackTrace();
			
			conn.rollback(savepoint);//E podemos dar um rollback no savepoint aonde so tem um insert
			conn.commit();//E depois dar um commit
			
			System.out.println("Commit realizado com sucesso");
		} finally {
			Connector.close(conn);
		}
	}
	
	//Obs.: Apenas a Engine InnoDB suporta essas operacoes. E alguns banco de dados nao suportam mais de um Savepoint
	//Mas podemos reaproveitar o mesmo usando o metodo: conn.releaseSavepoint(savepoint); E depois usar o
	//setSavepoint caso queira colocar outro
	
	
}
