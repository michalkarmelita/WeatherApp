package pl.michalkarmelita.weatherapp.rx;

import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

import javax.annotation.Nonnull;

import rx.functions.Action1;

public class MyViewActions {


    @Nonnull
    public static Action1<Object> showSnackbar(final @Nonnull View view,
                                               final @StringRes int text) {
        return showSnackbar(view, text, Snackbar.LENGTH_LONG);
    }

    @Nonnull
    public static Action1<Object> showSnackbar(final @Nonnull View view,
                                               final @StringRes int text,
                                               final int duration) {
        return new Action1<Object>() {
            @Override
            public void call(Object o) {
                Snackbar.make(view, text, duration).show();
            }
        };
    }

}
