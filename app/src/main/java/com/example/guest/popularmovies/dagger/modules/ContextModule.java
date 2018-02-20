package com.example.guest.popularmovies.dagger.modules;

/**
 * Created by guest on 2/20/18.
 */

import android.content.Context;
import com.example.guest.popularmovies.interfaces.ApplicationScope;
import javax.inject.Named;
import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {

    Context context;

    public ContextModule(Context context){
        this.context = context;
    }

    @Named("application_context")
    @ApplicationScope
    @Provides
    public Context context(){ return context.getApplicationContext(); }
}