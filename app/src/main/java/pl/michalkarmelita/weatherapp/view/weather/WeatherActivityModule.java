package pl.michalkarmelita.weatherapp.view.weather;

import javax.annotation.Nonnull;

import dagger.Module;
import dagger.Provides;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;

@Module
public class WeatherActivityModule {

    @Nonnull
    final WeatherActivity activity;

    public WeatherActivityModule(@Nonnull WeatherActivity activity) {
        this.activity = activity;
    }

    @Provides
    public ReactiveLocationProvider provideReactiveLocationProvider() {
        return new ReactiveLocationProvider(activity.getApplicationContext());
    }

}
