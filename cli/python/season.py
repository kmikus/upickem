import nflgame, datetime

today = datetime.date.today()

class Season:

	default_year = today.year if today.month > 2 else today.year-1

	# TODO force use a date object for first_game_date
	def __init__(self, year=default_year):
		self.year = year

# class CurrSeason(Season):

	# TODO