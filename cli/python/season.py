import nflgame
from datetime import date

today = datetime.today()

class Season:

	def_year = today.year if today.month > 2 else today.year-1

	# TODO force use a date object for first_game_date
	def __init__(self, year=current_year):
		self.year = year

class CurrSeason(Season):

	# TODO fix this
	def getCurrentWeek(self):
		today = date.today()
		firstgame = self.first_game_date
		daysdiff = (abs(today - firstgame)).days
		return daysdiff // 7
