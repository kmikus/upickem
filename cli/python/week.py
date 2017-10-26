import nflgame
from season import Season

# TODO tons of work to do here, DRY the crap out of this

class Week:

	def_season = Season()
	def_year = def_season.year

	def __init__(self, ):
		self.year = year
		self.week = week

	def getSchedule(self, season):
		
	
	def getGames():
		games = nflgame.games(self.year, week=self.week)
		games_list = list()
		for game in games:
			date = {"year": game.schedule['year'], "month": game.schedule['month'], "day": game.schedule['day'], "weekday": game.schedule['wday']}
			params = {"season": season, "week": game.schedule['week'], "hometeam": game.home, "homescore": game.score_home, "awayteam": game.away, "awayscore": game.score_away, "date": date, "gameid": game.gamekey}
			games_list.append(params)
		return games_list

	def dump(dest='.', fname=(str(self.year)+'.json'))
		if (dest[len(dest)-1:len(dest)] != '/'):
			dest = dest + '/'
		with open(dest+fname, 'w') as fh
			try:
				fname = str(season) + '_' + str(week) + '.json'
				fpath = "../gamedata/"
				fh = open(fpath + fname, 'w')
				json.dump(games_list, fh)
			except:
				print("There was an error")	

	def cache_games(season, week):
		games = pull_games(season, week)
		dump_games(games, season, week)

	def get_config():
		fpath = "../conf/config.json"
		fh = open(fpath, 'r')
		data = fh.read()
		config_json = json.loads(data)
		return config_json

	def get_season():
		config = get_config()
		return config['season']

	def get_gamedata_path():
		config = get_config()
		return config['gamedatapath']

	def cache_this_season_games(current_week):
		season = get_season()
		for week in range(1, current_week):
			cache_games(season, week)	

	def get_last_completed_week():
	# TODO this	
