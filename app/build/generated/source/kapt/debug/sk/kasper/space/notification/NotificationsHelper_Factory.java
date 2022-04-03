package sk.kasper.space.notification;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import sk.kasper.ui_common.mapper.RocketMapper;

@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class NotificationsHelper_Factory implements Factory<NotificationsHelper> {
  private final Provider<RocketMapper> rocketMapperProvider;

  private final Provider<Context> contextProvider;

  public NotificationsHelper_Factory(Provider<RocketMapper> rocketMapperProvider,
      Provider<Context> contextProvider) {
    this.rocketMapperProvider = rocketMapperProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public NotificationsHelper get() {
    return newInstance(rocketMapperProvider.get(), contextProvider.get());
  }

  public static NotificationsHelper_Factory create(Provider<RocketMapper> rocketMapperProvider,
      Provider<Context> contextProvider) {
    return new NotificationsHelper_Factory(rocketMapperProvider, contextProvider);
  }

  public static NotificationsHelper newInstance(RocketMapper rocketMapper, Context context) {
    return new NotificationsHelper(rocketMapper, context);
  }
}
