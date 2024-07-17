package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun InfoDisplayCard(
    icon: Int = R.drawable.ic_inventory_item,
    imageWidth: Dp = 40.dp,
    currency: String = emptyString,
    currencySize: TextUnit = 32.sp,
    currencyColor: Color = MaterialTheme.colorScheme.onBackground,
    bigText: String,
    bigTextSize: TextUnit,
    bigTextFontWeight: FontWeight = FontWeight.ExtraBold,
    bigTextColor: Color = MaterialTheme.colorScheme.onBackground,
    smallText: String,
    smallTextSize: TextUnit,
    smallTextFontWeight: FontWeight = FontWeight.SemiBold,
    smallTextColor: Color = MaterialTheme.colorScheme.onBackground,
    shape: Shape = MaterialTheme.shapes.large,
    elevation: Dp = LocalSpacing.current.noElevation,
    backgroundColor: Color = Color.Transparent,
    isAmount: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = shape,
        elevation = elevation,
        backgroundColor = backgroundColor
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(LocalSpacing.current.small),
                contentAlignment = Alignment.BottomCenter
            ) {
                if (isAmount){
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = currency,
                        textAlign = TextAlign.Center,
                        fontSize = currencySize,
                        fontWeight = FontWeight.ExtraBold,
                        overflow = TextOverflow.Ellipsis,
                        color = currencyColor
                    )
                }
                else {
                    Image(
                        modifier = Modifier
                            .size(imageWidth)
                            .aspectRatio(1f),
                        painter = painterResource(id = icon),
                        contentDescription = emptyString
                    )
                }
            }


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(LocalSpacing.current.small),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = bigText,
                    textAlign = TextAlign.Center,
                    fontSize = bigTextSize,
                    fontWeight = bigTextFontWeight,
                    overflow = TextOverflow.Ellipsis,
                    color = bigTextColor
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = smallText,
                    textAlign = TextAlign.Center,
                    fontSize = smallTextSize,
                    fontWeight = smallTextFontWeight,
                    overflow = TextOverflow.Ellipsis,
                    color = smallTextColor
                )
            }

        }
    }
}

@Composable
fun HorizontalInfoDisplayCard(
    modifier: Modifier = Modifier,
    icon: Int = R.drawable.ic_inventory_item,
    imageWidth: Dp = 40.dp,
    name: String,
    nameTextSize: TextUnit,
    nameTextColor: Color = MaterialTheme.colorScheme.onBackground,
    valueText: String,
    valueTextSize: TextUnit,
    valueTextColor: Color = MaterialTheme.colorScheme.onBackground,
    shape: Shape = MaterialTheme.shapes.large,
    elevation: Dp = LocalSpacing.current.noElevation,
    backgroundColor: Color = Color.Transparent,
) {
    Card(
        modifier = modifier.fillMaxSize(),
        shape = shape,
        elevation = elevation,
        backgroundColor = backgroundColor
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(imageWidth)
                    .padding(LocalSpacing.current.small),
                contentAlignment = Alignment.BottomCenter
            ) {
                Image(
                    modifier = Modifier
                        .size(imageWidth)
                        .aspectRatio(1f),
                    painter = painterResource(id = icon),
                    contentDescription = emptyString
                )
            }


            Box(
                modifier = Modifier
                    .weight(name.length.plus(1).toFloat())
                    .padding(LocalSpacing.current.small),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = name,
                    fontSize = nameTextSize,
                    fontWeight = FontWeight.ExtraBold,
                    overflow = TextOverflow.Ellipsis,
                    color = nameTextColor
                )
            }

            Box(
                modifier = Modifier
                    .weight(valueText.length.plus(1).toFloat()),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = valueText,
                    textAlign = TextAlign.End,
                    fontSize = valueTextSize,
                    fontWeight = FontWeight.ExtraBold,
                    overflow = TextOverflow.Ellipsis,
                    color = valueTextColor
                )
            }

        }
    }
}
