package pl.michalkarmelita.weatherapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import pl.michalkarmelita.weatherapp.dagger.BaseActivityComponent;


public interface BaseActivityComponentProvider {

    @NonNull
    BaseActivityComponent createActivityComponent(@Nullable Bundle savedInstanceState);

}
