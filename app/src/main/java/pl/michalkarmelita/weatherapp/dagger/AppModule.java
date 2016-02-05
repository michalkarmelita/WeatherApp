package pl.michalkarmelita.weatherapp.dagger;

import android.app.Application;
import android.content.Context;

import com.appunite.rx.android.NetworkObservableProviderImpl;
import com.appunite.rx.observables.NetworkObservableProvider;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.michalkarmelita.weatherapp.App;
import pl.michalkarmelita.weatherapp.api.ApiConstants;
import pl.michalkarmelita.weatherapp.api.WeatherApiService;
import pl.michalkarmelita.weatherapp.cache.Cache;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

@Module
public final class AppModule {

    private static final String TAG = AppModule.class.getCanonicalName();

    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return app;
    }

    @Provides
    @Singleton
    @ForApplication
    public Context activityContext() {
        return app.getApplicationContext();
    }

    @Provides
    @Singleton
    public Cache provideCache(@ForApplication Context context, Gson gson) {
        return new Cache(context, gson);
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(@ForApplication Context context) {
        return new OkHttpClient();
    }

    @Provides
    @Singleton
    Picasso providePicasso(@ForApplication Context context, OkHttpClient okHttpClient) {
        return new Picasso.Builder(context)
                .downloader(new OkHttpDownloader(okHttpClient))
                .build();
    }

    @Provides
    @Singleton
    RestAdapter provideRestAdapterBuilder(Gson gson,
                                          OkHttpClient client) {
        return new RestAdapter.Builder()
                .setClient(new OkClient(client))
                .setConverter(new GsonConverter(gson))
                .setEndpoint(ApiConstants.API_ENDPOINT)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addQueryParam("appid", ApiConstants.APP_ID);
                    }
                })
                .build();
    }

    @Provides
    @Singleton
    public WeatherApiService provideApiService(final RestAdapter adapter) {
        return adapter.create(WeatherApiService.class);
    }

    @Provides
    NetworkObservableProvider provideNetworkProvider(@ForApplication Context context) {
        return new NetworkObservableProviderImpl(context);
    }

}
