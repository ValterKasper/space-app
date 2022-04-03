package sk.kasper.space.notification.showLaunchNotificationJob;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
import dagger.internal.InstanceFactory;
import sk.kasper.space.notification.ShowLaunchNotificationWorker;

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
public final class ShowLaunchNotificationWorker_Factory_Impl implements ShowLaunchNotificationWorker.Factory {
  private final ShowLaunchNotificationWorker_Factory delegateFactory;

  ShowLaunchNotificationWorker_Factory_Impl(ShowLaunchNotificationWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public ShowLaunchNotificationWorker create(Context appContext, WorkerParameters workerParams) {
    return delegateFactory.get(appContext, workerParams);
  }

  public static Provider<ShowLaunchNotificationWorker.Factory> create(
      ShowLaunchNotificationWorker_Factory delegateFactory) {
    return InstanceFactory.create(new ShowLaunchNotificationWorker_Factory_Impl(delegateFactory));
  }
}
