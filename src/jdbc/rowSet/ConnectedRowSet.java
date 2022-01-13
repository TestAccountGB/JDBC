package jdbc.rowSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.JdbcRowSet;

import jdbc.connection.Connector;
import model.entities.Buyer;

public class ConnectedRowSet {
	
	public static List<Buyer> selectRowSet(Buyer buyerArgs) {
		if(buyerArgs == null || buyerArgs.getName() == null) {
			throw new RuntimeException("Comprador invalido");
		}
		
		String query = "SELECT id, name, cpf FROM BUYER WHERE NAME LIKE ?;";
		
		try(JdbcRowSet jrs = Connector.getRowSet()){
			jrs.setCommand(query);//Comando para colocar a query dentro do jrs
			
			jrs.setString(1, "%" + buyerArgs.getName() + "%");//Mesma coisa do que o PreparedStatement
			
			jrs.execute();//So temos um comando geral que e o "execute()"
			
			List<Buyer> buyerList = new ArrayList<>();
			
			while(jrs.next()) {//Mesma coisa que o ResultSet...
				int id = jrs.getInt("id");
				String name = jrs.getString("name");
				String cpf = jrs.getString("cpf");
				
				buyerList.add(new Buyer(id, name, cpf));
			}
			
			return buyerList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	//Mas temos um problema no JdbcRowSet que nao podemos usar no comando SQL, comandos de DELETE, INSERT e
	//UPDATE. Mas temos um jeito de contornar isso, fazendo assim...
	public static void updateRowSet(Buyer buyerArgs) {
		if(buyerArgs == null || buyerArgs.getName() == null) {
			throw new RuntimeException("Comprador invalido");
		}
		
		String query = "SELECT id, name, cpf FROM BUYER WHERE ID = ?;";
		//Vamos fazer um SELECT apenas de uma pessoa pelo id
		
		try(JdbcRowSet jrs = Connector.getRowSet()){
			jrs.setCommand(query);
			jrs.setInt(1, buyerArgs.getId());
			jrs.execute();
			//Vamos fazer o upadate assim...
			if(jrs.next()) {//Buscando pelo id so vai voltar um resultado, ou seja, vamos dar apenas um next
				jrs.updateString("name", "Update do Row Set :)");
				jrs.updateRow();
				//Metodos que foram mostrados na classe "PlayingWithDatabase"
			}
			
			System.out.println("Atualizado com sucesso");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void listenerRowSet() {
		
		String query = "SELECT id, name, cpf FROM BUYER;";
		
		try(JdbcRowSet jrs = Connector.getRowSet()){
			jrs.setCommand(query);
			jrs.addRowSetListener(new MyRowSetListener());//Assim que colocamos um Listener
			jrs.execute();//Ativando o metodo "rowSetChanged" do Listener
			jrs.absolute(4);//Ativando o metodo "cursorMoved"
			jrs.deleteRow();//Ativando o metodo "rowChanged"
			
			//Como pode ver essa classe pode ser bem poderosa
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}
