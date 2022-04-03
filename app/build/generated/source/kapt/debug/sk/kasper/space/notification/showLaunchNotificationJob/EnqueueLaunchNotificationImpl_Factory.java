package sk.kasper.space.notification.showLaunchNotificationJob;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import sk.kasper.space.notification.EnqueueLaunchNotificationImpl;

import javax.annotation.processing.Generated;
import javax.inject.Provider;

@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class EnqueueLaunchNotificationImpl_Factory implements Factory<EnqueueLaunchNotificationImpl> {
  private final Provider<Context> contextProvider;

  public EnqueueLaunchNotificationImpl_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public EnqueueLaunchNotificationImpl get() {
    return newInstance(contextProvider.get());
  }

  public static EnqueueLaunchNotificationImpl_Factory create(Provider<Context> contextProvider) {
    return new EnqueueLaunchNotificationImpl_Factory(contextProvider);
  }

  public static EnqueueLaunchNotificationImpl newInstance(Context context) {
    return new EnqueueLaunchNotificationImpl(context);
  }
}
