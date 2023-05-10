package com.example.stonks.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.stonks.PortfolioFragment
import com.example.stonks.R
import com.example.stonks.databinding.PortfolioViewBinding
import com.example.stonks.model.Model
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation


class ItemAdapter(
    private val fragment: PortfolioFragment,
 //   private val wallet: Wallet,
    private val navController: NavController,
    private val model: Model
    //private val binding: PortfolioViewBinding
     //  private val listener: AdapterView.OnItemClickListener,
): RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private val assets = model.wallet.assets()

    class ItemViewHolder(
        view: View,
        navController: NavController,
        model: Model,
    ) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.stonk_name)
        val qtyView: TextView = view.findViewById(R.id.stonk_qty)
        val priceView: TextView = view.findViewById(R.id.stonk_price)
        val profitView:TextView = view.findViewById(R.id.stonk_roi)
        val profitPic: ImageView = view.findViewById(R.id.roi_image_stock)
        val imageView: ImageView = view.findViewById(R.id.stonk_image)

        init {
            itemView.setOnClickListener {
                var stock = model.wallet.assets()[adapterPosition]
                model.selectStock(stock.ticker,stock.isin,stock.name)
                if (model.wallet.assets()[adapterPosition].ticker != "USD") {
                 //    resources.getString(R.string.selectedStockName, model.selectedStockName.value)
                    navController.navigate(R.id.action_portfolioFragment_to_stockViewFragment)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.asset_card, parent, false)
        val binding =
            PortfolioViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ItemViewHolder(adapterLayout, navController, model)
    }
        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            if (assets[position].name != "") {
                holder.titleView.text = assets[position].name
            } else {
                holder.titleView.text = assets[position].ticker
            }
            if (assets[position].ticker != "USD") {
                //making a card for a stock
                holder.qtyView.text = assets[position].qty.toString()
                updateStockPrices(holder,position)
                if (assets[position].image != "") {
                    Picasso.get()
                        .load(assets[position].image)
                        .fit()
                        .transform(CropCircleTransformation())
                        .into(holder.imageView)
                    }
            } else {
                setBalanceCard(holder,position)
            }
        }

        @SuppressLint("SetTextI18n")
        fun setBalanceCard(holder: ItemViewHolder, position: Int) {
            holder.qtyView.text = ""
            holder.profitView.text = ""
            holder.priceView.text = String.format("%.2f", model.wallet.balance()) + "$"
            Picasso.get()
                .load(R.drawable.dollar_clipart_png)
                .fit()
                .transform(CropCircleTransformation())
                .into(holder.imageView)
            holder.profitPic.setVisibility(View.INVISIBLE)
        }

        @SuppressLint("SetTextI18n", "SuspiciousIndentation")
        fun updateStockPrices(holder: ItemViewHolder, position: Int) {
            if (model.portfolioEvaluated) {
                val price = model.portfolioPrices.getOrDefault(
                    assets[position].ticker,
                    0.0
                )
                holder.priceView.text =  String.format("%.2f", price * assets[position].qty) + "$"
                val ROI = ((price - assets[position].purchasePrice) / assets[position].purchasePrice) * 100
                holder.profitView.text = String.format("%.2f", ROI) + "%"
                    if (ROI > 0.0) {
                        holder.profitView.setTextColor(ContextCompat.getColor(fragment.requireContext(), R.color.profit_green))
                        holder.profitPic.setImageResource(R.drawable.baseline_arrow_upward_24)
                } else {
                        holder.profitView.setTextColor(ContextCompat.getColor(fragment.requireContext(), R.color.dark_red))
                    holder.profitPic.setImageResource(R.drawable.baseline_arrow_downward_24)
                    }
                }
                if (model.ROI != 0.0) {
                    holder.profitPic.setVisibility(View.VISIBLE)
                }
            }


        override fun getItemCount() = model.wallet.assetCount()
    }



