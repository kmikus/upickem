# Store information regarding path values for gamedata json

import os.path, nflwrapper, datetime

class Gamedata:

	curr_season = nflwrapper.Season()
	default_fname = "gamedata" + str(curr_season.year) + ".json"

	# TODO path validator function
	def __init__(self, path='../../resources/gamedata/', fname=default_fname):
		self.path = path
		self.fname = fname
		self.fullpath = path + fname

	def getPath(self):
		return self.path

	def setPath(self, path):
		self.path = path

	def doesGamedataFileExist(self):
		return os.path.isfile(self.fullpath)

	#TODO def getLastCompleteWeek(self):

class Gamewriter:
	
	def __init__(self, season):
		self.season = nflwrapper.Season(season)

	def getGameDict(self, game):
		sch = game.getSchedule()
		# using the nflgame api for the id here, could change in the future to use own
		gameid = int(sch["gamekey"])
		hometeam, homescore = game.hometeam, game.getHomeScore()
		awayteam, awayscore = game.getAwayTeam(), game.getAwayScore()
		gamedate = datetime.date(sch["year"], sch["month"], sch["day"])
		gameDict ={"gameid": gameid, "hometeam": hometeam, "homescore": homescore, "awayteam": awayteam, "awayscore": awayscore, "gamedate": gamedate}
		return gameDict

#	def prepareJsonDict(self):
#		final_week = self.season.getCurrentWeek()
#		if final_week not in range(1, nflwrapper.num_of_weeks): raise ValueError("The week is not in the specified range, see Season.getCurrentWeek")
#		for i in range(1, final_week+1):
			
			
				
