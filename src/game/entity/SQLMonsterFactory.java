package game.entity;

import game.entity.MonsterHelper;
import game.entity.monsterStatements;
import game.entity.types.Monster;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class SQLMonsterFactory {
	Monster oppNedMonster;
	
	
	/**
	 * lager monster table
	 */
	public void createMonsterTable(){
		try (Connection conn = getConnection()){
			DatabaseMetaData dbm = conn.getMetaData();
			ResultSet res = dbm.getTables(null, null, "monsters", null);
			if(res!=null){
				try(PreparedStatement statement = conn.prepareStatement(monsterStatements.createTable)){
					statement.execute();
				}
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 *setter inn monster
	 *@param Monster  
	 */
	public void insertMonster(MonsterHelper monster){
		try (Connection conn = getConnection()){
			try(PreparedStatement statement = conn.prepareStatement(monsterStatements.insertMonster)){
				statement.setInt(1, monster.getNumber());
				statement.setString(2, monster.getName());
				statement.execute();
			}
		}
		catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 
	 * 
	 *@return List<MonsterHelper> list over all the monsters in a list of monsterhelpers
	 */
	public List<MonsterHelper> getAll(){

		List<MonsterHelper> monsters= new LinkedList<>();
		try (Connection conn = getConnection()){
			try(PreparedStatement statement = conn.prepareStatement(monsterStatements.selectAllMonsters)){
				try(ResultSet result = statement.executeQuery()){

					while(result.next()){
						int number = result.getInt("monster");
						String name = result.getString("name");
							monsters.add(new MonsterHelper(number,name));
					}

				}
			}
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return monsters;

	}


	public Connection getConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{

		String driver = "org.apache.derby.jdbc.EmbeddedDriver";
		Class.forName(driver).newInstance();
		String protocol = "jdbc:derby:";
		String databaseNavn = "database";
		String jdbcUrl = protocol + databaseNavn + ";create=true";

		Connection conn = DriverManager.getConnection(jdbcUrl);
		return conn;
	}
}
	

