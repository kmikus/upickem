<?php
	require "../includes/conn/db_conn.php";
	require "../includes/functions.php";
	
	//TODO setup functionality to automatically update db at certain times for appropriate week
	$games = getGameJson(2017, 1);
	
?>
