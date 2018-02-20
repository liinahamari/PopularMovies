package com.example.guest.popularmovies.dagger.modules;

/**
 * Created by guest on 2/20/18.
 */

import android.content.Context;

import com.example.guest.popularmovies.interfaces.ApplicationScope;
import java.io.File;
import java.util.concurrent.TimeUnit;
import javax.inject.Named;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

@Module(includes = ContextModule.class)
public class OkHttpClientModule {

    @Provides
    public OkHttpClient okHttpClient(Cache cache) {
        return new OkHttpClient()
                .newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .cache(cache)
                .build();
    }

    @Provides
    public Cache cache(File cacheFile) {
        return new Cache(cacheFile, 10 * 1000 * 1024);
    }

    @Provides
    @ApplicationScope
    public File file(@Named("application_context") Context context) {
        File file = new File(context.getCacheDir(), "HttpCache");
        file.mkdirs();
        return file;
    }
}