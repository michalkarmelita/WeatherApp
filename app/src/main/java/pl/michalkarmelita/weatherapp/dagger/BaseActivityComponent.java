package pl.michalkarmelita.weatherapp.dagger;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.Gson;

import dagger.Component;

@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = {
                ActivityModule.class,
        }
)
public interface BaseActivityComponent {

    @ForActivity
    Resources getResources();

    @ForApplication
    Context getApplicationContext();

    @ForActivity
    Context getActivityContext();

    Gson getGson();

}