package pl.michalkarmelita.weatherapp.api;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

public class ApiErrorHandler implements ErrorHandler {

    @Override
    public Throwable handleError(RetrofitError cause) {
        final RetrofitError.Kind kind = cause.getKind();
        switch (kind) {
            case NETWORK:
                return new NoConnectionException();
            case HTTP:
                return new HttpErrorException();
            case UNEXPECTED:
                return new RuntimeException("Cannot perform request");
            case CONVERSION:
            default:
                return new UnknownErrorException();
        }
    }
}