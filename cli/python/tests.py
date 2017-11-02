import nflwrapper cache, unittest

class NflgameTest(unittest.TestCase):
    # Write these unit tests

myseason = season.Season()
print('Made a season')
print myseason.year

myweek = week.Week(8)
print myweek.getSchedule()

# get all hometeams for the week to get all scores for the week
fingame = game.Game(myweek, 'BAL')
unfingame = game.Game(myweek, 'DET')

print fingame.isComplete()
print unfingame.isComplete()