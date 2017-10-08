import nflgame
import json

def pull_games(year, week):
	games = nflgame.games(year, week=week)
	games_list = list()
	for game in games:
		date = {"year": game.schedule['year'], "month": game.schedule['month'], "day": game.schedule['day'], "weekday": game.schedule['wday']}
		games_list.append({"season": year, "week": week, "hometeam": game.home, "homescore": game.score_home, "awayteam": game.away, "awayscore": game.score_away, "date": date})
	return games_list

# TODO input validation to make sure week and year are same for all
def dump_games(games_list):
	if games_list:
		try:
			fname = str(games_list[0]['season']) + "_" + str(games_list[0]['week']) + ".json"
			fpath = "../gamedata/"
			fh = open(fpath + fname, 'w')
			json.dump(games_list, fh)
		except:
			print("There was an error")	

def cache_games(year, week):
	games = pull_games(year, week)
	dump_games(games)
