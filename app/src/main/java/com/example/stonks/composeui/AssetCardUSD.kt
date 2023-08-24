package com.example.stonks.composeui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.stonks.R
import com.example.stonks.model.Asset


@Composable
fun SetBalanceCard(asset: Asset, navController: NavHostController) {
    Card(shape = RoundedCornerShape(12.dp), backgroundColor = Color.White, modifier = Modifier
        .padding(vertical = 3.dp, horizontal =10.dp)
        .advancedShadow(
            color = Color.Black,
            alpha = 0.05f,
            cornersRadius = 12.dp,
            shadowBlurRadius = 20.dp,
            offsetY = 4.dp,
            offsetX = 4.dp
        )
    ) {
        Row(modifier = Modifier.padding(15.dp)) {
            Column(modifier = Modifier
                .padding(start = 8.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.us_flag),
                    contentDescription = "USD",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .aspectRatio(1f),

                    ) // added for round image, remove if not needed
            }
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    //     .fillMaxWidth()
                    //   .weight(1f)
                    .padding(start = 8.dp) // Adjust padding as needed
            ) {
                Text(text = asset.name, style = MaterialTheme.typography.subtitle1, fontWeight = FontWeight.Bold)
            }
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = String.format("%.2f$", asset.qty), style = MaterialTheme.typography.subtitle1)
                Row(verticalAlignment = Alignment.CenterVertically) {
                }
            }
        }

   }

}
