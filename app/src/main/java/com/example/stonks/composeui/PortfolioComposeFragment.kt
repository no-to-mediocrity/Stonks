package com.example.stonks.composeui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavHostController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.example.stonks.R
import com.example.stonks.databinding.PortfolioComposeViewBinding
import com.example.stonks.model.Model
import com.example.stonks.model.StonksDatabase
import com.example.stonks.model.Asset
import com.example.stonks.theme.MyTheme
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers


class PortfolioComposeFragment : Fragment() {
    private val navController by lazy { findNavController(this) }
    private val model: Model by activityViewModels()

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<PortfolioComposeViewBinding>(
            inflater, R.layout.portfolio_compose_view, container, false
        )
            val db = context?.let { StonksDatabase.getInstance(it) }
            if (db != null) {
                model.loadState(db.assetDao())
            }

        var assets: MutableList<Asset> = mutableListOf()
        Observable.fromCallable {
            assets = model.allAssetsDirectly()
            val balanceAsset = assets.firstOrNull { it.ticker == "USD" }
            if (balanceAsset != null) {
                assets.remove(balanceAsset)
                assets.add(0, balanceAsset) // Add it back to the start of the list
                model.evalutatePortfolio()
            }
            Log.d("Observable", "Assets retrieved: ${assets.size}")
            assets // make the observable emit the assets list
        }
            .subscribeOn(Schedulers.computation()) // RxJava 3
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                binding.composeView.setContent {
                    MyTheme(false) {
                        PortfolioLayout(assets, navController as NavHostController, model)
                    }
                }
            }
            .doOnError { error ->
                Log.e("Observable", "Error: ${error.message}", error)
            }
            .subscribe()
            return binding.root


    }
}


