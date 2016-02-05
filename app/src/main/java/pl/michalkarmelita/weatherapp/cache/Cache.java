package pl.michalkarmelita.weatherapp.cache;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.base.Optional;
import com.google.gson.Gson;

import pl.michalkarmelita.weatherapp.api.model.LocalWeather;
import pl.michalkarmelita.weatherapp.dagger.ForApplication;

public class Cache {

    private final Gson gson;

    @SuppressLint("CommitPrefEdits")
    public void saveWeather(LocalWeather weather) {
        final String weatherJson = gson.toJson(weather);
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(WEATHER, weatherJson);
        editor.commit();
    }

    private static final String PREFERENCES_NAME = "weather_cache";
    private static final String WEATHER = "weather";

    private SharedPreferences mPreferences;

    public Cache(@ForApplication Context context, Gson gson) {
        this.gson = gson;
        mPreferences = context.getSharedPreferences(PREFERENCES_NAME, 0);
    }

    public Optional<LocalWeather> getWeather() {
        final Optional<String> json = Optional.fromNullable(mPreferences.getString(WEATHER, null));
        if (json.isPresent()) {
            return Optional.of(gson.fromJson(json.get(), LocalWeather.class));
        } else {
            return Optional.absent();
        }
    }

}
