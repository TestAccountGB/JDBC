package jdbc.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;

public class Connector {
	
	public static Connection getConnection() {
		//Para fazer a conexao com um banco de dados, vamos precisar de tres coisas:
		//1 - A url do banco: Que e composto primeiramento por jbdc:(Servico do banco de dados) que pode esta escrito mysql,
		//oracle e etc. Depois disso vem uma url que depende de cada banco de dados (Se estiver em duvida pesquise algo
		//assim: Java mysql connection code).
		//2 - Usuario: Esse user pode depender de cada caso, pois a empresa pode te dar um usuario especifico que nao 
		//Tenha todas as permissoes, mas o user de admin e o "root"
		//3 -  Senha: E a senha do banco de dados
		
		String url = "jdbc:mysql://localhost:3306/agencia?useSSL=false";//Esse linha "?useSSL=false" e um parametro que
		//Posso passar na url e ele serve para remover o aviso de server nao verificado. Voce pode pesquisar por diversos
		//Parametros para botar na url, tem alguns que sao muito uteis
		//Todos os parametros que voce pode passar:
		//https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-configuration-properties.html
		
		//Essa e a url do mysql, "jdbc:mysql://((Ip do server) localhost:3306)/((Nome do banco) agencia)"
		String user = "root";
		//Usuario
		String password = "";
		//E a senha
		
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			//Essa linha era obrigatoria ate a versao 3 do jdbc, pois ele nao conseguia reconhecer qual era o servico do banco
			//De dados que estamos usando na url, tinha que especificar o caminho e carregar a classe manualmente, mas 
			//Agora na versao 4 em diante nao precisamos mais usar esse codigo, ele consegue perceber qual estamos usando
			//Atraves da url
			
			Connection connection = /*return*/DriverManager.getConnection(url, user, password);
			//Vamos criar uma conexao que o driver manager vai nos retornar, mas como ele funciona? Primeiramente ele
			//Vai checar a url e vai ver de que servico e, o nosso e mysql, entao ele vai procurar o arquivo de conexao do mysql
			//E depois vai conectar ao banco de dados usando o resto da url e o user e password.
			
			//Mas esse arquivo a gente tem que adicionar ele como biblioteca (isso depende de cada IDE, entao pode dar uma
			//Pesquisada meu amigao :>)
			return connection;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static JdbcRowSet getRowSet() {
		//Como um RowSet e todos juntos (Connection, Statement e ResultSet) precisamos criar uma conexao pra ele
		
		String url = "jdbc:mysql://localhost:3306/agencia?useSSL=false";
		String user = "root";
		String password = "";
		
		try {
			JdbcRowSet jrs = RowSetProvider.newFactory().createJdbcRowSet();
			//Padrao de projeto Factory
			jrs.setUrl(url);
			jrs.setUsername(user);
			jrs.setPassword(password);
			
			return jrs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static CachedRowSet getRowSetCached() {
		String url = "jdbc:mysql://localhost:3306/agencia?useSSL=false&relaxAutoCommit=true";
		String user = "root";
		String password = "";
		
		try {
			CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
			//Padrao de projeto Factory
			crs.setUrl(url);
			crs.setUsername(user);
			crs.setPassword(password);
			
			return crs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void close(Connection connection) {
		try {
			if(connection != null)
				connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(ResultSet resultSet) {
		try {
			if(resultSet != null)
				resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(Statement statement) {
		try {
			if(statement != null)
				statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(RowSet jrs) {
		try {
			if(jrs != null)
				jrs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeAll(Connection connection, Statement statement, ResultSet resultSet) {
		close(connection);
		close(resultSet);
		close(statement);
	}
	
}
