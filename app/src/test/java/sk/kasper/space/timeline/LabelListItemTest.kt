package sk.kasper.space.timeline

import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LabelListItemTest {

    @Test
    fun comparision() {
        assertTrue(LabelListItem.Today == LabelListItem.Today)
        assertTrue(LabelListItem.Tomorrow == LabelListItem.Tomorrow)
        assertTrue(LabelListItem.Month(10) == LabelListItem.Month(10))
        assertTrue(LabelListItem.Month(11) != LabelListItem.Month(10))
    }
}