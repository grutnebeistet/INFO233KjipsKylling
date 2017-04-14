package game.entity;

public class monsterStatements {

	public static final String createTable = "CREATE TABLE monsters (\n" + 
			"       \"monster\" INT NOT NULL PRIMARY KEY,\n" + 
			"		\"name\" VARCHAR(128) NOT NULL)";

	public static final String insertMonster = "INSERT INTO monsters (\"monster\", \"name\")\n" + 
			"VALUES (?,?)";

	public static final String selectAllMonsters = "SELECT * FROM monsters";


}

