package sk.kasper.ui_common.tag

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import sk.kasper.ui_common.tag.FilterKtTest.TestTag.*

class FilterKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    enum class TestTag(override val title: String, override val color: Color = Color(0xFFFF5722)) :
        Tag {
        TAG_A("tag a"),
        TAG_B("tag b"),
        TAG_WITH_EXT("tag with extension tag"),
        EXT_TAG_X("extension tag x"),
        EXT_TAG_Y("extension tag y"),
    }

    private val filterDefinition = FilterDefinition(
        topTags = listOf(TAG_WITH_EXT, TAG_A, TAG_B),
        extensionTags = mapOf(
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