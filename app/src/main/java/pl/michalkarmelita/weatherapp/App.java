package pl.michalkarmelita.weatherapp;

import android.app.Application;

import pl.michalkarmelita.weatherapp.dagger.AppComponent;
import pl.michalkarmelita.weatherapp.dagger.AppModule;
import pl.michalkarmelita.weatherapp.dagger.BaseModule;
import pl.michalkarmelita.weatherapp.dagger.DaggerAppComponent;


public class App extends Application {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        setupGraph();
    }

    private void setupGraph() {
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .baseModule(new BaseModule())
                .build();
        component.inject(this);
    }

    public static AppComponent getAppComponent(Application app) {
        return ((App) app).component;
    }

}
