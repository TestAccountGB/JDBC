package jdbc.resultSet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jdbc.connection.Connector;
import model.entities.Buyer;

public class CursorMovement {
	
	public static void cursorTest() {
		String query = "SELECT * FROM BUYER";
		
		try(Connection conn = Connector.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet resultSet = stmt.executeQuery(query)){
			
			//Inicialmente o nosso cursor comeca na posicao 0, por isso temos que dar um resultSet.next() para ir para a
			//Linha 1, pois se a gente tentar pegar algo na linha 0 vai lancar uma excecao. Se comecar na linha 1 e der um next
			//Vamos para a linha 2 e vamos pular um dado, por isso comecamos da linha 0
			System.out.println("Posicao inicial: " + resultSet.getRow());
			
			//Temos muitos metodos para a movimentacao do cursos, como:
			
			//next() - Anda uma linha e retorna um boolean caso exista um dado na linha
			resultSet.next();
			System.out.println("Posicao depois do next(): " + resultSet.getRow());
			System.out.println("Pessoa na posicao 1: " + new Buyer(resultSet.getInt(1), resultSet.getString(2),
					resultSet.getString(3)));
			
			//last() - Vai para a ultima posicao e retorna um boolean...
			resultSet.last();
			System.out.println("Pessoa na ultima posicao: " + new Buyer(resultSet.getInt(1), resultSet.getString(2),
					resultSet.getString(3)));
			
			//previous() - Volta uma linha e retorna um boolean...
			resultSet.previous();
			System.out.println("Pessoa depois do previous(): " + new Buyer(resultSet.getInt(1), resultSet.getString(2),
					resultSet.getString(3)));
			
			//afterLast() - Vai para a posicao depois da final. Exemplo: Final = 4. afterLast = 5
			//Util quando queremos comecar do final para o comeca, porque como eu expliquei nas primeiras linhas,
			//Temos que comecar acima/abaixo da linha inicial/final
			resultSet.afterLast();
			
			//absolute() - Vai para a posicao passada no parametro e retorna um boolean...
			resultSet.absolute(4);
			System.out.println("Pessoa na posicao 4: " + new Buyer(resultSet.getInt(1), resultSet.getString(2),
					resultSet.getString(3)));
			
			//relative() - Ele diminui/acrescenta a posicao dependendo do que passar no parametro e retorna um boolean...
			resultSet.relative(-1);
			System.out.println("Pessoa depois do relative(): " + new Buyer(resultSet.getInt(1), resultSet.getString(2),
					resultSet.getString(3)));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
