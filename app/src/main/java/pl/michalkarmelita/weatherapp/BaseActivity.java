package pl.michalkarmelita.weatherapp;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import pl.michalkarmelita.weatherapp.dagger.BaseActivityComponent;


public abstract class BaseActivity extends RxAppCompatActivity implements BaseActivityComponentProvider {


    private BaseActivityComponent activityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityComponent = createActivityComponent(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @NonNull
    public BaseActivityComponent getActivityComponent() {
        return activityComponent;
    }

}
