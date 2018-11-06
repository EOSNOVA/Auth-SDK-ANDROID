package io.eosnova.wallet.android.sdk.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.JsonObject
import io.eosnova.wallet.android.sdk.NovaAuth
import io.eosnova.wallet.android.sdk.OnNovaListener
import io.eosnova.wallet.android.sdk.model.NovaAction
import io.eosnova.wallet.android.sdk.model.NovaSignature
import io.eosnova.wallet.android.sdk.model.NovaStake
import io.eosnova.wallet.android.sdk.model.NovaTransfer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private val callBack = object : OnNovaListener {
        override fun callback(map: HashMap<String, String>) {
            text.text = ""
            for (key in map.keys) {
                text.append("$key: ${map[key]}\n")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        NovaAuth.test = true
        NovaAuth.testUrl = "http://dev.cryptolions.io:38888"

        // Account
        account.setOnClickListener {
            NovaAuth.requestAccount(this, callBack)
        }

        // Transfer
        transfer.setOnClickListener {
            val transfer = NovaTransfer("test2eosnova")
            transfer.contract = "eosio.token"
            transfer.to = "wzdworksnova"
            transfer.amount = 0.0001
            transfer.precision = 4
            transfer.symbol = "EOS"
            transfer.memo = "from EOSNOVA"
            NovaAuth.requestTransfer(this, transfer, callBack)
        }

        // Stake
        stake.setOnClickListener {
            val stake = NovaStake("test2eosnova")
            stake.action = NovaStake.Stake.STAKE
            stake.receiver = "test2eosnova"
            stake.cpu = 1.0
            stake.net = 1.0
            stake.transfer = false
            NovaAuth.requestStake(this, stake, callBack)
        }

        // UnStake
        unskake.setOnClickListener {
            val stake = NovaStake("test2eosnova")
            stake.action = NovaStake.Stake.UNSTAKE
            stake.receiver = "test2eosnova"
            stake.cpu = 1.0
            stake.net = 1.0
            NovaAuth.requestStake(this, stake, callBack)
        }


        // Transcation
        transcation.setOnClickListener {
            val transfer = NovaAction("test2eosnova")
            transfer.contract = "eosio.token"
            transfer.action = "transfer"
            transfer.args = JsonObject().apply {
                addProperty("from", "test2eosnova")
                addProperty("to", "wzdworksnova")
                addProperty("quantity", "1.0000 EOS")
                addProperty("memo", "test")
            }.toString()

            val delegatebw = NovaAction("test2eosnova")
            delegatebw.contract = "eosio"
            delegatebw.action = "delegatebw"
            delegatebw.args = JsonObject().apply {
                addProperty("from", "test2eosnova")
                addProperty("receiver", "test2eosnova")
                addProperty("stake_cpu_quantity", "1.0000 EOS")
                addProperty("stake_net_quantity", "1.0000 EOS")
                addProperty("transfer", false)
            }.toString()

            val list = ArrayList<NovaAction>()
            list.add(delegatebw)
            list.add(transfer)
            NovaAuth.requestTransaction(this, list, callBack)

        }

        // Signature
        signature.setOnClickListener {
            val signature = NovaSignature("test2eosnova")
            signature.data = JsonObject().apply {
                addProperty("wallet", "eosnova")
                addProperty("eos", "sample")
            }
            NovaAuth.requestSignature(this, signature, callBack)
        }

    }


}

