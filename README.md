# My work list Application

#Requirements:
1. App should use Trello API
2. App should work online and offline
3. App should be able to create, edit, remove cards
4. App should be able to move cards within 3 lists

#Assumptions:
1. For alpha there is no timestamp to determine which posts are most important (server side or client side) – so app will download posts from server on first run and then override any data on server to be same as on device.
2. Cards should be added only for TODO list, but there should be possibility to unlock option to add it to any from code.
3. Lists ID and server authentication should be provided inside code
4. On first run app requires internet connection
5. App supports only portrait orientation (activities are locked on it)


#Development requirements:
- Android Studio using current android build tools (in code is used beta version but can be changed to any other)

#Run requirements
- Android device with unlocked 'external sources' for installation purposes
- At least device with Android ICS (4.0+)
