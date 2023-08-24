package com.example.stonks.composeui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.stonks.R
import com.example.stonks.model.Asset
import com.example.stonks.model.Model


@Composable
fun PortfolioLayout(
    assets: MutableList<Asset>,
    navController: NavHostController,
    model : ViewModel
) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {navController.navigate(R.id.action_portfolioComposeFragment_to_shopFragment)},
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(
                    imageVector =  Icons.Default.ShoppingCart,
                    contentDescription = "Shop button",
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->
        LazyColumn () {
            item{
                PortfolioSummaryCard(model as Model, innerPadding)
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .wrapContentSize()
                        .animateContentSize()
                ) {
                    Text(
                        text = "My assets",
                        style = MaterialTheme.typography.body1,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Left
                    )
                }
            }
            items(assets.size) {
                index ->
                AssetCard(asset = assets[index], model = model as Model, navController = navController)
            }
        }
    }
}

