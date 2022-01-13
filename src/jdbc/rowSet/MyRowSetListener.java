package jdbc.rowSet;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.RowSetEvent;
import javax.sql.RowSetListener;

public class MyRowSetListener implements RowSetListener {

	@Override
	public void rowSetChanged(RowSetEvent event) {
		//Metodo ativado quando acontece um comando que muda ou executa algo, como o metodo execute e updateRow.
		System.out.println("Execute/Update executado");
	}

	@Override
	public void rowChanged(RowSetEvent event) {
		//Quando alguma linha e alterada, seja excluida, atualizada ou inserida
		System.out.println("Linha alterada");
		
	}

	@Override
	public void cursorMoved(RowSetEvent event) {
		//Quando o cursor se moveu
		System.out.print("O cursor se moveu para a posicao: ");
		if(event.getSource() instanceof ResultSet) {//Estamos vendo se a fonte e uma instancia de ResultSet pois quase
			//Todos os "set", como o RowSet, extendem de ResultSet
			try {
				System.out.println(((ResultSet) event.getSource()).getRow());//E depois vamos mostrar a linha
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
