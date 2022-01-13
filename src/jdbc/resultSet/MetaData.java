package jdbc.resultSet;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import jdbc.connection.Connector;

public class MetaData {
	
	public static void showResultSetMetaData() {
		String query = "SELECT * FROM BUYER";
		
		try(Connection conn = Connector.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet resultSet = stmt.executeQuery(query)) {
			
			ResultSetMetaData rsMetaData = resultSet.getMetaData();
			//E assim que a gente pega os meta dados do banco de dados, mas para que eles servem? Ele ajudam muito
			//Quando a gente nao tem acesso ao banco de dados em si, quando nao podemos ver as colunas e seus
			//Atributos, como assim? Observe...
			
			int columnCount = rsMetaData.getColumnCount();
			
			System.out.println("Quantidade de colunas: " + columnCount);
			System.out.println("-----------------------------------------------------");
			
			for(int i = 1; i <= columnCount; i++) {//Comecamos do 1, pois como falei, a contagem do banco de dados comeca do 1
				System.out.println("Nome da tabela: " + rsMetaData.getTableName(columnCount));
				System.out.println("Nome da coluna: " + rsMetaData.getColumnName(i));
				System.out.println("Tipo da coluna: " + rsMetaData.getColumnType(i));
				//Esse tipo retorna um numero que corresponde ao tipo da coluna do sql, voce pode ver a lista aqui:
				//https://www.tutorialspoint.com/java-resultsetmetadata-getcolumntype-method-with-example
				System.out.println("Tamanho da coluna: " + rsMetaData.getColumnDisplaySize(i));
				System.out.println("-----------------------------------------------------");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void showDatabaseMetaData() {
		
		try(Connection conn = Connector.getConnection()) {
			
			DatabaseMetaData dbMetaData = conn.getMetaData();
			//Nessa interface temos alguns metodos interessantes como:
			
			System.out.println(dbMetaData.getUserName());
			System.out.println(dbMetaData.getURL());
			System.out.println(dbMetaData.getDriverName());
			System.out.println(dbMetaData.getDriverVersion());
			
			//Mas o seu maior destaque e que ela consegue testar se o banco de dados suporta alguns tipos de alteracao
			//De arquivo, mas antes de mostrar o metodo vou explicar quais sao os tipos que ele pode retornar
			
			//1 - ResultSet.TYPE_FORWARD_ONLY, Esse tipo apenas permite a gente mandar o cursor pra frente (baixo), mas 
			//Como assim? O cursor e o que chamamos o "apontador" de linha, vamos se dizer que a gente ta na linha 1 e
			//Vamos para a linha 2, se o banco de dados for do tipo TYPE_FORWARD_ONLY ele nao vai deixar voltar para a
			//Linha 1, ou seja, no codigo so vamos poder usar o metodo resultSet.nex() e nao vamos poder usar o metodo
			//resultSet.previous()
			
			//2 - ResultSet.TYPE_SCROLL_INSENSITIVE, O "SCROLL" significa que a gente pode mover para cima e para baixo,
			//E o "INSENSITIVE" significa que ele nao consegue perceber modificacoes externas, como assim? Vamos se dizer
			//Que alguem bem na hora que meu ResultSet ta aberto e mexendo na dado 3, alguma pessoa atualiza essa
			//Mesmo dado, o que vai acontecer? No INSENSITIVE nada, pois ele nao percebe, ele guarda o dado na memoria
			//Na hora em que eu abro o ResultSet
			
			//3 - ResultSet.TYPE_SCROLL_SENSITIVE, Como foi dito no "INSENSITIVE" ele nao consegue perceber mudancas
			//Externas, mas o SENSITIVE consegue, ou seja, se uma pessoa mudar um dado que eu estou mexendo no
			//ResultSet, o meu ResultSet tambem vai mudar, pois ele esta ligado ao banco e se atualiza constantemente
			
			//Agora vou falar sobre os tipos de Concur.
			
			//1 - ResultSet.CONCUR_READ_ONLY, O que sao Concur? Sla porra, mo nome estranho kkkk, mas ele falam se
			//O banco de dados aceita updates, ou sao apenas para leitura
			
			//2 - ResultSet.CONCUR_UPDATABLE
			
			//Mas como usar isso em pratica?
			//Podemos fazer um teste assim:
			
			System.out.println();
			System.out.print("O banco de dados e do tipo: ");
			if(dbMetaData.supportsResultSetType(ResultSet.TYPE_FORWARD_ONLY)) {
				System.out.print("TYPE_FORWARD_ONLY");
				if(dbMetaData.supportsResultSetConcurrency(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
					System.out.println(" e tambem aceita atualizacoes");
				}
			} else if (dbMetaData.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE)) {
				System.out.print("TYPE_SCROLL_INSENSITIVE");
				if(dbMetaData.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
					System.out.println(" e tambem aceita atualizacoes");
				}
			} else {
				System.out.print("TYPE_SCROLL_SENSITIVE");
				if(dbMetaData.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
					System.out.println(" e tambem aceita atualizacoes");
				}
			}
			//O tipo padrao do MySQL e TYPE_SCROLL_INSENSITIVE e CONCUR_UPDATABLE
			
			//Podemos fazer um ResultSet usando esses parametros:
			//Mas botamos ele em um Statement...
			Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			//Obs.: Instanciar um Statement com esses parametros e recomendavel
			//E depois usamos esse statement para criar o resultSet
			//ResultSet resultSet = stmt.executeQuery("Query aleatoria");
			//Obs.: O driver do MySQL tem uma pecularidade que ele nao se importa com quando Type voce botar ele
			//Sempre vai ser TYPE_SCROLL_INSENSITIVE
			
			//Esses sao os usos mais frequentes para esses metaDados, mas claro que tem muitos mais usos que esses, mas
			//Nao muito usados no cotidiano, se tiver interassado pesquise sobre tanto os metadados do banco de dados e
			//Do ResultSet :)
			
			stmt.close();
			//resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
