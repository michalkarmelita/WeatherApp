package pl.michalkarmelita.weatherapp.dagger;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity activity) {
        mActivity = activity;
    }

    @Provides
    public LayoutInflater provideLayoutInflater(@ForActivity Context context) {
        return LayoutInflater.from(context);
    }

    @Provides
    @ForActivity
    public Resources provideResources() {
        return mActivity.getResources();
    }

    @Provides
    @ForActivity
    public Context provideContext() {
        return mActivity;
    }

}
