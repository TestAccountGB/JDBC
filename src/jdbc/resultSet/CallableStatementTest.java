package jdbc.resultSet;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jdbc.connection.Connector;
import model.entities.Buyer;

public class CallableStatementTest {
	//O que e CallableStatement? E basicamente igual a PreparedStatement, so que ele serve para chamar
	//StoredProcedures (Basicamente uma funcao/metodo do SQL).

	public static List<Buyer> callableStatementTest(Buyer buyerArgs) {
		if (buyerArgs == null || buyerArgs.getName() == null) {
			throw new RuntimeException("Comprador invalido");
		}

		String query = "CALL SP_getCompradorPorNome(?);";

		try (Connection conn = Connector.getConnection();
				CallableStatement cs = conn.prepareCall(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY)) {
			
			cs.setString(1, "%" + buyerArgs.getName() + "%");
			
			ResultSet rs = cs.executeQuery();
			
			List<Buyer> buyerList = new ArrayList<>();
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String cpf = rs.getString("cpf");

				buyerList.add(new Buyer(id, name, cpf));
			}
			rs.close();
			return buyerList;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
