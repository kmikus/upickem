# Most of the logic centers around the home team, home teams always come first or are used as inputs

import nflgame, nflgame.update_sched, datetime

today = datetime.date.today()
teams = ("ARI","ATL","BAL","BUF","CAR","CHI","CIN","CLE","DAL","DEN","DET","GB","HOU","IND","JAX","KC","LAC","LAR","MIA","MIN","NE","NO","NYG","NYJ","OAK","PHI","PIT","SEA","SF","TB","TEN","WAS")
num_of_teams = len(teams)
num_of_weeks = 17

class Season:

	default_year = today.year if today.month > 2 else today.year-1

	# TODO force use a date object for first_game_date
	def __init__(self, year=default_year):
		self.year = year
		
	def getSchedule(self):
		schedule = []
		for weeknum in range(1, 18):
			schedule.append(nflgame.update_sched.week_schedule(self.year, "REG", weeknum))
		return schedule

	def getCurrentWeek(self):
		schedule = self.getSchedule()
		current_week = None
		for week in schedule:
			first_game = week[0]
			week = first_game["week"]
			year, month, day = first_game["year"], first_game["month"], first_game["day"]
			if datetime.date(year, month, day) > today:
				current_week = week
				break
		return current_week
		
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

	# note that this list is ordered by time
	def getSchedule(self):
		return self.season.getSchedule()[self.week_num-1]
	
	# return an array of tuples with the hometeam first
	def getMatchups(self):
		games = []
		for game in self.getSchedule():
			games.append((game["home"], game["away"]))
		return games

	def getHometeams(self):
		hometeams = [game[0] for game in self.getMatchups()]
		return hometeams

	def getAwayteams(self):
		awayteams = [game[1] for game in self.getMatchups()]
		return awayteams

# class PostWeek(Week):
	
	# TODO
		
class Game:
	
	# TODO change constructor to take home or away team
	# TODO use other seasons other than the current
	# TODO default week is current week
	
	def __init__(self, week, hometeam):
		# yeah I poorly named week
		self.week = week.week_num
		self.weekObj = week
		self.hometeam = hometeam
		self.year = week.year
		
	def getSchedule(self):
		for game in self.weekObj.getSchedule():
			if game["home"] == self.hometeam:
				return game
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
		if not self.hasData():
			return None
		else:
			return self.getNflgameObj().score_home
			
	def getAwayScore(self):
		if not self.hasData():
			return None
		else:
			return self.getNflgameObj().score_away
	
	def getAwayTeam(self):
		return self.getSchedule()["away"]

	def isHomeWinner(self):
		if self.isComplete():
			return self.getHomeScore() > self.getAwayScore()
		else:
			return None

