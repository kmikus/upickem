import nflwrapper, cache, unittest, random

num_of_weeks = 17
num_of_teams = 32
# use a home team on first game for test team, preferably first game of season
test_team = "NE"

# TODO use test suites or break up into different classes for more organization
# TODO Game class uses hometeams as a constructor. theres probably a better way

class NflwrapperTest(unittest.TestCase):
	
	# unit test setup	
	def setUp(self):
		self.season = nflwrapper.Season()
		self.week1 = nflwrapper.Week(1)
		self.week2 = nflwrapper.Week(9)
		self.week3 = nflwrapper.Week(17)
		self.testweeks = [self.week1, self.week2, self.week3]
		self.game = nflwrapper.Game(self.week1, test_team)

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

	# gets a random hometeam for the test weeks
	def test_getGameSchedule(self):
		for week in self.testweeks:
			testgame = nflwrapper.Game(week, week.getHometeams()[random.randint(0, len(week.getHometeams())-1)])
			self.assertIs(type(testgame.getSchedule()), dict)

	def test_hasData(self):
		self.assertIsNotNone(self.game.hasData())

	def test_getNflGameObj(self):
		self.assertIsNotNone(self.game.getNflgameObj())

	def test_isComplete(self):
		self.assertTrue(self.game.isComplete())

	def test_getHomeScore(self):
		self.assertIs(type(self.game.getHomeScore()), int)

	def test_getAwayScore(self):
		self.assertIs(type(self.game.getAwayScore()), int)

	def test_getAwayTeam(self):
		self.assertIs(type(self.game.getAwayTeam()), unicode)

	def isHomeWinner(self):
		self.assertIs(type(self.game.isHomeWinner()), bool)

class CacheTest(unittest.TestCase):

	def setUp(self):
		# TODO
	
if __name__ == "__main__":
	unittest.main()
