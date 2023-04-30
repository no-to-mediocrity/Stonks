package com.example.stonks.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.stonks.PortfolioFragment
import com.example.stonks.R
import com.example.stonks.databinding.PortfolioViewBinding
import com.example.stonks.model.Wallet
import com.squareup.picasso.Picasso


class ItemAdapter(
        private val context: PortfolioFragment,
        private val wallet: Wallet,
     //  private val listener: AdapterView.OnItemClickListener,
): RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

        private val assets = wallet.assets()
        class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
                val titleView: TextView = view.findViewById(R.id.stonk_name)
                val qtyView:TextView = view.findViewById(R.id.stonk_qty)
                val priceView:TextView = view.findViewById(R.id.stonk_price)
              //  val profitView:TextView = view.findViewById(R.id.stonk_profit)
                val imageView: ImageView = view.findViewById(R.id.stonk_image)
                val button:Button = view.findViewById(R.id.button)

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
                return ItemViewHolder(adapterLayout)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.button.setOnClickListener()
            holder.titleView.text = assets[position].ticker
            holder.qtyView.text = assets[position].qty.toString()
            holder.priceView.text =
                assets[position].purchasePrice.toString() + "$"
            if (assets[position].image != "") {
                Picasso.get().load(assets[position].image).fit()
                    .into(holder.imageView)
            }
        }

    fun viewStock() {
        println("this works!")
        findNavController().navigate(R.id.action_portfolioFragment_to_stockViewFragment)
       // Toast.makeText(this, "Its a toast!", Toast.LENGTH_SHORT).show()
        // findNavController().navigate(R.id.action_portfolioFragment_to_stockViewFragment)
    }
         //    holder.itemView.setOnClickListener {
           //      listener.onItemClick(null, holder.itemView, position, holder.itemId)
            // }
       // }
        override fun getItemCount() = wallet.assetCount()
}



