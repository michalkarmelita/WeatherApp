# WeatherApp

WeatherApp is simple showcase appliaction that shows current conditions (sunny, foggy, raining, etc.), temperature, wind speed and direction. By use of OpenWeatherMap.org API.
The application folows MVP convention and uses RxJava as well as dependency injection.

#### Implemented features:

* The weather information are cached for future offline use.
* If user is offline, and there is weather information cached that is less than 24 hours old. The last known conditions and location is shown along with a prominent display to indicate when the data was last updated.
* If offline, and there are no previous conditions known, or the previous conditions are more than 24 hours old:
  - A screen is displayed to indicate there is no previous data available.
  - A button is displayed to allow a user to refresh the data.
* If user refresh the data manually, and is offline, a message is displayed to indicate that user need to connect to the Internet in order to get updated data.
* The app display a loading indicator if it is fetching data.


# Buid instructions

After cloning this repository please make sure that submodules was initialized and updated.
```
git submodule init
git submodule update
```
