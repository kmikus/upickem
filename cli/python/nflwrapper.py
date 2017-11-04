import nflgame, nflgame.update_sched, datetime

today = datetime.date.today()

class Season:

	default_year = today.year if today.month > 2 else today.year-1

	# TODO force use a date object for first_game_date
	def __init__(self, year=default_year):
		self.year = year
		
	def getSchedule(self):
		schedule = []
		for weeknum in range(1, 18):
			schedule.append(nflgame.update_sched.week_schedule(self.year, 'REG', weeknum))
		return schedule
		
# class CurrSeason(Season):

	# TODO
	
# class PostSeason(Season):

	# TODO
	
class Week:
	
	default_season = Season()

	def __init__(self, week_num, season=default_season):
		self.season = season
		self.week_num = week_num
		self.year = season.year

	def getSchedule(self):
		# note that this list is ordered by time
		return self.season.getSchedule()[self.week_num-1]
		
	def getHometeams(self):
		for game in self.getSchedule():
			return list().append(game['home'])

# class PostWeek(Week):
	
	# TODO
		
class Game:
	
	# TODO change constructor to take home or away team
	# TODO use other seasons other than the current
	
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
	
	# empty array for false, 1 game object for true
	def hasData(self):
		return nflgame.games(self.year, week=self.week, home=self.hometeam)
		
	def getNflgameObj(self):
		return self.hasData()[0] if self.hasData() else None
	
	# togo (time left in game) will be 0 when game is complete
	def isComplete(self):
		game = self.getNflgameObj()
		return not game.togo if game else None
		
	def getHomeScore(self):
		if not self.isComplete():
			return None
		else:
			return self.getNflgameObj().score_home
			
	def getAwayScore(self):
		if not self.isComplete():
			return None
		else:
			return self.getNflgameObj().score_away
	
	def getAwayTeam(self):
		return self.getSchedule()['away']

