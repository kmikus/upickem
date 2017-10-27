# Store information regarding path values for gamedata json

class Gamedata:

	# TODO path validator function
	def __init__(self, path='../../resources/gamedata/'):
		self.path = path

	def getPath(self):
		return self.path

	def setPath(self, path):
		self.path = path

# TODO determine what data is missing from season files
	# def getLastComplete(self, season):
	# 	with open(str(season.year)+'.json', 'r'):
		
# TODO
# class Gamewriter:
	
	# 	def __init__(self, season):