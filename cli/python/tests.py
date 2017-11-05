import nflwrapper, cache, unittest

class Nflwrappertest(unittest.TestCase):
	def test_teams(self):
		self.assertTrue("PIT" in nflwrapper.teams)
		self.assertTrue("LAR" in nflwrapper.teams)

# class Gametest(unittest.TestCase):
#    def setUp(self):
#		self.testweek1 = nflwrapper.Week(1)
#		self.testweek2 = nflwrapper.Week(7)
#		self.testweek3 = nflwrapper.Week(17)
#        self.test_game1 = Game(self.test_week, 'BAL')
#        
#    def tearDown(

if __name__ == "__main__":
	unittest.main()
