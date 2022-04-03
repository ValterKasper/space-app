package sk.kasper.domain.usecase

fun interface ShowLaunchNotification {
    suspend operator fun invoke(launchId: String)
}