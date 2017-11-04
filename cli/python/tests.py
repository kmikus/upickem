import nflwrapper cache, unittest

class NflgameTest(unittest.TestCase):
    def setUp(self):
        self.test_season = Season()
        self.test_week = Week(8)
        self.test_game = Game(self.test_week, 'BAL')
        
    def
    
# myseason = season.Season()
# print('Made a season')
# print myseason.year

# myweek = week.Week(8)
# print myweek.getSchedule()

# # get all hometeams for the week to get all scores for the week
# fingame = game.Game(myweek, 'BAL')
# unfingame = game.Game(myweek, 'DET')

# print fingame.isComplete()
# print unfingame.isComplete()