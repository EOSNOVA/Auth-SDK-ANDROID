package io.eosnova.wallet.android.sdk.sample.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.eosnova.wallet.android.sdk.sample.R
import io.eosnova.wallet.android.sdk.sample.eos.EosKotlinAppCompatActivity
import io.eosnova.wallet.android.sdk.sample.klaytn.KlaytnKotlinAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by shinhyo.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        klaytn.setOnClickListener { startActivity(Intent(this, KlaytnKotlinAppCompatActivity::class.java)) }
        eos.setOnClickListener { startActivity(Intent(this, EosKotlinAppCompatActivity::class.java)) }
    }
}