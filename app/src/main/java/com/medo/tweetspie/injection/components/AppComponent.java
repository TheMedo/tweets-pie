package com.medo.tweetspie.injection.components;

import com.medo.tweetspie.injection.modules.AppModule;
import com.medo.tweetspie.system.PreferencesInteractor;
import com.medo.tweetspie.system.StringInteractor;
import com.medo.tweetspie.system.SystemModule;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {
        AppModule.class,
        SystemModule.class
})
public interface AppComponent {

  PreferencesInteractor getPreferences();

  StringInteractor getStrings();
}