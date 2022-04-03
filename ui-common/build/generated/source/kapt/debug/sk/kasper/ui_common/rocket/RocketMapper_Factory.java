package sk.kasper.ui_common.rocket;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import sk.kasper.ui_common.mapper.RocketMapper;

import javax.annotation.processing.Generated;

@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class RocketMapper_Factory implements Factory<RocketMapper> {
  @Override
  public RocketMapper get() {
    return newInstance();
  }

  public static RocketMapper_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static RocketMapper newInstance() {
    return new RocketMapper();
  }

  private static final class InstanceHolder {
    private static final RocketMapper_Factory INSTANCE = new RocketMapper_Factory();
  }
}
