<?php
	function jsonPrint($json) {
		echo "<pre>" . var_export($json, true) . "</pre>";
	}

	// TODO setup configuration file for gamedata path
	function getGameJson($season, $week) {
		$gamedata_path = json_decode(file_get_contents("../conf/gamedatapath.json"), true);
		$gamedata_path = $gamedata_path['path'];
		$filepath = $gamedata_path . $season . "_" . $week . ".json";
		$json = json_decode(file_get_contents($filepath), true);
		return $json;
	}

	function prepDate($year, $month, $day) {
		$year = strval($year);
		$month = strlen(strval($month)) === 1 ? "0" . strval($month) : strval($month);
		$day = strlen(strval($day)) === 1 ? "0" . strval($day) : strval($day);
		return ($year . "-" . $month . "-" . $day);
	}
?>
