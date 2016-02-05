package pl.michalkarmelita.weatherapp.api.model;

import java.util.List;

import javax.annotation.Nonnull;

public class WeatherResponse {

    @Nonnull
    private final List<Weather> weather;
    @Nonnull
    private final Main main;
    @Nonnull
    private final Wind wind;

    public WeatherResponse(@Nonnull List<Weather> weather, @Nonnull Main main, @Nonnull Wind wind) {
        this.weather = weather;
        this.main = main;
        this.wind = wind;
    }

    @Nonnull
    public List<Weather> getWeather() {
        return weather;
    }

    @Nonnull
    public Main getMain() {
        return main;
    }

    @Nonnull
    public Wind getWind() {
        return wind;
    }
}
