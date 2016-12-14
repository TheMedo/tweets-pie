package com.medo.tweetspie.injection.components;

import com.medo.tweetspie.database.RealmModule;
import com.medo.tweetspie.injection.scopes.UserScope;
import com.medo.tweetspie.main.MainActivity;
import com.medo.tweetspie.main.MainModule;
import com.medo.tweetspie.main.viewer.PhotoFragment;
import com.medo.tweetspie.main.viewer.ViewerActivity;
import com.medo.tweetspie.main.viewer.ViewerModule;
import com.medo.tweetspie.rest.TwitterModule;
import com.medo.tweetspie.service.TimelineService;

import dagger.Component;


@UserScope
@Component(dependencies = AppComponent.class, modules = {
        MainModule.class,
        RealmModule.class,
        TwitterModule.class,
        ViewerModule.class
})
public interface UserComponent {

  void inject(MainActivity activity);

  void inject(TimelineService service);

  void inject(ViewerActivity activity);

  void inject(PhotoFragment fragment);
}