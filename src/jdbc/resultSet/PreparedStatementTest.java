package jdbc.resultSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jdbc.connection.Connector;
import model.entities.Buyer;

public class PreparedStatementTest {
	//O que e um PreparedStatement? Como o nome ja diz, ele e um Statement so que ele "prepara" a nossa query, Mas
	//O que isso significa? Significa que ele pre-compila a nossa query e isso faz a gente ganhar muito mais performance,
	//Pois quando a gente mandar para o banco de dados ele nao vai precisar checkar tanto para ver se esta correto e
	//Tambem evita a injecao de sql, que e uma falha de seguranca gravissima, vamos pensar assim: Tenho um metodo que
	//Aceita um nome: SELECT nome FROM PERSON WHERE NOME = *nome*; Esse nome eu coloco na query, isso tem uma
	//Falha pois a pessoa pode passar o nome assim: joao OR NOME = "Arthur"; e assim ele consegue pegar dados de outra
	//Pessoa. Isso ja aconteceu com empresas grandes, como o Yahoo!
	//Injecao de sql explicacao:
	//https://www.tecmundo.com.br/tecmundo-explica/113195-sql-injection-saiba-tudo-ataque-simples-devastador.htm
	
	//Mas como usamos o PreparedStatement? Obzerve...
	public static List<Buyer> preparedeStatementTest(Buyer buyerArgs) {
		if(buyerArgs == null || buyerArgs.getName() == null) {
			throw new RuntimeException("Comprador invalido");
		}
		
		String query = "SELECT id, name, cpf FROM BUYER WHERE NAME LIKE ?;";//O que significa essa interrogacao?
		//Ela e o que vamos substituir com o PreparedStatement (PS), mas aqui tambem nao pode ter uma injecao de sql?
		//Nao, pois o PS nao deixa a gente passar apostrofo e nem aspas, e desse jeito o banco de dados nao consegue
		//Reconhecer.
		
		try(Connection conn = Connector.getConnection();
				PreparedStatement ps = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY)){//Somente leitura, porque so estamos usando um SELECT :)
			//Assim criamos o PS, tambem podemos passar os tipos de ResultSet, mas sempre lembre de passar os 2
			
			//E como substituimos os pontos de interregacoes? Assim...
			ps.setString(1, "%" + buyerArgs.getName() + "%");//A gente usa o set(tipo do dado que vai na interrogacao)
			//O primeiro parametro e a posicao da interrogacao em relacao com outras interrogacoes, comecando do 1, isso
			//Mesmo, voce pode colocar quantas interrogacoes voce quiser na query
			//O segundo parametro e o valor que vai substituir a interrogacao
			
			//Depois executamos a query...
			ResultSet rs = ps.executeQuery();
			//Aqui e o mesma esquema que um Statement normal. Tem o executeQuery, Update e o normal, mas a unica
			//Diferenca e que nao pode passar a query como parametro pois ela e passada no prepareStatement(),
			//Pois nesse metodo ele ja pre-compila a query
			
			List<Buyer> buyerList = new ArrayList<>();
			while(rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String cpf = rs.getString("cpf");
				
				buyerList.add(new Buyer(id, name, cpf));
			}
			rs.close();
			return buyerList;
			//Como pode ver, no geral o PS e praticamente o Statement, so que mais rapido e seguro, entao sempre que puder
			//Usar o PS, USE.
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
