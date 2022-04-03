package sk.kasper.ui_timeline;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import sk.kasper.base.SettingsManager;
import sk.kasper.domain.usecase.GetTimelineItems;
import sk.kasper.domain.usecase.RefreshTimelineItems;
import sk.kasper.ui_common.mapper.RocketMapper;
import sk.kasper.ui_common.mapper.MapToDomainTag;
import sk.kasper.ui_common.mapper.MapToUiTag;

@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class TimelineViewModel_Factory implements Factory<TimelineViewModel> {
  private final Provider<GetTimelineItems> getTimelineItemsProvider;

  private final Provider<RefreshTimelineItems> refreshTimelineItemsProvider;

  private final Provider<SettingsManager> settingsManagerProvider;

  private final Provider<MapToDomainTag> mapToDomainTagProvider;

  private final Provider<MapToUiTag> mapToUiTagProvider;

  private final Provider<RocketMapper> rocketMapperProvider;

  public TimelineViewModel_Factory(Provider<GetTimelineItems> getTimelineItemsProvider,
      Provider<RefreshTimelineItems> refreshTimelineItemsProvider,
      Provider<SettingsManager> settingsManagerProvider,
      Provider<MapToDomainTag> mapToDomainTagProvider, Provider<MapToUiTag> mapToUiTagProvider,
      Provider<RocketMapper> rocketMapperProvider) {
    this.getTimelineItemsProvider = getTimelineItemsProvider;
    this.refreshTimelineItemsProvider = refreshTimelineItemsProvider;
    this.settingsManagerProvider = settingsManagerProvider;
    this.mapToDomainTagProvider = mapToDomainTagProvider;
    this.mapToUiTagProvider = mapToUiTagProvider;
    this.rocketMapperProvider = rocketMapperProvider;
  }

  @Override
  public TimelineViewModel get() {
    return newInstance(getTimelineItemsProvider.get(), refreshTimelineItemsProvider.get(), settingsManagerProvider.get(), mapToDomainTagProvider.get(), mapToUiTagProvider.get(), rocketMapperProvider.get());
  }

  public static TimelineViewModel_Factory create(
      Provider<GetTimelineItems> getTimelineItemsProvider,
      Provider<RefreshTimelineItems> refreshTimelineItemsProvider,
      Provider<SettingsManager> settingsManagerProvider,
      Provider<MapToDomainTag> mapToDomainTagProvider, Provider<MapToUiTag> mapToUiTagProvider,
      Provider<RocketMapper> rocketMapperProvider) {
    return new TimelineViewModel_Factory(getTimelineItemsProvider, refreshTimelineItemsProvider, settingsManagerProvider, mapToDomainTagProvider, mapToUiTagProvider, rocketMapperProvider);
  }

  public static TimelineViewModel newInstance(GetTimelineItems getTimelineItems,
      RefreshTimelineItems refreshTimelineItems, SettingsManager settingsManager,
      MapToDomainTag mapToDomainTag, MapToUiTag mapToUiTag, RocketMapper rocketMapper) {
    return new TimelineViewModel(getTimelineItems, refreshTimelineItems, settingsManager, mapToDomainTag, mapToUiTag, rocketMapper);
  }
}
