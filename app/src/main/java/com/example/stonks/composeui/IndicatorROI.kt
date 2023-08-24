package com.example.stonks.composeui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.stonks.theme.profitGreen

@SuppressLint("SuspiciousIndentation")

@Composable
fun IndicatorROI(ROI: Double, sizeBig:Boolean, percentROI:Boolean) {
    val cardColor = if (ROI >= 0.0) profitGreen else Color.Red
    val arrowRotation = if (ROI >= 0.0) 270F else 90F
    val cardPadding  = if (sizeBig) 14.dp else 2.dp
    val textStyle = if (sizeBig) MaterialTheme.typography.h6 else  MaterialTheme.typography.subtitle2
    val textStartPadding = if (sizeBig) 25.dp else 18.dp
    val textEndPadding = if (sizeBig) 6.dp else 6.dp
    val iconSize= if (sizeBig) 26.dp else 20.dp
  //  val symbol = if (percentROI) "%" else "$" // TODO make both $ and percent ROI
    val symbol = "%"
        Card(
        shape = RoundedCornerShape(14.dp),
        backgroundColor = cardColor,
        modifier = Modifier
            .padding(cardPadding)
    )
    {
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Return on investment symbol",
            tint = Color.White,
            modifier = Modifier
                .padding(start = 0.dp)
                .size(iconSize)
                .rotate(arrowRotation)
        )
        Box(
         //   modifier = Modifier
           //     .padding(start = 0.dp)
        )
        {
            Text(
                text = String.format("%.2f", ROI) + symbol,
                style = textStyle,
                color = Color.White,
                modifier = Modifier.padding(
                    start = textStartPadding,
                    end = textEndPadding
                )
            )

        }
    }
}
