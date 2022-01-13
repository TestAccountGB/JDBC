package application;

import java.util.List;
import java.util.Scanner;

import jdbc.InitialSetupDatabase;
import jdbc.resultSet.BuyerSet;
import jdbc.resultSet.CallableStatementTest;
import jdbc.resultSet.CursorMovement;
import jdbc.resultSet.MetaData;
import jdbc.resultSet.PlayingWithDatabase;
import jdbc.resultSet.PreparedStatementTest;
import jdbc.rollback.Rollback;
import jdbc.rowSet.ConnectedRowSet;
import jdbc.rowSet.DisconnectedRowSet;
import model.entities.Buyer;

public class Program {
	public static void main(String[] args) {

		try (Scanner scan = new Scanner(System.in)) {
			scan.useDelimiter("\r\n");
			
			InitialSetupDatabase.initialSetup();
			System.out.println("Aperte enter para continuar...");
			scan.nextLine();
			
			System.out.println("Para fazer um insert vamos precisar das seguintes informacoes:");
			System.out.print("Nome (Sem sobrenome): ");
			String nome = scan.next();
			
			if(!nome.matches("[a-zA-Z0-9]+"))
				throw new Exception();
			
			System.out.print("Cpf (Com pontinhos e tracinhos): ");
			String cpf = scan.next();
			
			Buyer buyer = new Buyer(6, nome, cpf);
			System.out.println();
			System.out.println("Linhas afetadas: " + BuyerSet.insertBuyer(buyer));
			
			System.out.println();
			System.out.println("Agora vou atualizar o novo dado (Aperte enter)...");
			scan.nextLine();
			scan.nextLine();
			
			buyer.setName("Atualizado");
			System.out.println("Linhas afetadas: " + BuyerSet.updateBuyer(buyer));
			System.out.println("Atualizado!");
			
			System.out.println();
			System.out.println("Agora vou apagar o mesmo dado (Aperte enter)...");
			scan.nextLine();
			
			System.out.println("Linhas afetadas: " + BuyerSet.deleteBuyer(buyer));
			System.out.println("Apagado!");
			
			System.out.println();
			System.out.println("Todos os Buyers (Aperte enter)... ");
			scan.nextLine();
			
			List<Buyer> buyerList = BuyerSet.getAllFromBuyer();
			buyerList.forEach(System.out::println);
			
			System.out.println();
			System.out.println("Testes com MetaData (Aperte enter)...");
			scan.nextLine();
			
			System.out.println();
			MetaData.showResultSetMetaData();
			MetaData.showDatabaseMetaData();
			System.out.println();
			CursorMovement.cursorTest();
			
			System.out.println();
			System.out.println("Irei arrumar os nomes (Aperte enter)...");
			scan.nextLine();
			PlayingWithDatabase.resultSetUpdate();
			
			System.out.println();
			System.out.println("Irei inserir um nome (Aperte enter)...");
			scan.nextLine();
			PlayingWithDatabase.resultSetInsert();
			
			System.out.println();
			System.out.println("Irei deletar um linha (Aperte enter)...");
			scan.nextLine();
			PlayingWithDatabase.resultSetDelete(scan);
			
			System.out.println();
			System.out.print("Digite um nome e eu vou pesquisar todos que tem o nome parecido: ");
			buyer.setName(scan.next());
			
			System.out.println();
			System.out.println("Select normal:");
			System.out.println(PreparedStatementTest.preparedeStatementTest(buyer));
			
			System.out.println();
			System.out.println("Stored Procedure:");
			System.out.println(CallableStatementTest.callableStatementTest(buyer));
			
			System.out.println();
			System.out.println("Vou dar um update (Aperte enter)...");
			scan.nextLine();
			scan.nextLine();
			ConnectedRowSet.updateRowSet(buyer);
			
			System.out.println();
			System.out.println("Teste listener (Aperte enter)...");
			scan.nextLine();
			ConnectedRowSet.listenerRowSet();
			
			System.out.println();
			System.out.println("Teste Disconnected RowSet (Aperte enter)...");
			scan.nextLine();
			DisconnectedRowSet.updateRowSet(buyer, scan);
			
			System.out.println();
			System.out.println("Teste rollback (Aperte enter)...");
			scan.nextLine();
			Rollback.commit();
			Rollback.savepoint();
			
		} catch (Exception  e) {
			e.printStackTrace();
		}
	}
}
