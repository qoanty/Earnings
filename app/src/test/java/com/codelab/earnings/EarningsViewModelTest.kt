
package com.codelab.earnings

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.NumberFormat

class EarningsViewModelTest {

    private val viewModel = EarningsViewModel()

    @Test
    fun calculate_with_earnings_calculation() {
        viewModel.onPrincipalChange("10000")
        viewModel.onDaysChange("30")
        viewModel.onRateChange("3.25")
        viewModel.calculate()
        val result = viewModel.uiState.value.result
        val expected = NumberFormat.getCurrencyInstance().format(10000 * 0.0325 * 30 / 365)
        assertEquals(expected, result)
    }

    @Test
    fun calculate_with_APR_calculation() {
        viewModel.onAprCalChange(true)
        viewModel.onPrincipalChange("10000")
        viewModel.onDaysChange("30")
        viewModel.onEarningsChange("26.71")
        viewModel.calculate()
        val result = viewModel.uiState.value.result
        val expected = NumberFormat.getNumberInstance().format(26.71 / 10000 * 365 / 30 * 100) + "%"
        assertEquals(expected, result)
    }

        @Test
    fun testDigitToChinese() {
        assertEquals("零元整", viewModel.digitToChinese(0.0))
        assertEquals("壹元整", viewModel.digitToChinese(1.0))
        assertEquals("壹拾元整", viewModel.digitToChinese(10.0))
        assertEquals("壹佰元整", viewModel.digitToChinese(100.0))
        assertEquals("壹仟元整", viewModel.digitToChinese(1000.0))
        assertEquals("壹万元整", viewModel.digitToChinese(10000.0))
        assertEquals("壹拾万元整", viewModel.digitToChinese(100000.0))
        assertEquals("壹佰万元整", viewModel.digitToChinese(1000000.0))
        assertEquals("壹仟万元整", viewModel.digitToChinese(10000000.0))
        assertEquals("壹亿元整", viewModel.digitToChinese(100000000.0))
        assertEquals("壹拾亿元整", viewModel.digitToChinese(1000000000.0))
        assertEquals("壹佰亿元整", viewModel.digitToChinese(10000000000.0))
        assertEquals("壹仟亿元整", viewModel.digitToChinese(100000000000.0))
        assertEquals("壹万亿元整", viewModel.digitToChinese(1000000000000.0))

        assertEquals("壹角", viewModel.digitToChinese(0.1))
        assertEquals("壹分", viewModel.digitToChinese(0.01))
        assertEquals("壹元壹角壹分", viewModel.digitToChinese(1.11))
        assertEquals("壹元零壹分", viewModel.digitToChinese(1.01))
        assertEquals("壹拾壹元壹角壹分", viewModel.digitToChinese(11.11))
        assertEquals("壹佰壹拾壹元壹角壹分", viewModel.digitToChinese(111.11))
        assertEquals("壹仟壹佰壹拾壹元壹角壹分", viewModel.digitToChinese(1111.11))
        assertEquals("壹万壹仟壹佰壹拾壹元壹角壹分", viewModel.digitToChinese(11111.11))
        assertEquals("壹拾壹万壹仟壹佰壹拾壹元壹角壹分", viewModel.digitToChinese(111111.11))
        assertEquals("壹佰壹拾壹万壹仟壹佰壹拾壹元壹角壹分", viewModel.digitToChinese(1111111.11))
        assertEquals("壹仟壹佰壹拾壹万壹仟壹佰壹拾壹元壹角壹分", viewModel.digitToChinese(11111111.11))
        assertEquals("壹亿壹仟壹佰壹拾壹万壹仟壹佰壹拾壹元壹角壹分", viewModel.digitToChinese(111111111.11))
        assertEquals("壹拾壹亿壹仟壹佰壹拾壹万壹仟壹佰壹拾壹元壹角壹分", viewModel.digitToChinese(1111111111.11))
        assertEquals("壹佰壹拾壹亿壹仟壹佰壹拾壹万壹仟壹佰壹拾壹元壹角壹分", viewModel.digitToChinese(11111111111.11))
        assertEquals("壹仟壹佰壹拾壹亿壹仟壹佰壹拾壹万壹仟壹佰壹拾壹元壹角壹分", viewModel.digitToChinese(111111111111.11))
        assertEquals("壹万壹仟壹佰壹拾壹亿壹仟壹佰壹拾壹万壹仟壹佰壹拾壹元壹角壹分", viewModel.digitToChinese(1111111111111.11))
    }
}
