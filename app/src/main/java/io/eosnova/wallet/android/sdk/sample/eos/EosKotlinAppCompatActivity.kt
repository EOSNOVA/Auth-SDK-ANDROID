package io.eosnova.wallet.android.sdk.sample.eos

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.eosnova.wallet.android.sdk.NovaEosAuth
import io.eosnova.wallet.android.sdk.OnNovaListener
import io.eosnova.wallet.android.sdk.model.Action
import io.eosnova.wallet.android.sdk.model.eos.EosSignature
import io.eosnova.wallet.android.sdk.model.eos.EosStake
import io.eosnova.wallet.android.sdk.model.eos.EosTransfer
import io.eosnova.wallet.android.sdk.sample.R
import kotlinx.android.synthetic.main.activity_eos.*
import java.lang.Exception


/**
 * Created by shinhyo.
 */
class EosKotlinAppCompatActivity : AppCompatActivity() {

    private val gson by lazy { GsonBuilder().setPrettyPrinting().create() }

    private val novaAuth by lazy { NovaEosAuth(this) }

    private val from = "eosnovatest3"
    private val to = "novatesteos1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eos)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        novaAuth.novaListener = object : OnNovaListener {
            override fun callback(map: MutableMap<String, String>) {
                text.text = ""
                for (key in map.keys) {
                    val value = map[key] ?: ""
                    println("$key - $value")
                    if (key == "raw") {
                        try {
                            text.append("$key: ${gson.toJson(JsonParser().parse(value))}\n")
                        } catch (e: Exception){
                            text.append("$key: $value\n")
                        }
                    } else {
                        text.append("$key: $value\n")
                    }
                }
            }
        }

        // Test Url
        test.setOnClickListener { novaAuth.requestTestMode("http://jungle2.cryptolions.io") }

        // Account
        account.setOnClickListener { novaAuth.requestAccount() }

        // Transfer
        transfer.setOnClickListener {
            val transfer = EosTransfer(from, to, "eosio.token")
            transfer.amount = 0.0001.toBigDecimal()
            transfer.precision = 4
            transfer.symbol = "EOS"
            transfer.memo = "by nova"

            novaAuth.requestTransfer(transfer)
        }

        // Stake
        stake.setOnClickListener {
            val stake = EosStake(from, to)
            stake.action = EosStake.Stake.STAKE
            stake.cpu = 1.0.toBigDecimal()
            stake.net = 1.0.toBigDecimal()
            stake.transfer = false
            novaAuth.requestStake(stake)
        }

        // UnStake
        unskake.setOnClickListener {
            val stake = EosStake(from, to)
            stake.action = EosStake.Stake.UNSTAKE
            stake.cpu = 1.0.toBigDecimal()
            stake.net = 1.0.toBigDecimal()
            novaAuth.requestStake(stake)
        }


        // Transcation
        transcation.setOnClickListener {
            val transfer = Action(from)
            transfer.contract = "eosio.token"
            transfer.action = "transfer"
            transfer.args = JsonObject().apply {
                addProperty("from", from)
                addProperty("to", to)
                addProperty("quantity", "1.0000 EOS")
                addProperty("memo", "test")
            }.toString()

            val delegatebw = Action(from)
            delegatebw.contract = "eosio"
            delegatebw.action = "delegatebw"
            delegatebw.args = JsonObject().apply {
                addProperty("from", from)
                addProperty("receiver", from)
                addProperty("stake_cpu_quantity", "1.0000 EOS")
                addProperty("stake_net_quantity", "1.0000 EOS")
                addProperty("transfer", false)
            }.toString()

            val list = ArrayList<Action>()
            list.add(delegatebw)
            list.add(transfer)
            novaAuth.requestTransaction(list)

        }

        // Signature
        signature.setOnClickListener {
            val signature = EosSignature(from)
            signature.data = JsonObject().apply {
                addProperty("wallet", "eosnova")
                addProperty("eos", "sample")
            }
            novaAuth.requestSignature(signature)
        }

    }


}

