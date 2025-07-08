
package com.codelab.earnings

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test

class EarningsUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testCalculateEarnings() {
        composeTestRule.setContent {
            EarningsApp()
        }
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        composeTestRule.onNodeWithText(context.getString(R.string.principal)).performTextInput("10000")
        composeTestRule.onNodeWithText(context.getString(R.string.investment_days)).performTextInput("30")
        composeTestRule.onNodeWithText(context.getString(R.string.percentage_rate)).performTextInput("3.25")

        composeTestRule.onNodeWithText(context.getString(R.string.calculate)).performClick()

        composeTestRule.onNodeWithText(context.getString(R.string.cal_earnings, "$26.71")).assertExists()
    }

    @Test
    fun testCalculateAPR() {
        composeTestRule.setContent {
            EarningsApp()
        }
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        composeTestRule.onNodeWithTag("mySwitch").performClick()

        composeTestRule.onNodeWithText(context.getString(R.string.principal)).performTextInput("10000")
        composeTestRule.onNodeWithText(context.getString(R.string.investment_days)).performTextInput("30")
        composeTestRule.onNodeWithText(context.getString(R.string.earnings)).performTextInput("26.71")

        composeTestRule.onNodeWithText(context.getString(R.string.calculate)).performClick()

        composeTestRule.onNodeWithText(context.getString(R.string.cal_percentage_rate, "3.25%")).assertExists()
    }

    @Test
    fun testClearInputs() {
        composeTestRule.setContent {
            EarningsApp()
        }
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        composeTestRule.onNodeWithText(context.getString(R.string.principal)).performTextInput("10000")
        composeTestRule.onNodeWithText(context.getString(R.string.investment_days)).performTextInput("30")
        composeTestRule.onNodeWithText(context.getString(R.string.percentage_rate)).performTextInput("3.5")

        composeTestRule.onNodeWithText(context.getString(R.string.clear)).performClick()

        composeTestRule.onNodeWithText(context.getString(R.string.principal)).assertExists()
    }
}
