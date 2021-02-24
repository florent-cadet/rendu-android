package android.eservices.rendu;

import android.app.Application;
import android.eservices.rendu.data.di.FakeDependencyInjection;

import com.facebook.stetho.Stetho;

/**
 * In this sample application, we decided not to use dependency injection.
 * Thus, there are some objects we don't want to recreate, such as retrofit, gson or API services.
 * Here we use the Application class to store our singletons, but in real case, we shall use DI to
 * handle these issues. In other words, please do not do that in production-aimed apps.
 */
public class MovieApplication extends Application {

    public static final String API_KEY = "5a3b517c702cbcfbd2bcdb128c9a3d66";
    public static final String LANGUAGE = "fr";


    public static final String displayChangeAction = "DISPLAY_CHANGE";

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        FakeDependencyInjection.setContext(this);
    }
}
