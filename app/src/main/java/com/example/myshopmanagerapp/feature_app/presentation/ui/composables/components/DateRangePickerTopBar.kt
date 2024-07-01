package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.EndDate
import com.example.myshopmanagerapp.core.FormRelatedString.StartDate
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerTopBar(
    startDate: Long,
    endDate: Long,
    goBack: () -> Unit = {},
    getDateRange: (String, String) -> Unit,
) {
    val context = LocalContext.current

    var startDayOfWeek by remember {
        mutableStateOf(startDate.toLocalDate().dayOfWeek.toString().lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() })
    }
    var startDateString by remember {
        mutableStateOf(startDate.toDateString())
    }
    var endDayOfWeek by remember {
        mutableStateOf(endDate.toLocalDate().dayOfWeek.toString().lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() })
    }
    var endDateString by remember {
        mutableStateOf(endDate.toDateString())
    }
    TopAppBar (
        title = {
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier
                    .weight(1f)
                    .padding(LocalSpacing.current.extraSmall)) {
                    MiniDatePickerTextField(
                        defaultDate = startDateString,
                        context = context,
                        onValueChange = { _dateString ->
                            startDateString = _dateString
                            startDayOfWeek = _dateString.toLocalDate().dayOfWeek.toString().lowercase()
                                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT)
                                else it.toString() }
                        },
                        label = StartDate
                    )
                }
                Box(modifier = Modifier
                    .weight(1f)
                    .padding(LocalSpacing.current.extraSmall)
                ) {
                    MiniDatePickerTextField(
                        defaultDate = endDateString,
                        context = context,
                        onValueChange = { _dateString ->
                            endDateString = _dateString
                            endDayOfWeek = _dateString.toLocalDate().dayOfWeek.toString().lowercase()
                                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT)
                                else it.toString() }
                        },
                        label = EndDate
                    )
                }
                Box(modifier = Modifier
                    .padding(start = LocalSpacing.current.extraSmall)){
                    Icon(
                        modifier = Modifier
                            .width(LocalSpacing.current.dropDownItem)
                            .clickable { getDateRange(startDateString, endDateString) },
                        painter = painterResource(id = R.drawable.ic_send),
                        contentDescription = emptyString,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        },
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .width(LocalSpacing.current.topBarIcon)
                    .clickable { goBack() },
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = emptyString,
                tint = MaterialTheme.colorScheme.onBackground
            )
         },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground
        ),
    )
}
