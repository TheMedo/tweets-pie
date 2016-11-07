package com.medo.tweetspie.injection.components;

import com.medo.tweetspie.injection.scopes.OnboardingScope;
import com.medo.tweetspie.onboarding.OnboardingActivity;
import com.medo.tweetspie.onboarding.OnboardingModule;

import dagger.Component;


@OnboardingScope
@Component(dependencies = AppComponent.class, modules = OnboardingModule.class)
public interface OnboardingComponent {

  void inject(OnboardingActivity activity);
}