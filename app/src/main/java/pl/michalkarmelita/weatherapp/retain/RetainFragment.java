package pl.michalkarmelita.weatherapp.retain;

import android.support.v4.app.Fragment;

public class RetainFragment<T> extends Fragment {

    public static <T> RetainFragment<T> newInstance() {
        final RetainFragment<T> retainFragment = new RetainFragment<>();
        retainFragment.setRetainInstance(true);
        return retainFragment;
    }

    private T object;

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
