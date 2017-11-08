import nflwrapper, cache

phi = nflwrapper.Game("PHI")
writer = cache.Gamewriter(nflwrapper.Season())
print writer.getGameDict(phi)
