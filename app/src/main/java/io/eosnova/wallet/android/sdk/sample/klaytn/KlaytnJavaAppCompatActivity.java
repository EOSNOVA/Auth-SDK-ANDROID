package io.eosnova.wallet.android.sdk.sample.klaytn;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.math.BigDecimal;

import io.eosnova.wallet.android.sdk.NovaKlaytnAuth;
import io.eosnova.wallet.android.sdk.model.klaytn.KlaytnTransfer;
import io.eosnova.wallet.android.sdk.sample.R;

public class KlaytnJavaAppCompatActivity extends AppCompatActivity {

    private String from = "0xea67ecf8f7fa613f16634b30cb95e4ba289ae40c";
    private String to = "0x6ce1698fd5c155a1ee12ffdf878e30fcf9ad5681";

    private NovaKlaytnAuth novaAuth;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klay);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        text = findViewById(R.id.text);

        // Init NovaWallet SDK.
        novaAuth = new NovaKlaytnAuth(this);
        novaAuth.setNovaListener(map -> {
            text.setText("");
            for (String key : map.keySet()) {
                text.append(key + ": " + map.get(key) + "\n");
            }
        });


        // Test Url Enable
        findViewById(R.id.test).setOnClickListener(view -> {
            novaAuth.requestTestMode("https://api.baobab.klaytn.net:8651");
        });

        // Account
        findViewById(R.id.account).setOnClickListener(view -> {
            novaAuth.requestAccount();
        });

        // ValueTransferTransaction
        findViewById(R.id.transfer).setOnClickListener(view -> {
            BigDecimal amount = new BigDecimal(0.123);
            String memo = "By Nova";    // or 'Empty'

            KlaytnTransfer transfer = KlaytnTransfer.createKlaytnTransfer(from, to, amount, memo);
            novaAuth.sendTransfer(transfer);
        });

        // SmartContractExcutationTransaction
        findViewById(R.id.transfer_tct).setOnClickListener(view -> {
            String contract = "0x0f446a6c8e96fa124c7ab2c95d99f42ccfa7efc8";
            int decimal = 18;
            String symbol = "ICN";
            BigDecimal amount = new BigDecimal(1.111);

            KlaytnTransfer transfer = KlaytnTransfer.createSmartContractTransfer(
                    from, to, contract, symbol, decimal, amount);
            novaAuth.sendTransfer(transfer);
        });

    }
}
