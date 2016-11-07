package com.medo.tweetspie.onboarding;

import com.medo.tweetspie.injection.scopes.OnboardingScope;
import com.medo.tweetspie.system.PreferencesInteractor;
import com.medo.tweetspie.system.StringInteractor;

import dagger.Module;
import dagger.Provides;


@Module
public class OnboardingModule {

  @Provides
  @OnboardingScope
  OnboardingPresenter provideOnboardingPresenter(PreferencesInteractor preferences, StringInteractor strings) {

    return new OnboardingPresenter(preferences, strings);
  }

}
