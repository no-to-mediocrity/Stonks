package com.example.stonks.composeui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.stonks.model.Model

//import com.example.stonks.IndicatorROI

@SuppressLint("SuspiciousIndentation")
@Composable
fun PortfolioSummaryCard(model: Model, paddingValues: PaddingValues) {
        Card(shape = RoundedCornerShape(12.dp), modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 10.dp)
            .advancedShadow(
                color = Color.Black,
                alpha = 0.05f,
                cornersRadius = 12.dp,
                shadowBlurRadius = 20.dp,
                offsetY = 4.dp,
                offsetX = 4.dp
            )
        ) {
        Box(
            modifier = Modifier
               .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(6.dp)


            ) {
                Text(
                    text = "Portfolio value",
                    style = MaterialTheme.typography.h6,
                    color = Color.Black,
                )
                Row() {
                    Text(
                        text = String.format("%.2f$", model.portfolioValue),
                        color = Color.Black,
                        style = MaterialTheme.typography.h4,
                    )
                    if (model.portfolioEvaluated) {
                        IndicatorROI(model.ROI, sizeBig = true, percentROI = true)
                         }
                    }
                }
            }
        }
    }

