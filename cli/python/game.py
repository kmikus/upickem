import nflgame

class Game:
	
	def __init__(self, week, hometeam):
		self.week = week.week_num
		self.hometeam = hometeam
		self.year = week.year
		
	def getSchedule(self):
		for i, game in enumerate(self.week.getSchedule()):
			if game['home'] == self.hometeam:
				return self.week.getSchedule()[i]
			else:
				return None
	
	# TODO This will only determine if the game is started, need to fix this.
	def isComplete(self):
		return bool(len(nflgame.games(self.year, week=self.week, home=self.hometeam)))
		
	def getHomeScore(self):
		if not self.isComplete():
			return None
		else:
			return nflgame.games(self.year, week=self.week, home=self.hometeam)[0].score_home
			
	def getAwayScore(self):
		if not self.isComplete():
			return None
		else:
			return nflgame.games(self.year, week=self.week, home=self.hometeam)[0].score_away
	
	def getAwayTeam(self):
		return self.getSchedule()['away']