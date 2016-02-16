package pl.michalkarmelita.weatherapp.presenter;


import android.content.res.Resources;
import android.location.Location;
import android.support.annotation.NonNull;

import com.appunite.rx.ResponseOrError;
import com.appunite.rx.functions.Functions1;
import com.appunite.rx.observables.NetworkObservableProvider;
import com.appunite.rx.operators.MoreOperators;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import pl.michalkarmelita.weatherapp.R;
import pl.michalkarmelita.weatherapp.api.ApiConstants;
import pl.michalkarmelita.weatherapp.api.WeatherApiService;
import pl.michalkarmelita.weatherapp.api.model.LocalWeather;
import pl.michalkarmelita.weatherapp.api.model.WeatherResponse;
import pl.michalkarmelita.weatherapp.cache.Cache;
import pl.michalkarmelita.weatherapp.dagger.ForActivity;
import pl.michalkarmelita.weatherapp.dagger.NetworkScheduler;
import pl.michalkarmelita.weatherapp.dagger.UiScheduler;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observers.Observers;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

public class WeatherPresenter {

    @Nonnull
    private final Observable<String> conditionObservable;
    @Nonnull
    private final Observable<String> temperatureObservable;
    @Nonnull
    private final Observable<String> windSpeedObservable;
    @Nonnull
    private final Observable<String> windDirectionObservable;
    @Nonnull
    private Observable<String> iconUrlObservable;
    @Nonnull
    private final Observable<Boolean> outDatedObservable;
    private final PublishSubject<Object> refreshSubject = PublishSubject.create();
    @Nonnull
    private final Observable<Object> errorObservable;
    @Nonnull
    private final BehaviorSubject<Boolean> progressBarSubject = BehaviorSubject.create(false);

    @Inject
    public WeatherPresenter(@Nonnull final WeatherApiService weatherApi,
                            @Nonnull @NetworkScheduler Scheduler networkScheduler,
                            @Nonnull @UiScheduler Scheduler uiScheduler,
                            @Nonnull final Cache cache,
                            @Nonnull @ForActivity final Resources resources,
                            @Nonnull final NetworkObservableProvider networkObservableProvider,
                            @Nonnull final ReactiveLocationProvider locationProvider) {

        final Observable<Location> locationObservable = locationProvider.getLastKnownLocation();

        final Observable<ResponseOrError<LocalWeather>> weatherResponseObservable = locationObservable
                .debounce(30, TimeUnit.SECONDS)
                .switchMap(new Func1<Location, Observable<WeatherResponse>>() {
                    @Override
                    public Observable<WeatherResponse> call(Location location) {
                        return weatherApi.getWeather(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                    }
                })
                .map(new Func1<WeatherResponse, LocalWeather>() {
                    @Override
                    public LocalWeather call(WeatherResponse weatherResponse) {
                        final LocalWeather localWeather = new LocalWeather(System.currentTimeMillis(),
                                weatherResponse.getMain(),
                                weatherResponse.getWeather(),
                                weatherResponse.getWind());
                        cache.saveWeather(localWeather);
                        return localWeather;
                    }
                })
                .doOnNext(hideProgressBarAction())
                .compose(ResponseOrError.<LocalWeather>toResponseOrErrorObservable())
                .compose(MoreOperators.<LocalWeather>repeatOnErrorOrNetwork(networkObservableProvider, networkScheduler))
                .compose(MoreOperators.<ResponseOrError<LocalWeather>>refresh(refreshSubject))
                .compose(MoreOperators.<ResponseOrError<LocalWeather>>cacheWithTimeout(uiScheduler));

        errorObservable = weatherResponseObservable
                .compose(ResponseOrError.<LocalWeather>onlyError())
                .map(Functions1.toObject());

        final Observable<LocalWeather> weatherObservable = weatherResponseObservable
                .compose(ResponseOrError.<LocalWeather>onlySuccess())
                .startWith(cache.getWeather().orNull())
                .filter(Functions1.isNotNull());

        conditionObservable = weatherObservable.map(new Func1<LocalWeather, String>() {
            @Override
            public String call(LocalWeather localWeather) {
                return localWeather.getWeather().get(0).getDescription();
            }
        });

        temperatureObservable = weatherObservable.map(new Func1<LocalWeather, String>() {
            @Override
            public String call(LocalWeather localWeather) {
                final double temp = Double.valueOf(localWeather.getMain().getTemp()) - 273;
                return String.format(resources.getString(R.string.weather_activity_temperature_format), temp);
            }
        });

        windSpeedObservable = weatherObservable.map(new Func1<LocalWeather, String>() {
            @Override
            public String call(LocalWeather localWeather) {
                return String.format(resources.getString(R.string.weather_activity_wind_speed_format), localWeather.getWind().getSpeed());
            }
        });
        windDirectionObservable = weatherObservable.map(new Func1<LocalWeather, String>() {
            @Override
            public String call(LocalWeather localWeather) {
                return getDirection(localWeather.getWind().getDeg());
            }
        });

        iconUrlObservable = weatherObservable.map(new Func1<LocalWeather, String>() {
            @Override
            public String call(LocalWeather localWeather) {
                return String.format(ApiConstants.ICON_URL_FORMAT, localWeather.getWeather().get(0).getIcon());
            }
        });

        outDatedObservable = weatherObservable.map(new Func1<LocalWeather, Boolean>() {
            @Override
            public Boolean call(LocalWeather localWeather) {
                return isOutDated(localWeather.getTime());
            }
        });

    }

    @NonNull
    private Action1<Object> hideProgressBarAction() {
        return new Action1<Object>() {
            @Override
            public void call(Object ignore) {
                progressBarSubject.onNext(false);
            }
        };
    }

    private boolean isOutDated(Long updateTime) {
        long timeDiff = System.currentTimeMillis() - updateTime;
        long diffDays = timeDiff / (24 * 60 * 60 * 1000);
        return diffDays > 1;
    }

    private String getDirection(double degrees) {
        if (degrees < 22.5 && degrees >= 0.0) {
            return "N";
        } else if (degrees < 67.5) {
            return "NE";
        } else if (degrees < 112.5) {
            return "E";
        } else if (degrees < 157.5) {
            return "SE";
        } else if (degrees < 202.5) {
            return "S";
        } else if (degrees < 247.5) {
            return "SW";
        } else if (degrees < 292.5) {
            return "W";
        } else if (degrees < 337.5) {
            return "NW";
        } else if (degrees <=360.0 ){
            return "N";
        } else {
            return "Unknown";
        }
    }

    @Nonnull
    public Observable<String> conditionObservable() {
        return conditionObservable;
    }

    @Nonnull
    public Observable<String> temperatureObservable() {
        return temperatureObservable;
    }

    @Nonnull
    public Observable<String> windSpeedObservable() {
        return windSpeedObservable;
    }

    @Nonnull
    public Observable<String> windDirectionObservable() {
        return windDirectionObservable;
    }

    @Nonnull
    public Observable<String> weatherIconObservable() {
        return iconUrlObservable;
    }

    @Nonnull
    public Observable<Boolean> getOutDatedObservable() {
        return outDatedObservable;
    }

    @Nonnull
    public Observer<? super Void> refreshObserver() {
        return Observers.create(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                refreshSubject.onNext(aVoid);
                progressBarSubject.onNext(true);
            }
        });
    }

    @Nonnull
    public Observable<Boolean> dataPresentObservable() {
        return conditionObservable
                .map(Functions1.returnTrue())
                .startWith(false)
                .distinctUntilChanged();
    }

    @Nonnull
    public Observable<Boolean> dataNotPresentObservable() {
        return dataPresentObservable()
                .map(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean) {
                        return !aBoolean;
                    }
                });
    }

    @Nonnull
    public Observable<Object> getErrorObservable() {
        return errorObservable;
    }

    @Nonnull
    public Observable<Boolean> progressBarVisibilityObservable() {
        return progressBarSubject;
    }
}
