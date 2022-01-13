package jdbc.rowSet;

import java.sql.SQLException;
import java.util.Scanner;

import javax.sql.rowset.CachedRowSet;

import jdbc.connection.Connector;
import model.entities.Buyer;

public class DisconnectedRowSet {
	
	public static void updateRowSet(Buyer buyerArgs, Scanner scan) {
		if(buyerArgs == null || buyerArgs.getName() == null) {
			throw new RuntimeException("Comprador invalido");
		}
		
		String query = "SELECT id, name, cpf FROM BUYER WHERE ID = ?;";
		
		try {
			CachedRowSet crs = Connector.getRowSetCached();
			//Como eu falei, esse RowSet nao precisa ser fechado, pois sua conexao com o banco de dados e fechada
			//Automaticamente, imagine assim: Na instanciacao foi aberta uma conexao com o banco de dados e todos os
			//Dados do banco agora estao guardados na memoria e podemos mudar o que esta na memoria e depois mandar
			//Para o banco de dados, mas isso tudo e feito automaticamente, entao a unica diferenca que vai fazer para voce
			//E que vai ter que usar o metodo "acceptChanges" quando quiser mandar para o banco de dados.
			crs.setCommand(query);
			crs.setInt(1, buyerArgs.getId());
			crs.execute();
			
			if(crs.next()) {
				crs.updateString("name", "Update do CachedRowSet :)");
				crs.updateRow();
				//Mas aqui pode ter um problema, porque quando a gente usa o metodo "acceptChanges" ele abre novamente
				//A conexao com o banco de dados e verifica se ele esta igual aos dados da memoria, se nao estiver ele vai
				//Lancar uma excecao
				System.out.println("Faca uma atualizacao no banco de dados e depois aperte a tecla enter");
				scan.nextLine();
				scan.nextLine();
				crs.acceptChanges();
				
			}
			
			System.out.println("Atualizado com sucesso");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
