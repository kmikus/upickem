# Store information regarding path values for gamedata json

import os.path, nflwrapper

class Gamedata:

	curr_season = nflwrapper.Season()
	default_fname = "gamedata" + str(curr_season.year) + ".json"

	# TODO path validator function
	def __init__(self, path='../../resources/gamedata/', fname=default_fname):
		self.path = path
		self.fname = fname
		self.fullpath = path + fname

	def getPath(self):
		return self.path

	def setPath(self, path):
		self.path = path

	def doesGamedataFileExist(self):
		return os.path.isfile(self.fullpath)

# TODO determine what data is missing from season files
	# def getLastComplete(self, season):
	# 	with open(str(season.year)+'.json', 'r'):
		
# TODO
# class Gamewriter:
	
	# 	def __init__(self, season):
