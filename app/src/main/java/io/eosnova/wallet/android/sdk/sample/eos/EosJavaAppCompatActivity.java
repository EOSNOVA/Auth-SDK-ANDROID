package io.eosnova.wallet.android.sdk.sample.eos;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.JsonObject;

import java.math.BigDecimal;
import java.util.ArrayList;

import io.eosnova.wallet.android.sdk.NovaEosAuth;
import io.eosnova.wallet.android.sdk.model.Action;
import io.eosnova.wallet.android.sdk.model.eos.EosSignature;
import io.eosnova.wallet.android.sdk.model.eos.EosStake;
import io.eosnova.wallet.android.sdk.model.eos.EosTransfer;
import io.eosnova.wallet.android.sdk.sample.R;

public class EosJavaAppCompatActivity extends AppCompatActivity {

    private TextView text;
    private NovaEosAuth novaAuth;

    private String from = "eosnovatest3";
    private String to = "novatesteos1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        novaAuth = new NovaEosAuth(this);

        text = findViewById(R.id.text);

        novaAuth.setNovaListener(map -> {
            text.setText("");
            for (String key : map.keySet()) {
                text.append(key + ": " + map.get(key) + "\n");
            }
        });

        // Account
        findViewById(R.id.account).setOnClickListener(view -> {
            novaAuth.requestAccount();
        });

        // Transfer
        findViewById(R.id.transfer).setOnClickListener(view -> {
            EosTransfer transfer = new EosTransfer(from, to, "eosio.token");
            transfer.setAmount(BigDecimal.valueOf(0.001));
            transfer.setPrecision(4);
            transfer.setSymbol("EOS");
            transfer.setMemo("by NOVA");
            novaAuth.requestTransfer(transfer);
        });

        // Stake
        findViewById(R.id.stake).setOnClickListener(view -> {
            EosStake stake = new EosStake(from, to);
            stake.setAction(EosStake.Stake.STAKE);
            stake.setCpu(BigDecimal.valueOf(1.0));
            stake.setNet(BigDecimal.valueOf(1.0));
            stake.setTransfer(false);
            novaAuth.requestStake(stake);
        });

        // UnStake
        findViewById(R.id.unskake).setOnClickListener(view -> {
            EosStake stake = new EosStake(from, to);
            stake.setAction(EosStake.Stake.UNSTAKE);
            stake.setCpu(BigDecimal.valueOf(1.0));
            stake.setNet(BigDecimal.valueOf(1.0));
            novaAuth.requestStake(stake);
        });

        // Transaction
        findViewById(R.id.transcation).setOnClickListener(view -> {
            JsonObject transferArgs = new JsonObject();
            transferArgs.addProperty("from", from);
            transferArgs.addProperty("to", to);
            transferArgs.addProperty("quantity", "1.0000 EOS");
            transferArgs.addProperty("memo", "test");

            Action transfer = new Action(from, "eosio.token", "transfer", transferArgs.toString());


            JsonObject delegatebwArgs = new JsonObject();
            delegatebwArgs.addProperty("from", from);
            delegatebwArgs.addProperty("receiver", from);
            delegatebwArgs.addProperty("stake_cpu_quantity", "1.0000 EOS");
            delegatebwArgs.addProperty("stake_net_quantity", "1.0000 EOS");
            delegatebwArgs.addProperty("transfer", false);
            Action delegatebw = new Action(from, "eosio", "delegatebw", delegatebwArgs.toString());


            ArrayList<Action> list = new ArrayList<>();
            list.add(transfer);
            list.add(delegatebw);

            novaAuth.requestTransaction(list);
        });

        // Signature
        findViewById(R.id.signature).setOnClickListener(view -> {

            // Gson
            JsonObject sigs = new JsonObject();
            sigs.addProperty("wallet", "eosnova");
            sigs.addProperty("eos", "sample");

            EosSignature signature = new EosSignature(from, sigs);
            novaAuth.requestSignature(signature);
        });

    }


}
