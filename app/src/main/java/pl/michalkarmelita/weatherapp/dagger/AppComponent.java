package pl.michalkarmelita.weatherapp.dagger;

import android.app.Application;
import android.content.Context;

import com.appunite.rx.observables.NetworkObservableProvider;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Component;
import pl.michalkarmelita.weatherapp.App;
import pl.michalkarmelita.weatherapp.api.WeatherApiService;
import pl.michalkarmelita.weatherapp.cache.Cache;
import rx.Scheduler;

@Singleton
@Component(
        modules = {
                AppModule.class,
                BaseModule.class
        }
)
public interface AppComponent {

    void inject(App app);

    Application provideApplication();

    WeatherApiService getWeatherApiService();

    @UiScheduler
    Scheduler getUiScheduler();

    @NetworkScheduler
    Scheduler getNetworkScheduler();

    @ForApplication
    Context getContext();


    Picasso getPicasso();

    Gson getGson();

    NetworkObservableProvider getNetworkObservableProvider();

    Cache getCache();

}