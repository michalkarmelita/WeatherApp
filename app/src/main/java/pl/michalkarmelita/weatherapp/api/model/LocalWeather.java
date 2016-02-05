package pl.michalkarmelita.weatherapp.api.model;

import java.util.List;

import javax.annotation.Nonnull;

public class LocalWeather extends WeatherResponse {

    @Nonnull
    private final Long time;

    public LocalWeather(@Nonnull Long time, @Nonnull Main main, @Nonnull List<Weather> weather, @Nonnull Wind wind) {
        super(weather, main, wind);
        this.time = time;
    }

    @Nonnull
    public Long getTime() {
        return time;
    }
}
