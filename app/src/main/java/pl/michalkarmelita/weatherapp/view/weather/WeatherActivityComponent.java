package pl.michalkarmelita.weatherapp.view.weather;

import dagger.Component;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import pl.michalkarmelita.weatherapp.dagger.ActivityModule;
import pl.michalkarmelita.weatherapp.dagger.ActivityScope;
import pl.michalkarmelita.weatherapp.dagger.AppComponent;
import pl.michalkarmelita.weatherapp.dagger.BaseActivityComponent;
import pl.michalkarmelita.weatherapp.presenter.WeatherPresenter;

@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = {
                ActivityModule.class,
                WeatherActivityModule.class
        }
)
public interface WeatherActivityComponent extends BaseActivityComponent {

    void inject(WeatherActivity weatherActivity);

    WeatherPresenter presenter();

    ReactiveLocationProvider getReactiveLocationProvider();
}
