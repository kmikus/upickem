import nflgame, nflgame.update_sched
from season import Season

# TODO tons of work to do here, DRY the crap out of this

class Week:
	# Inherit this class for postseason
	default_season = Season()

	def __init__(self, week_num, season=default_season):
		self.season = season
		self.week_num = week_num
		self.year = season.year

	def getSchedule(self):
		# Note that this list is ordered by time
		return nflgame.update_sched.week_schedule(self.year, 'REG', self.week_num)
