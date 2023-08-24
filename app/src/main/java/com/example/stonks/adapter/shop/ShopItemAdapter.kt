package com.example.stonks.adapter.shop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.stonks.R
import com.example.stonks.fragments.ShopFragment
import com.example.stonks.model.Model
import com.example.stonks.model.Asset
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class ShopItemAdapter(
    private val fragment: ShopFragment,
    private val model: Model,
    private val navController: NavController,
): RecyclerView.Adapter<ShopItemAdapter.ShopItemViewHolder>() {

    class ShopItemViewHolder(
        view: View,
        navController: NavController,
        model: Model,
    ) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.stonk_name)
        val imageView: ImageView = view.findViewById(R.id.stonk_image)
        val assets = model.shop

        init {
            itemView.setOnClickListener {
               var stock = assets[adapterPosition]
                model.selectAsset(Asset(stock.ticker, stock.isin,stock.name,0.0,0.0 ))
                navController.navigate(R.id.action_ShopFragment_to_stockViewFragment)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.shop_asset_card, parent, false)
        return ShopItemViewHolder(adapterLayout, navController, model)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
            holder.titleView.text = model.shop[position].name
            if (model.shop[position].image != "") {
                Picasso.get()
                    .load(model.shop[position].image)
                    .fit()
                    .transform(CropCircleTransformation())
                    .into(holder.imageView)
            }
    }

    override fun getItemCount() = model.shop.size
}
