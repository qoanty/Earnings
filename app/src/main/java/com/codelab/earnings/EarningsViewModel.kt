package com.codelab.earnings

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat

data class EarningsUiState(
    val principal: String = "",
    val days: String = "",
    val rate: String = "",
    val earnings: String = "",
    val aprCal: Boolean = false,
    val result: String = "0"
)

class EarningsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(EarningsUiState())
    val uiState: StateFlow<EarningsUiState> = _uiState.asStateFlow()

    fun onPrincipalChange(principal: String) {
        _uiState.update { it.copy(principal = principal) }
    }

    fun onDaysChange(days: String) {
        _uiState.update { it.copy(days = days) }
    }

    fun onRateChange(rate: String) {
        _uiState.update { it.copy(rate = rate) }
    }

    fun onEarningsChange(earnings: String) {
        _uiState.update { it.copy(earnings = earnings) }
    }

    fun onAprCalChange(aprCal: Boolean) {
        _uiState.update { it.copy(aprCal = aprCal, result = "0", earnings = "", rate = "") }
    }

    fun calculate() {
        val principal = uiState.value.principal.toDoubleOrNull() ?: 0.0
        val days = uiState.value.days.toIntOrNull() ?: 0
        val rate = uiState.value.rate.toDoubleOrNull() ?: 0.0
        val earnings = uiState.value.earnings.toDoubleOrNull() ?: 0.0

        val result = if (!uiState.value.aprCal) {
            calculateEarnings(principal, days, rate)
        } else {
            calculateAPR(principal, days, earnings)
        }
        _uiState.update { it.copy(result = result) }
    }

    fun clearInputs() {
        _uiState.update { EarningsUiState() }
    }

    private fun calculateEarnings(principal: Double, days: Int, rate: Double): String {
        val result = principal * (rate / 100) * days / 365
        return NumberFormat.getCurrencyInstance().format(result)
    }

    private fun calculateAPR(principal: Double, days: Int, earnings: Double): String {
        val result = earnings / principal * 365 / days * 100
        return NumberFormat.getNumberInstance().format(result) + "%"
    }

    @SuppressLint("DefaultLocale")
    fun digitToChinese(digit: Double): String {
        val s = String.format("%.2f", digit)
        val path = s.split('.')
        val integer = path[0]
        val decimal = path[1]

        if (integer == "0" && decimal == "00") {
            return "零元整"
        }

        val integerChinese = integerToChinese(integer)
        val decimalChinese = decimalToChinese(decimal)

        return when {
            decimal == "00" -> "${integerChinese}元整"
            integer == "0" -> decimalChinese
            decimal.startsWith("0") -> "${integerChinese}元零${decimalChinese}"
            else -> "${integerChinese}元${decimalChinese}"
        }
    }

    private fun integerToChinese(integer: String): String {
        if (integer.all { it == '0' }) return ""
        if (integer == "0") return ""
        var result = ""
        if (integer.length > 8) {
            val i = integer.substring(0, integer.length - 8)
            val j = integer.substring(integer.length - 8)
            result += integerToChinese(i) + "亿" + integerToChinese(j)
        } else if (integer.length > 4) {
            val i = integer.substring(0, integer.length - 4)
            val j = integer.substring(integer.length - 4)
            result += integerToChinese(i) + "万" + integerToChinese(j)
        } else {
            result += fourDigitToChinese(integer)
        }
        if (result.endsWith("零")) {
            result = result.substring(0, result.length - 1)
        }
        return result
    }

    private fun fourDigitToChinese(fourDigitStr: String): String {
        val num = arrayOf("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖")
        val unit = arrayOf("", "拾", "佰", "仟")
        var result = ""
        var zeroFlag = false
        for (i in fourDigitStr.indices) {
            val digit = Character.getNumericValue(fourDigitStr[i])
            val unitIndex = fourDigitStr.length - i - 1
            if (digit == 0) {
                zeroFlag = true
            } else {
                if (zeroFlag) {
                    result += "零"
                    zeroFlag = false
                }
                result += num[digit] + unit[unitIndex]
            }
        }
        return result
    }

    private fun decimalToChinese(decimalStr: String): String {
        val num = arrayOf("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖")
        val jiao = Character.getNumericValue(decimalStr[0])
        val fen = Character.getNumericValue(decimalStr[1])
        var result = ""
        if (jiao > 0) {
            result += num[jiao] + "角"
        }
        if (fen > 0) {
            result += num[fen] + "分"
        }
        return result
    }
}