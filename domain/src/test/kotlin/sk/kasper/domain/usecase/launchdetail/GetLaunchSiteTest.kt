package sk.kasper.domain.usecase.launchdetail

import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.anyLong
import org.mockito.junit.MockitoJUnitRunner
import sk.kasper.domain.model.ErrorResponse
import sk.kasper.domain.model.LaunchSite
import sk.kasper.domain.model.Position
import sk.kasper.domain.model.SuccessResponse
import sk.kasper.domain.repository.LaunchSiteRepository

@RunWith(MockitoJUnitRunner::class)
class GetLaunchSiteTest {

    @Mock
    private lateinit var launchSiteRepository : LaunchSiteRepository

    private lateinit var getLaunchSite : GetLaunchSite

    @Before
    fun setUp() {
        getLaunchSite = GetLaunchSite(launchSiteRepository)
    }

    @Test
    fun getLaunchSite_error_errorResponse() = runBlocking {
        doAnswer { throw Exception("hej") }.`when`(launchSiteRepository).getLaunchSite(42)

        assertEquals(ErrorResponse("hej"), getLaunchSite.getLaunchSite(42))
    }

    @Test
    fun getLaunchSite_result_successResponse() = runBlocking {
        val launchSite = LaunchSite(Position(10.0, 20.0), "Name")
        whenever(launchSiteRepository.getLaunchSite(anyLong())).thenReturn(launchSite)

        assertEquals(SuccessResponse(launchSite), getLaunchSite.getLaunchSite(42))
    }

}