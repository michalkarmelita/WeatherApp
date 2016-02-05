package pl.michalkarmelita.weatherapp.api.model;

import javax.annotation.Nonnull;

public class Weather {

    @Nonnull
    private final String main;
    @Nonnull
    private final String description;
    @Nonnull
    private final String icon;

    public Weather(@Nonnull String main, @Nonnull String description, @Nonnull String icon) {
        this.main = main;
        this.description = description;
        this.icon = icon;
    }

    @Nonnull
    public String getMain() {
        return main;
    }

    @Nonnull
    public String getDescription() {
        return description;
    }

    @Nonnull
    public String getIcon() {
        return icon;
    }
}
