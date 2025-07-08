package com.codelab.earnings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codelab.earnings.ui.theme.EarningsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EarningsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    EarningsApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun EarningsApp(
    modifier: Modifier = Modifier,
    viewModel: EarningsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(horizontal = 40.dp)
            .statusBarsPadding()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = if (!uiState.aprCal) stringResource(id = R.string.calculate_earnings)
            else stringResource(id = R.string.calculate_apr),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )
        EditNumberField(
            label = R.string.principal,
            leadingIcon = R.drawable.currency_yuan,
            value = uiState.principal,
            onValueChange = { viewModel.onPrincipalChange(it) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
        )
        if (uiState.principal.isNotEmpty()) {
            Text(
                text = stringResource(
                    id = R.string.capital_amount,
                    viewModel.digitToChinese(uiState.principal.toDoubleOrNull() ?: 0.0)
                ),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
            )
        }
        EditNumberField(
            label = R.string.investment_days,
            leadingIcon = R.drawable.date_range,
            value = uiState.days,
            onValueChange = { viewModel.onDaysChange(it) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
        )
        if (!uiState.aprCal) {
            EditNumberField(
                label = R.string.percentage_rate,
                leadingIcon = R.drawable.percent,
                value = uiState.rate,
                onValueChange = { viewModel.onRateChange(it) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .fillMaxWidth(),
            )
        } else {
            EditNumberField(
                label = R.string.earnings,
                leadingIcon = R.drawable.currency_yuan,
                value = uiState.earnings,
                onValueChange = { viewModel.onEarningsChange(it) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .fillMaxWidth(),
            )
        }
        APRCalRow(
            aprCal = uiState.aprCal,
            onAPRCalChanged = { viewModel.onAprCalChange(it) },
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Row(
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = { viewModel.calculate() },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.calculate),
                    fontSize = 16.sp
                )
            }
            OutlinedButton(
                onClick = {
                    viewModel.clearInputs()
                    focusManager.clearFocus()
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.clear),
                    fontSize = 16.sp
                )
            }
        }
        Text(
            text = if (!uiState.aprCal) stringResource(id = R.string.cal_earnings, uiState.result)
            else stringResource(id = R.string.cal_percentage_rate, uiState.result),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Composable
fun EditNumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(label)) },
        leadingIcon = {
            Icon(
                painter = painterResource(id = leadingIcon),
                contentDescription = null
            )
        },
        keyboardOptions = keyboardOptions,
        singleLine = true,
        shape = MaterialTheme.shapes.large,
        modifier = modifier
    )
}

@Composable
fun APRCalRow(
    aprCal: Boolean,
    onAPRCalChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .size(24.dp)
    ) {
        Text(
            text = stringResource(id = R.string.apr),
            style = MaterialTheme.typography.bodyLarge,
        )
        Switch(
            checked = aprCal,
            onCheckedChange = onAPRCalChanged,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
                .testTag("mySwitch")
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EarningsAppPreview() {
    EarningsTheme {
        EarningsApp()
    }
}