package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import android.content.Context
import android.icu.util.Calendar
import android.widget.DatePicker
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.shortDateFormatter
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
import java.time.LocalDate
import java.util.*


@Composable
fun DatePickerTextField(
    defaultDate: String,
    context: Context,
    onValueChange: (value:String)-> Unit,
    label: String,
) {

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    calendar.time = Date()

    var localDate by remember {
        mutableStateOf(LocalDate.now())
    }
    val dateString = remember {
        mutableStateOf("$day-$month-$year")
    }

    val showDateValue = remember {
        mutableStateOf(defaultDate)
    }

    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _: DatePicker, _year: Int, _month: Int, _dayOfMonth: Int ->
            val thisMonth = if (_month.plus(1)<10) "0${_month.plus(1)}" else _month.plus(1).toString()
            val thisDayOfMonth = if (_dayOfMonth<10) "0$_dayOfMonth" else _dayOfMonth.toString()
            dateString.value = "$thisDayOfMonth-$thisMonth-$_year"

            val selectedLocalDate = LocalDate.parse(dateString.value, shortDateFormatter)
            val selectedDayOfTheWeek = (selectedLocalDate.dayOfWeek).toString()
                .lowercase(Locale.getDefault())
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
            showDateValue.value = "$selectedDayOfTheWeek, ${dateString.value}"
            onValueChange(dateString.value)
            localDate = dateString.value.toLocalDate()
        }, localDate.year, localDate.monthValue.minus(1), localDate.dayOfMonth
    )

    var isFocused by remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = showDateValue.value,
        readOnly = true,
        shape = MaterialTheme.shapes.small,
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            errorIndicatorColor = MaterialTheme.colorScheme.onErrorContainer,
            errorContainerColor = MaterialTheme.colorScheme.errorContainer,
            cursorColor = MaterialTheme.colorScheme.onSurface,
            errorTextColor = MaterialTheme.colorScheme.onErrorContainer,
            focusedLabelColor = MaterialTheme.colorScheme.onSurface,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
            focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
        ),
        textStyle = MaterialTheme.typography.bodyLarge,
        onValueChange = {
            onValueChange(dateString.value)
        },
        label = {
            Text(
                text = label,
                textAlign = TextAlign.Start,
                color = if (isFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        },
        trailingIcon = {
            Icon(
                modifier = Modifier.clickable {
                    datePickerDialog.show()
                },
                painter = painterResource(id = R.drawable.ic_date),
                contentDescription = emptyString,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
                if (isFocused) datePickerDialog.show()
            }
    )
}


@Composable
fun MiniDatePickerTextField(
    defaultDate: String,
    context: Context,
    onValueChange: (value:String)-> Unit,
    label: String,
) {
    var dayOfWeek by remember {
        mutableStateOf(defaultDate.toLocalDate().dayOfWeek.toString().take(3).lowercase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() })
    }

    val calendar = Calendar.getInstance()

    calendar.time = Date()

    var localDate by remember {
        mutableStateOf(defaultDate.toLocalDate())
    }
    val dateString = remember {
        mutableStateOf(defaultDate)
    }
    val showDateValue = remember {
        mutableStateOf(defaultDate)
    }
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _: DatePicker, _year: Int, _month: Int, _dayOfMonth: Int ->
            val thisMonth = if (_month.plus(1)<10) "0${_month.plus(1)}" else _month.plus(1).toString()
            val thisDayOfMonth = if (_dayOfMonth<10) "0$_dayOfMonth" else _dayOfMonth.toString()
            dateString.value = "$thisDayOfMonth-$thisMonth-$_year"

            showDateValue.value = dateString.value
            onValueChange(dateString.value)
            localDate = dateString.value.toLocalDate()
            dayOfWeek = dateString.value.toLocalDate().dayOfWeek.toString().take(3)
                .lowercase().replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        }, localDate.year, localDate.monthValue.minus(1), localDate.dayOfMonth
    )

    var isFocused by remember {
        mutableStateOf(false)
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(LocalSpacing.current.topAppBarSize)
            .border(
                width = LocalSpacing.current.extraSmall,
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.onBackground
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier
            .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label.plus("($dayOfWeek)"),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal
                )
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                SimpleTextField(
                    modifier = Modifier
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                            if (isFocused) datePickerDialog.show()
                        },
                    value = showDateValue.value,
                    onValueChange = { onValueChange(dateString.value) },
                    placeholder = emptyString,
                    readOnly = true,
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    ),
                )
            }
        }

        Icon(
            modifier = Modifier
                .size(LocalSpacing.current.miniDatePicker)
                .clickable { datePickerDialog.show() },
            painter = painterResource(id = R.drawable.ic_date),
            contentDescription = emptyString,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(LocalSpacing.current.small))
    }
}

