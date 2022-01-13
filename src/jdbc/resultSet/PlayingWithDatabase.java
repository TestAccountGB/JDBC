package jdbc.resultSet;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import jdbc.connection.Connector;
import model.entities.Buyer;

public class PlayingWithDatabase {
	
	public static void resultSetUpdate() {
		
		String query = "SELECT id, name, cpf FROM BUYER;";
		
		try(Connection conn = Connector.getConnection();
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				ResultSet rs = stmt.executeQuery(query)){
			
			//Isso pode depender da opiniao de cada pessoa, mas podemos atualizar e inserir linhas fora do banco de
			//Dados, podemos fazer isso dentro do proprio codigo, isso pode as vezes poupar tempo. Mas algumas preferem
			//Fazer a logica no banco de dados e outras no codigo
			
			//Atualizando linhas...
			
			//Exemplo hipotetico: Quero atualizar todos os nomes do banco de dados para o padrao - Primeira letra
			//Maiuscula e o resto minusculo
			
			//Vamos usar o while para passar por todos os dados...
			while(rs.next()) {
				
				String name = rs.getString("name");//Pegando o nome
				char firstLetter = name.toUpperCase().charAt(0);//Apenas a primeira letra em maiusculo
				
				rs.updateString("name", firstLetter + name.substring(1).toLowerCase());
				//"update(tipo da coluna que vai ser atualizada)"
				//Aceita dois parametros, o primeiro sendo o nome da coluna e o segundo o valor atualizado. E depois ele
				//Atualiza o RESULTSET, ou seja, o banco de dados nao ta sendo mudado. Para atualizar ele usamos o metodo
				//updateRow()
				
				//Mas vamos dizer que se a pessoa ja estiver com o nome correto vamos querer desfazer o update acima,
				//Como fazemos isso?
				
				if(name.matches("[A-Z][a-z]+")) {//Esse regex so funciona com apenas nomes sem sobrenomes
					//rs.updateString("name", name);
					//Podemos fazer o codigo acima para desfazer o outro update? Talvez, mas o java nao recomenda fazer mais
					//Que um update na mesma coluna, por isso temos o metodo abaixo para desfazer os updates anteriores
					rs.cancelRowUpdates();
					System.out.println("Pessoa na posicao " +  rs.getRow() + " ja estava correto");
				}
				
				
				rs.updateRow();//E esse metodo faz os updates no banco de dados
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void resultSetInsert() {
		
		String query = "SELECT id, name, cpf FROM BUYER;";
		
		//Nao so podemos atualizar dados do banco de dados pelo codigo, mas como tambem podemos inserir...
		
		try(Connection conn = Connector.getConnection();
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				ResultSet rs = stmt.executeQuery(query)){
			
			//Vamos tentar pegar as primeira letra de todas as pessoas e depois juntar tudo e criar uma nova linha com esse
			//Nome...
			
			String nome = "";
			//Vamos usar o while para passar por todos os dados...
			while(rs.next()) {
				nome += rs.getString("name").charAt(0);
			}
			
			//Arrumando o nome...
			char firstLetter = nome.toUpperCase().charAt(0);
			nome = firstLetter + nome.substring(1).toLowerCase();
			
			rs.moveToInsertRow();//Imagina que estamos em uma linha imaginaria sem dados.
			//Obs.: Esse metodo move o cursor, mas caso voce use o metodo getRow para ver a posicao dele, ele vai estar
			//Na posicao que foi deixado antes, mas essa nao e a posicao real dele, esse e o jeito do java lembrar a posicao
			//Antiga do cursor antes desse metodo, por isso caso a gente queira voltar o cursor para a posicao antiga, temos
			//Que usar esse metodo: rs.moveToCurrentRow();
			
			//E agora vamos atualizar esse linha com dados
			rs.updateString("name", nome);
			rs.updateString("cpf", "999.999.999-99");
			//Nao precisamos atualizar o id, porque ele esta como auto incrementado no banco de dados
			
			//E agora vamos inserir essa linha no banco de dados
			rs.insertRow();
			System.out.println("Nome da pessoa inserida: " + rs.getString("name"));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void resultSetDelete(Scanner scan) {
		String query = "SELECT id, name, cpf FROM BUYER;";
		
		//Tambem podemos apagar linhas do banco de dados dentro do codigo
		
		try(Connection conn = Connector.getConnection();
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				ResultSet rs = stmt.executeQuery(query)){
			
			System.out.print("Escolha uma linha para ser apagada: ");
			int row = scan.nextInt();
			
			rs.absolute(row);//Vamos se mover para a linha
			
			System.out.println("Pessoa deletada: " + new Buyer(rs.getInt(1), rs.getString(2),
					rs.getString(3)));
			
			rs.deleteRow();//E aqui deletamos
			
			//Tambem podemos testar para ver se foi realmente excluido com esse metodo...
			
//			if(rs.rowDeleted()) {
//				System.out.println("Deletado");
//			}
			
			//Mas tome cuidado a usar este metodo, pois nem todos os banco de dados suportam ele, por isso temos que
			//Testar antes...
			
			//Para poder testar vamos ter que usar um MetaData, sendo mais exato, DatabaseMetaData
			
			DatabaseMetaData dbMetaData = conn.getMetaData();
			
			System.out.println();
			System.out.println("Podemos detectar os deletes? " +
			dbMetaData.deletesAreDetected(ResultSet.TYPE_SCROLL_INSENSITIVE));
			//Como parametro temos que passar o tipo do nosso ResultSet
			
			//Mas nao temos apenas como detectar deletes, podemos detectar deletes, insertes e updates
			System.out.println("Podemos detectar os insertes? " +
			dbMetaData.insertsAreDetected(ResultSet.TYPE_SCROLL_INSENSITIVE));
			
			System.out.println("Podemos detectar os updates? " +
			dbMetaData.updatesAreDetected(ResultSet.TYPE_SCROLL_INSENSITIVE));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
