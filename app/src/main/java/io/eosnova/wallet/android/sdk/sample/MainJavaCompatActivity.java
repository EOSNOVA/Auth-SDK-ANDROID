package io.eosnova.wallet.android.sdk.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import io.eosnova.wallet.android.sdk.NovaAuthCompat;
import io.eosnova.wallet.android.sdk.OnNovaListener;
import io.eosnova.wallet.android.sdk.model.NovaAction;
import io.eosnova.wallet.android.sdk.model.NovaSignature;
import io.eosnova.wallet.android.sdk.model.NovaStake;
import io.eosnova.wallet.android.sdk.model.NovaTransfer;

public class MainJavaCompatActivity extends Activity {

    private TextView text;
    private OnNovaListener callback = new OnNovaListener() {
        @Override
        public void callback(@NotNull HashMap<String, String> map) {
            text.setText("");
            for (String key : map.keySet()) {
                text.append(key + ": " + map.get(key) + "\n");
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NovaAuthCompat.INSTANCE.unregister(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("NOVA");

        text = findViewById(R.id.text);

        NovaAuthCompat.INSTANCE.setTest(true);
        NovaAuthCompat.INSTANCE.register(this);

        // Account
        findViewById(R.id.account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NovaAuthCompat.INSTANCE.requestAccount(MainJavaCompatActivity.this, callback);
            }
        });

        // Transfer
        findViewById(R.id.transfer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NovaTransfer transfer = new NovaTransfer(
                        "shinhyo", "eosio.token", "emart",
                        0.0001, 4, "EOS", "from EOSNOVA"
                );
                NovaAuthCompat.INSTANCE.requestTransfer(MainJavaCompatActivity.this, transfer, callback);
            }
        });

        // Stake
        findViewById(R.id.stake).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NovaStake stake = new NovaStake("shinhyo", NovaStake.Stake.STAKE,
                        "shinhyo", 1.0, 1.0, false);
                NovaAuthCompat.INSTANCE.requestStake(MainJavaCompatActivity.this, stake, callback);
            }
        });

        // UnStake
        findViewById(R.id.unskake).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NovaStake stake = new NovaStake("shinhyo", NovaStake.Stake.UNSTAKE,
                        "shinhyo", 1.0, 1.0, false);
                NovaAuthCompat.INSTANCE.requestStake(MainJavaCompatActivity.this, stake, callback);
            }
        });

        // Transcation
        findViewById(R.id.transcation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObject transferArgs = new JsonObject();
                transferArgs.addProperty("from", "shinhyo");
                transferArgs.addProperty("to", "emart");
                transferArgs.addProperty("quantity", "1.0000 EOS");
                transferArgs.addProperty("memo", "test");

                NovaAction transfer = new NovaAction("shinhyo",
                        "eosio.token", "transfer", transferArgs.toString());


                JsonObject delegatebwArgs = new JsonObject();
                delegatebwArgs.addProperty("from", "shinhyo");
                delegatebwArgs.addProperty("receiver", "shinhyo");
                delegatebwArgs.addProperty("stake_cpu_quantity", "1.0000 EOS");
                delegatebwArgs.addProperty("stake_net_quantity", "1.0000 EOS");
                delegatebwArgs.addProperty("transfer", false);
                NovaAction delegatebw = new NovaAction("shinhyo",
                        "eosio", "delegatebw", delegatebwArgs.toString());


                ArrayList<NovaAction> list = new ArrayList<>();
                list.add(transfer);
                list.add(delegatebw);

                NovaAuthCompat.INSTANCE.requestTransaction(MainJavaCompatActivity.this, list, callback);
            }
        });

        // Signature
        findViewById(R.id.signature).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Gson
                JsonObject sigs = new JsonObject();
                sigs.addProperty("wallet", "eosnova");
                sigs.addProperty("eos", "sample");

                NovaSignature signature = new NovaSignature("shinhyo", sigs);
                NovaAuthCompat.INSTANCE.requestSignature(MainJavaCompatActivity.this, signature, callback);
            }
        });

    }


}
