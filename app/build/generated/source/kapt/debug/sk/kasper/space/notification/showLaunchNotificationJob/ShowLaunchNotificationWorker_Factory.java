package sk.kasper.space.notification.showLaunchNotificationJob;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import sk.kasper.domain.usecase.ShowLaunchNotification;
import sk.kasper.space.notification.ShowLaunchNotificationWorker;

@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class ShowLaunchNotificationWorker_Factory {
  private final Provider<ShowLaunchNotification> showLaunchNotificationProvider;

  public ShowLaunchNotificationWorker_Factory(
      Provider<ShowLaunchNotification> showLaunchNotificationProvider) {
    this.showLaunchNotificationProvider = showLaunchNotificationProvider;
  }

  public ShowLaunchNotificationWorker get(Context appContext, WorkerParameters workerParams) {
    return newInstance(appContext, workerParams, showLaunchNotificationProvider.get());
  }

  public static ShowLaunchNotificationWorker_Factory create(
      Provider<ShowLaunchNotification> showLaunchNotificationProvider) {
    return new ShowLaunchNotificationWorker_Factory(showLaunchNotificationProvider);
  }

  public static ShowLaunchNotificationWorker newInstance(Context appContext,
      WorkerParameters workerParams, ShowLaunchNotification showLaunchNotification) {
    return new ShowLaunchNotificationWorker(appContext, workerParams, showLaunchNotification);
  }
}
