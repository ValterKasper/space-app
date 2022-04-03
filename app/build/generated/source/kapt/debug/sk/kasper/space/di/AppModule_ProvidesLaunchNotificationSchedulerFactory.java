package sk.kasper.space.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import sk.kasper.base.notification.EnqueueLaunchNotification;
import sk.kasper.space.notification.EnqueueLaunchNotificationImpl;

@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class AppModule_ProvidesLaunchNotificationSchedulerFactory implements Factory<EnqueueLaunchNotification> {
  private final AppModule module;

  private final Provider<EnqueueLaunchNotificationImpl> launchNotificationSchedulerProvider;

  public AppModule_ProvidesLaunchNotificationSchedulerFactory(AppModule module,
      Provider<EnqueueLaunchNotificationImpl> launchNotificationSchedulerProvider) {
    this.module = module;
    this.launchNotificationSchedulerProvider = launchNotificationSchedulerProvider;
  }

  @Override
  public EnqueueLaunchNotification get() {
    return providesLaunchNotificationScheduler(module, launchNotificationSchedulerProvider.get());
  }

  public static AppModule_ProvidesLaunchNotificationSchedulerFactory create(AppModule module,
      Provider<EnqueueLaunchNotificationImpl> launchNotificationSchedulerProvider) {
    return new AppModule_ProvidesLaunchNotificationSchedulerFactory(module, launchNotificationSchedulerProvider);
  }

  public static EnqueueLaunchNotification providesLaunchNotificationScheduler(AppModule instance,
      EnqueueLaunchNotificationImpl launchNotificationScheduler) {
    return Preconditions.checkNotNullFromProvides(instance.providesLaunchNotificationScheduler(launchNotificationScheduler));
  }
}
