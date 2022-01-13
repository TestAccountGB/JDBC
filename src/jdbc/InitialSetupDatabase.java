package jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jdbc.connection.Connector;
import model.entities.Buyer;

public class InitialSetupDatabase {

	private static void dropTable() {
		String query = "DROP TABLE IF EXISTS BUYER;";

		try (Connection conn = Connector.getConnection();
				PreparedStatement ps = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_UPDATABLE)) {

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void createTable() {
		String query = "create table buyer("
	+ "id INT AUTO_INCREMENT PRIMARY KEY,"
				+ "name VARCHAR(50) NOT NULL,"
				+ "cpf VARCHAR(14) NOT NULL"
				+ ")ENGINE = innodb;";

		try (Connection conn = Connector.getConnection();
				PreparedStatement ps = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_UPDATABLE)) {

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void buyerInsert() {

		Buyer[] buyers = new Buyer[5];
		buyers[0] = new Buyer("Felipe", "111.111.111-11");
		buyers[1] = new Buyer("ulisses", "222.222.222-22");
		buyers[2] = new Buyer("cesar", "333.333.333-33");
		buyers[3] = new Buyer("kauan", "444.444.444-44");
		buyers[4] = new Buyer("Uriel", "555.555.555-55");
		
		String query = "INSERT INTO buyer(name, cpf) values (?, ?);";
		
		try (Connection conn = Connector.getConnection();
				PreparedStatement ps = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_UPDATABLE)) {
			
			for(int i = 0; i < buyers.length; i++) {
				ps.setString(1, buyers[i].getName());
				ps.setString(2, buyers[i].getCpf());
				ps.executeUpdate();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void initialSetup() {
		dropTable();
		createTable();
		buyerInsert();
		
		System.out.println("The database is ready :)");
	}
	
	
	
}
