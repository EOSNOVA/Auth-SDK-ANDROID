package io.eosnova.wallet.android.sdk.sample.klaytn

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.eosnova.wallet.android.sdk.NovaKlaytnAuth
import io.eosnova.wallet.android.sdk.OnNovaListener
import io.eosnova.wallet.android.sdk.model.klaytn.KlaytnTransfer
import io.eosnova.wallet.android.sdk.sample.R
import kotlinx.android.synthetic.main.activity_eos.account
import kotlinx.android.synthetic.main.activity_eos.test
import kotlinx.android.synthetic.main.activity_eos.text
import kotlinx.android.synthetic.main.activity_eos.toolbar
import kotlinx.android.synthetic.main.activity_klay.*
import org.json.JSONObject


/**
 * Created by shinhyo.
 */
class KlaytnKotlinAppCompatActivity : AppCompatActivity() {

    private val from = "0x8136d50a8ab45585218addd1f6ec208702c52bae"
    private val to = "0xe863e09d1923c43edd54117b04a74fdaca75b800"

    private val novaAuth by lazy { NovaKlaytnAuth(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_klay)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        novaAuth.novaListener = object : OnNovaListener {
            override fun callback(map: MutableMap<String, String>) {
                try {
                    println("map $map")
                    text.text = ""
                    for (key in map.keys) {
                        val value = map[key] ?: ""
                        println("$key - $value")

                        if(key == "raw") {
                            try {
                                val jsonObj = JSONObject(value)
                                text.append("$key:\n${jsonObj.toString(4)}\n")
                                continue
                            } catch (e: Exception) {

                            }
                        }
                        text.append("$key: $value\n")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }


        // Test Url
        test.setOnClickListener {
            novaAuth.requestTestMode("https://api.baobab.klaytn.net:8651")
        }

        // Account
        account.setOnClickListener {
            novaAuth.requestAccount()
        }

        // ValueTransferTransaction
        transfer.setOnClickListener {
            val amount = 0.123.toBigDecimal()
            val memo = "novawallet" // or 'Empty'

            val transfer = KlaytnTransfer.createKlaytnTransfer(from, to, amount, memo)
            novaAuth.sendTransfer(transfer)
        }

        // SmartContractExecutionTransaction
        transfer_tct.setOnClickListener {
            val contract = "0x8FE8226946cB76073E3621A76B834fc5B7A37b78"
            val decimal = 18
            val symbol = "ICN"
            val amount = 1.111.toBigDecimal()

            val transfer = KlaytnTransfer.createSmartContractTransfer(
                    from, to, contract, symbol, decimal, amount)
            novaAuth.sendTransfer(transfer)
        }
    }

}



