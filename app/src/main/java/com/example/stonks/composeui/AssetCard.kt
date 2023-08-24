package com.example.stonks.composeui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.stonks.R
import com.example.stonks.model.Asset
import com.example.stonks.model.Model
import com.example.stonks.model.calculateROI
import com.example.stonks.theme.profitGreen
import com.example.stonks.tinkoffImageSize
import com.example.stonks.tinkoffImageSource


@Composable
fun AssetCard(asset: Asset, model: Model, navController: NavHostController) {
    if (asset.ticker == "USD") {
        SetBalanceCard(asset = asset,navController)
    } else {
        SetAssetCard(asset = asset, model, navController)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SetAssetCard(asset: Asset, model: Model, navController: NavHostController) {
    Card(shape = RoundedCornerShape(12.dp), backgroundColor = Color.White, onClick = {
        model.selectAsset(asset)
        navController.navigate(R.id.action_portfolioComposeFragment_to_stockViewFragment)
    }, modifier = Modifier
        .padding(vertical = 5.dp, horizontal = 10.dp)
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
                    painter = rememberAsyncImagePainter(tinkoffImageSource +asset.isin+ tinkoffImageSize),
                    contentDescription = "Company logo",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .aspectRatio(1f),

                    )
            }
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .padding(start = 8.dp)
            ) {
                Text(text = asset.name, style = MaterialTheme.typography.subtitle1, fontWeight = FontWeight.Bold)
                Card(shape = RoundedCornerShape(14.dp),backgroundColor = Color(0xFFE2E3EB), modifier = Modifier
                    .padding(2.dp))
                {
                    Text(text = "${asset.qty.toInt()}", style = MaterialTheme.typography.subtitle1, modifier = Modifier.padding(horizontal = 4.dp))
                }
            }
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.fillMaxWidth(),

            ) {
                    val assetPrice = model.returnPortfolioPrices()[asset.ticker]?.times(asset.qty)
                    Text(
                        String.format("%.2f$", assetPrice),
                        style = MaterialTheme.typography.subtitle1
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        var textColor = Color.White
                        var arrowRotation = 0F
                        var assetROI = model.returnPortfolioPrices()[asset.ticker]?.times(asset.qty)
                            ?.let { calculateROI(asset.qty * asset.purchasePrice, it) }
                        if (assetROI != null) {
                            IndicatorROI(assetROI, sizeBig = false, percentROI = true)
                            if (assetROI >= 0.0) {
                                textColor = profitGreen
                                arrowRotation = 270F
                            } else {
                                textColor = Color.Red
                                arrowRotation = 90F
                            }
                        }
                    }
            }
        }
   }

}



