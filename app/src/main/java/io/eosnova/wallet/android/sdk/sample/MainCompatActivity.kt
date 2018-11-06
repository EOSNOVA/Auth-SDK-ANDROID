package io.eosnova.wallet.android.sdk.sample

import android.app.Activity
import android.os.Bundle
import com.google.gson.JsonObject
import io.eosnova.wallet.android.sdk.NovaAuthCompat
import io.eosnova.wallet.android.sdk.OnNovaListener
import io.eosnova.wallet.android.sdk.model.NovaAction
import io.eosnova.wallet.android.sdk.model.NovaSignature
import io.eosnova.wallet.android.sdk.model.NovaStake
import io.eosnova.wallet.android.sdk.model.NovaTransfer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainCompatActivity : Activity() {

    private val callBack = object : OnNovaListener {
        override fun callback(map: HashMap<String, String>) {
            text.text = ""
            for (key in map.keys) {
                text.append("$key: ${map[key]}\n")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        NovaAuthCompat.unregister(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar.title = "NOVA"

        NovaAuthCompat.test = true
        NovaAuthCompat.testUrl = "http://dev.cryptolions.io:38888"
        NovaAuthCompat.register(this)

        // Account
        account.setOnClickListener {
            NovaAuthCompat.requestAccount(this, callBack)
        }

        // Transfer
        transfer.setOnClickListener {
            val transfer = NovaTransfer("shinhyo")
            transfer.contract = "eosio.token"
            transfer.to = "emart"
            transfer.amount = 0.0001
            transfer.precision = 4
            transfer.symbol = "EOS"
            transfer.memo = "from EOSNOVA"
            NovaAuthCompat.requestTransfer(this, transfer, callBack)
        }

        // Stake
        stake.setOnClickListener {
            val stake = NovaStake("shinhyo")
            stake.action = NovaStake.Stake.STAKE
            stake.receiver = "shinhyo"
            stake.cpu = 1.0
            stake.net = 1.0
            stake.transfer = false
            NovaAuthCompat.requestStake(this, stake, callBack)
        }

        // UnStake
        unskake.setOnClickListener {
            val stake = NovaStake("shinhyo")
            stake.action = NovaStake.Stake.UNSTAKE
            stake.receiver = "shinhyo"
            stake.cpu = 1.0
            stake.net = 1.0
            NovaAuthCompat.requestStake(this, stake, callBack)
        }


        // Transcation
        transcation.setOnClickListener {
            val transfer = NovaAction("shinhyo")
            transfer.contract = "eosio.token"
            transfer.action = "transfer"
            transfer.args = JsonObject().apply {
                addProperty("from", "shinhyo")
                addProperty("to", "emart")
                addProperty("quantity", "1.0000 EOS")
                addProperty("memo", "test")
            }.toString()

            val delegatebw = NovaAction("shinhyo")
            delegatebw.contract = "eosio"
            delegatebw.action = "delegatebw"
            delegatebw.args = JsonObject().apply {
                addProperty("from", "shinhyo")
                addProperty("receiver", "shinhyo")
                addProperty("stake_cpu_quantity", "1.0000 EOS")
                addProperty("stake_net_quantity", "1.0000 EOS")
                addProperty("transfer", false)
            }.toString()

            val list = ArrayList<NovaAction>()
            list.add(delegatebw)
            list.add(transfer)
            NovaAuthCompat.requestTransaction(this, list, callBack)

        }

        // Signature
        signature.setOnClickListener {
            val signature = NovaSignature("shinhyo")
            signature.data = JsonObject().apply {
                addProperty("wallet", "eosnova")
                addProperty("eos", "sample")
            }
            NovaAuthCompat.requestSignature(this, signature, callBack)
        }

    }


}

