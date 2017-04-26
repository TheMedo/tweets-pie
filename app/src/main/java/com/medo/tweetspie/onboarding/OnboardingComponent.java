package com.medo.tweetspie.onboarding;

import com.medo.tweetspie.injection.components.AppComponent;

import dagger.Component;


@OnboardingScope
@Component(dependencies = AppComponent.class, modules = OnboardingModule.class)
interface OnboardingComponent {

  void inject(OnboardingActivity activity);
}