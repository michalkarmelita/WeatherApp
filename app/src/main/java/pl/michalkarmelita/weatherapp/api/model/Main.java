package pl.michalkarmelita.weatherapp.api.model;

import javax.annotation.Nonnull;

public class Main {

    @Nonnull
    private final String temp;
    @Nonnull
    private final String humidity;
    @Nonnull
    private final String pressure;
    @Nonnull
    private final String tempMin;
    @Nonnull
    private final String tempMax;

    public Main(@Nonnull String temp, @Nonnull String humidity, @Nonnull String pressure, @Nonnull String tempMin, @Nonnull String tempMax) {
        this.temp = temp;
        this.humidity = humidity;
        this.pressure = pressure;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
    }

    @Nonnull
    public String getTemp() {
        return temp;
    }

    @Nonnull
    public String getHumidity() {
        return humidity;
    }

    @Nonnull
    public String getPressure() {
        return pressure;
    }

    @Nonnull
    public String getTempMin() {
        return tempMin;
    }

    @Nonnull
    public String getTempMax() {
        return tempMax;
    }
}
