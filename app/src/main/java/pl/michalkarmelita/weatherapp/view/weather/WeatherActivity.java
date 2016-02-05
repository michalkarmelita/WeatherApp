package pl.michalkarmelita.weatherapp.view.weather;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.michalkarmelita.weatherapp.App;
import pl.michalkarmelita.weatherapp.BaseActivity;
import pl.michalkarmelita.weatherapp.R;
import pl.michalkarmelita.weatherapp.dagger.ActivityModule;
import pl.michalkarmelita.weatherapp.dagger.BaseActivityComponent;
import pl.michalkarmelita.weatherapp.presenter.WeatherPresenter;
import pl.michalkarmelita.weatherapp.retain.RetainFragmentManager;
import pl.michalkarmelita.weatherapp.rx.MyViewActions;
import rx.functions.Action1;

public class WeatherActivity extends BaseActivity {


    @InjectView(R.id.root_view)
    View rootView;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.current_condition_value)
    TextView currentCondition;
    @InjectView(R.id.temperature_value)
    TextView temperature;
    @InjectView(R.id.wind_speed_value)
    TextView windSpeed;
    @InjectView(R.id.wind_direction_value)
    TextView windDirection;
    @InjectView(R.id.weather_activity_weather_icon)
    ImageView weatherIcon;
    @InjectView(R.id.fab)
    FloatingActionButton refreshButton;
    @InjectView(R.id.no_data_info)
    TextView noDataInfo;
    @InjectView(R.id.card_view)
    CardView cardView;
    @InjectView(R.id.outdated_info)
    TextView outdatedInfo;
    @InjectView(R.id.progress)
    ProgressBar progress;

    @Inject
    Picasso picasso;

    private WeatherActivityComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final WeatherPresenter presenter = providePresenter(savedInstanceState);

        presenter.dataNotPresentObservable()
                .compose(this.<Boolean>bindToLifecycle())
                .subscribe(RxView.visibility(noDataInfo));

        presenter.dataPresentObservable()
                .compose(this.<Boolean>bindToLifecycle())
                .subscribe(RxView.visibility(cardView));

        presenter.getOutDatedObservable()
                .compose(this.<Boolean>bindToLifecycle())
                .subscribe(RxView.visibility(outdatedInfo));

        presenter.conditionObservable()
                .compose(this.<String>bindToLifecycle())
                .subscribe(RxTextView.text(currentCondition));

        presenter.temperatureObservable()
                .compose(this.<String>bindToLifecycle())
                .subscribe(RxTextView.text(temperature));

        presenter.windSpeedObservable()
                .compose(this.<String>bindToLifecycle())
                .subscribe(RxTextView.text(windSpeed));

        presenter.windDirectionObservable()
                .compose(this.<String>bindToLifecycle())
                .subscribe(RxTextView.text(windDirection));

        presenter.weatherIconObservable()
                .compose(this.<String>bindToLifecycle())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String url) {
                        picasso.load(url)
                                .resizeDimen(R.dimen.icon_size, R.dimen.icon_size)
                                .centerInside()
                                .into(weatherIcon);
                    }
                });

        RxView.clicks(refreshButton)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(presenter.refreshObserver());

        presenter.getErrorObservable()
                .compose(this.bindToLifecycle())
                .subscribe(MyViewActions.showSnackbar(rootView, R.string.weather_activity_error_message));

        presenter.progressBarVisibilityObservable()
                .compose(this.<Boolean>bindToLifecycle())
                .subscribe(RxView.visibility(progress));

    }

    private WeatherPresenter providePresenter(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            final WeatherPresenter weatherPresenter = component.presenter();
            RetainFragmentManager.setObject(this, getSupportFragmentManager(), weatherPresenter);
            return weatherPresenter;
        } else {
            return RetainFragmentManager.getObject(this, getSupportFragmentManager());
        }
    }

    @NonNull
    @Override
    public BaseActivityComponent createActivityComponent(@Nullable Bundle savedInstanceState) {
        component = DaggerWeatherActivityComponent
                .builder()
                .appComponent(App.getAppComponent(getApplication()))
                .activityModule(new ActivityModule(this))
                .weatherActivityModule(new WeatherActivityModule(this))
                .build();
        component.inject(this);
        return component;
    }
}
