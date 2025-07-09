
package com.codelab.earnings

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test
import java.text.NumberFormat

class EarningsUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testCalculateEarnings() {
        composeTestRule.setContent {
            EarningsApp()
        }
        composeTestRule.onNodeWithStringId(R.string.principal).performTextInput("10000")
        composeTestRule.onNodeWithStringId(R.string.investment_days).performTextInput("30")
        composeTestRule.onNodeWithStringId(R.string.percentage_rate).performTextInput("3.25")
        composeTestRule.onNodeWithStringId(R.string.calculate).performClick()
        val expectedEarnings = NumberFormat.getCurrencyInstance().format(26.71)
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.cal_earnings, expectedEarnings)).assertExists()
    }

    @Test
    fun testCalculateAPR() {
        composeTestRule.setContent {
            EarningsApp()
        }
        composeTestRule.onNodeWithTagForStringId(R.string.switching).performClick()
        composeTestRule.onNodeWithStringId(R.string.principal).performTextInput("10000")
        composeTestRule.onNodeWithStringId(R.string.investment_days).performTextInput("30")
        composeTestRule.onNodeWithStringId(R.string.earnings).performTextInput("26.71")
        composeTestRule.onNodeWithStringId(R.string.calculate).performClick()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.cal_percentage_rate, "3.25%")).assertExists()
    }

    @Test
    fun testClearInputs() {
        composeTestRule.setContent {
            EarningsApp()
        }
        composeTestRule.onNodeWithStringId(R.string.principal).performTextInput("10000")
        composeTestRule.onNodeWithStringId(R.string.investment_days).performTextInput("30")
        composeTestRule.onNodeWithStringId(R.string.percentage_rate).performTextInput("3.5")
        composeTestRule.onNodeWithStringId(R.string.clear).performClick()
        composeTestRule.onNodeWithStringId(R.string.principal).assertExists()
    }
}

fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.onNodeWithStringId(
    @StringRes id: Int
): SemanticsNodeInteraction = onNodeWithText(activity.getString(id))

fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.onNodeWithTagForStringId(
    @StringRes id: Int
): SemanticsNodeInteraction = onNodeWithTag(activity.getString(id))