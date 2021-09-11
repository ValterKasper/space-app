package sk.kasper.ui_common.tag

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import sk.kasper.ui_common.R
import sk.kasper.ui_common.tag.FilterKtTest.TestTag.*

class FilterKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    enum class TestTag(
        override val label: Int,
        val title: String,
        override val color: Int = R.color.cyan_700
    ) :
        FilterItem {
        TAG_A(R.string.tag_iss, "ISS"),
        TAG_B(R.string.tag_mars, "Mars"),
        TAG_WITH_EXT(R.string.tag_manned, "Manned"),
        EXT_TAG_X(R.string.tag_secret, "Secret"),
        EXT_TAG_Y(R.string.tag_rover, "Rover"),
    }

    private val filterDefinition = FilterDefinition(
        topFilterItems = listOf(TAG_WITH_EXT, TAG_A, TAG_B),
        extensionFilterItems = mapOf(
            TAG_WITH_EXT to listOf(EXT_TAG_X, EXT_TAG_Y)
        )
    )

    @Test
    fun selectTag_thenClickClearButton_topLevelTagsShouldBeShown() {
        showFilter()

        composeTestRule
            .onNodeWithText(TAG_A.title)
            .performClick()
        composeTestRule
            .onNodeWithText(TAG_A.title)
            .assertIsOn()
        composeTestRule
            .onNodeWithText(TAG_B.title)
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithContentDescription("clear button")
            .performClick()

        composeTestRule
            .onNodeWithText(TAG_B.title)
            .assertExists()
    }

    @Test
    fun selectExtTag_thenUnselectExtTag_extTagShouldBeUnselected() {
        showFilter()

        composeTestRule
            .onNodeWithText(TAG_WITH_EXT.title)
            .performClick()
        composeTestRule
            .onNodeWithText(TAG_WITH_EXT.title)
            .assertIsOn()
        composeTestRule
            .onNodeWithText(EXT_TAG_X.title)
            .assertExists()
            .assertIsOff()
        composeTestRule
            .onNodeWithText(EXT_TAG_Y.title)
            .assertExists()
            .assertIsOff()

        composeTestRule
            .onNodeWithText(EXT_TAG_X.title)
            .performClick()
        composeTestRule
            .onNodeWithText(EXT_TAG_X.title)
            .assertIsOn()
        composeTestRule
            .onNodeWithText(EXT_TAG_Y.title)
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(EXT_TAG_X.title)
            .performClick()
        composeTestRule
            .onNodeWithText(EXT_TAG_X.title)
            .assertIsOff()
        composeTestRule
            .onNodeWithText(EXT_TAG_Y.title)
            .assertExists()
            .assertIsOff()
    }

    @Test
    fun selectTag_thenUnselectTag_topLevelTagsShouldBeShown() {
        showFilter()

        composeTestRule
            .onNodeWithText(TAG_A.title)
            .performClick()
        composeTestRule
            .onNodeWithText(TAG_A.title)
            .assertIsOn()

        composeTestRule
            .onNodeWithText(TAG_A.title)
            .performClick()
        composeTestRule
            .onNodeWithText(TAG_A.title)
            .assertIsOff()

        composeTestRule
            .onClearButton()
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithText(TAG_B.title)
            .assertExists()
    }

    @Test
    fun selectTag_thenClearAll_againSelectTag_shouldBeSelected() {
        showFilter()

        composeTestRule
            .onNodeWithText(TAG_A.title)
            .performClick()
        composeTestRule
            .onNodeWithText(TAG_A.title)
            .assertIsOn()

        composeTestRule
            .onClearButton()
            .performClick()
        composeTestRule
            .onClearButton()
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithText(TAG_A.title)
            .assertIsOff()

        composeTestRule
            .onNodeWithText(TAG_A.title)
            .performClick()
        composeTestRule
            .onNodeWithText(TAG_A.title)
            .assertIsOn()
    }

    fun SemanticsNodeInteractionsProvider.onClearButton(): SemanticsNodeInteraction {
        return onNodeWithContentDescription("clear button")
    }

    private fun showFilter() {
        composeTestRule.setContent {
            Filter(filterDefinition = filterDefinition)
        }
    }
}