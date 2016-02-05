package pl.michalkarmelita.weatherapp.retain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import static com.google.common.base.Preconditions.checkNotNull;

public class RetainFragmentManager {

    private static final String RETAIN_FRAGMENT_TAG = "retain_fragment";

    @Nullable
    private static <T> RetainFragment<T> getInstance(FragmentManager fragmentManager, String tag) {
        //noinspection unchecked
        return (RetainFragment<T>) fragmentManager.findFragmentByTag(tag);
    }

    @NonNull
    public static <T> T getObject(Object tag, FragmentManager fragmentManager) {
        final RetainFragment<T> instance = checkNotNull(RetainFragmentManager
                .<T>getInstance(fragmentManager, getTag(tag)));
        return checkNotNull(instance.getObject());
    }

    public static <T> void setObject(Object tag, FragmentManager fragmentManager, T object) {
        RetainFragment<T> instance = getInstance(fragmentManager, getTag(tag));
        if (instance == null) {
            instance = RetainFragment.newInstance();
            fragmentManager
                    .beginTransaction()
                    .add(instance, getTag(tag))
                    .commit();
        }
        instance.setObject(object);
    }

    @NonNull
    private static String getTag(Object object) {
        return RETAIN_FRAGMENT_TAG + object.getClass().getCanonicalName();
    }
}