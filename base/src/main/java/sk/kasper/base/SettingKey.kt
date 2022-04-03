package sk.kasper.base
enum class SettingKey(val key: String) {
    NIGHT_MODE("pref_night_mode"),
    SHOW_UNCONFIRMED_LAUNCHES("pref_show_unconfirmed_launches"),
    DURATION_BEFORE_NOTIFICATION_IS_SHOWN("pref_duration_before_notification_is_shown"),
    SHOW_LAUNCH_NOTIFICATION("pref_show_launch_notifications"),
    API_ENDPOINT("pref_api_endpoint"),
    LAUNCHES_FETCHED_ALREADY("pref_launches_fetched_already"),
    INVALID("pref_invalid")
}

