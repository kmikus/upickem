<?php
require "../includes/conn/db_conn.php";
require "../includes/functions.php";

$conn = dbconn();
//TODO setup functionality to automatically update db at certain times for appropriate week
$games = getGameJson(2017, 4);

//TESTING
//	$day = $games[0]['date']['day'];
//	$month =  $games[0]['date']['month'];
//	$year =  $games[0]['date']['year'];
//	echo prepDate($year, $month, $day);
//	jsonPrint($games[0]);

$sql = $sqlstart = "INSERT INTO game (gameid, season, week, hometeam, homescore, awayteam, awayscore, gamedate) VALUES (";
foreach($games as $game) {
	$year = $game['date']['year'];
	$month = $game['date']['month'];
	$day = $game['date']['day'];
	$gamedate = prepDate($year, $month, $day);
	
	$sql .= $game['gameid'] . ",";
	$sql .= $game['season'] . ",";
	$sql .= "'" . $game['week'] . "',";
	$sql .= "'" . $game['hometeam'] . "',";
	$sql .= $game['homescore'] . ",";
	$sql .= "'" . $game['awayteam'] . "',";
	$sql .= $game['awayscore'] . ",";
	$sql .= "'" . $gamedate . "'";
	$sql .= ");";

//	echo $sql . "<br>";
	if ($conn->query($sql) === TRUE) {
		echo "Created record for " . $game['gameid'] . "<br>";
	} else {
		echo "Error: " . $sql . "<br>" . $conn->error;
	}

	$sql = $sqlstart;
}
$conn->close();	
?>
