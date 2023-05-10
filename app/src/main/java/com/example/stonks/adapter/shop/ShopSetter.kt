package com.example.stonks.adapter.shop

import com.example.stonks.model.Stock

val names = listOf("1Life Healthcare", "AMD", "Affirm Holdings", "AGNC Investment REIT", "Alphabet Class C", "Alphabet Class A", "Amazon.com", "American Airlines Group", "Apple", "Baker Hughes", "Bed Bath", "Cisco", "Coinbase", "Comcast", "ContextLogic", "CSX Corp", "Disney", "DraftKings", "FuelCell Energy", "GILEAD", "Host Hotels & Resorts REIT", "Huntington Bancshares", "Inovio Pharmaceuticals", "Intel Corporation", "Lucid Group", "Luminar Technologies", "Marathon Digital Holdings", "Marvell Technology, Inc.", "McDonald’s", "Microchip Technology", "Micron Technology", "Microsoft Corporation", "Netflix", "Nikola", "NVIDIA", "ON Semiconductor Corp", "PayPal Holdings", "Plug Power Inc", "Qualcomm", "Qurate Retail Inc", "Riot Blockchain", "Rivian Automotive", "Sirius XM Holdings Inc", "SoFi Technologies", "Starbucks Corporation", "Tesla Motors", "Upstart Holdings", "Warner Bros. Discovery")
val tickers = listOf("ONEM", "AMD", "AFRM", "AGNC", "GOOG", "GOOGL", "AMZN", "AAL", "AAPL", "BKR", "BBBY", "CSCO", "COIN", "CMCSA", "WISH", "CSX", "DIS", "DKNG", "FCEL", "GILD", "HST", "HBAN", "INO", "INTC", "LCID", "LAZR", "MARA", "MRVL", "MCD", "MCHP", "MU", "MSFT", "NFLX", "NKLA", "NVDA", "ON", "PYPL", "PLUG", "QCOM", "QRTEA", "RIOT", "RIVN", "SIRI", "SOFI", "SBUX", "TSLA", "UPST", "WBD")
val isins = listOf("US68269G1076", "US0079031078", "US00827B1061", "US00123Q1040", "US02079K3059", "US02079K3059", "US0231351067", "US02376R1023", "US0378331005", "US05722G1004", "US0758961009", "US17275R1023", "US19260Q1076", "US20030N1019", "US21077C1071", "US1264081035", "US2546871060", "US26142R1041", "US35952H6018", "US3755581036", "US44107P1049", "US4461501045", "US45773H2013", "US4581401001", "US5494981039", "US5504241051", "US56585W4015", "US5738741041", "US5801351017", "US5950171042", "US5951121038", "US5949181045", "US64110L1061", "US6541101050", "US67066G1040", "US6821891057", "US70450Y1038", "US72919P2020", "US7475251036", "US74915M1009", "US7672921050", "US76954A1034", "US82968B1035", "US83406F1021", "US8552441094", "US88160R1014", "US91680M1071", "US9344231041")

fun shopSetter() : MutableList<Stock> {
    var stock: Stock?
    val stocks = mutableListOf<Stock>()
    for ((index, value) in names.withIndex()) {
        stock = Stock(tickers[index], isins[index], value)
        stock.setImage()
        stocks.add(stock)
    }
    return stocks
}