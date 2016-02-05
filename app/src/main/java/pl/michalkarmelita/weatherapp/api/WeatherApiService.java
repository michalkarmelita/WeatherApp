package pl.michalkarmelita.weatherapp.api;

import pl.michalkarmelita.weatherapp.api.model.WeatherResponse;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface WeatherApiService {

    @GET("/weather")
    Observable<WeatherResponse> getWeather(@Query("lat") String lat, @Query("lon") String lon);

}
