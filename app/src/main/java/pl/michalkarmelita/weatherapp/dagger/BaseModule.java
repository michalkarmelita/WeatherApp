package pl.michalkarmelita.weatherapp.dagger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module
public final class BaseModule {

    @Provides
    @Singleton
    public Gson provideGson() {
        return new GsonBuilder()
                .create();
    }

    @Provides
    @UiScheduler
    Scheduler provideUiScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @NetworkScheduler
    Scheduler provideNetworkScheduler() {
        return Schedulers.io();
    }

}