import nflwrapper, cache, unittest

num_of_weeks = 17
num_of_teams = 32

# TODO use test suites or break up into different classes for more organization

class NflwrapperTest(unittest.TestCase):
	
	# unit test setup	
	def setUp(self):
		self.season = nflwrapper.Season()
		self.week1 = nflwrapper.Week(1)
		self.week2 = nflwrapper.Week(9)
		self.week3 = nflwrapper.Week(17)
		self.testweeks = [self.week1, self.week2 self.week3]

	def tearDown(self):
		self.season = None
		self.week1 = None
		self.week17 = None
		self.testweeks = None

	# season class
	def test_teams(self):
		self.assertTrue("PIT" in nflwrapper.teams)
		self.assertEqual(len(nflwrapper.teams), num_of_teams)

	def test_getSeasonSchedule(self):
		self.assertEqual(len(self.season.getSchedule()), num_of_weeks)

	# week class
	def test_getWeekSchedule(self):
		for week in self.testweeks: self.assertTrue(len(week.getSchedule()) > num_of_teams/4)

	def test_getMatchups(self):
		for week in self.testweeks: self.assertTrue(len(week.getMatchups()) > 0)

	def test_getHometeams(self):
		for week in self.testweeks: self.assertTrue(len(week.getHometeams()) > num_of_teams/4)
		for week in self.testweeks: self.assertNotIn(week.getHometeams()[0], week.getAwayteams())

	def test_getAwayteams(self):
		for week in self.testweeks: self.assertTrue(len(week.getAwayteams()) > num_of_teams/4)
		for week in self.testweeks: self.assertNotIn(week.getAwayteams()[0], week.getHometeams())

	# game class
	# def test_getGameSchedule():
		# TODO
	

#class Gametest(unittest.TestCase):
#   def setUp(self):
#   	self.testweek1 = nflwrapper.Week(1)
#   	self.testweek2 = nflwrapper.Week(7)
#   	self.testweek3 = nflwrapper.Week(17)
#       self.test_game1 = Game(self.test_week, 'BAL')
#       
#   def tearDown(
#
if __name__ == "__main__":
	unittest.main()
