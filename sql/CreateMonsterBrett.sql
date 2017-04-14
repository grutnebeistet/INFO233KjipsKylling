CREATE TABLE monsterPlace(
//	monster VARCHAR(128) NOT NULL,
	brett VARCHAR(128) NOT NULL PRIMARY KEY,
	monsterID INTEGER NOT NULL,
	row INTEGER NOT NULL,
	col INTEGER NOT NULL,

	FOREIGN KEY (monsterID) REFERENCES monsterTable (id)
//FOREIGN KEY (monster) REFERENCES monsterTable(navn)
)