![](images/app2app.jpg)

Nova Wallet
=====
[Download App][1]
[YouTube][5]
For more information please see [the website][2].

-----

Quick Start
=====

### Gradle
```groovy
    dependencies {
        // Nova Wallet SDK
        implementation 'io.eosnova:auth-sdk:2.0.0-beta'
        
        // Gson JsonParser
        implementation 'com.google.code.gson:gson:2.8.6'
    }
```

### NovaWallet Version Check
How to check the minimum NovaWallet version supported by the SDK.
```java
    Utils.checkMinNovaWalletVersion(context);
```
- SDK Version: 2.0.0-beta - Min NovaWallet VersionCode: 136 (2.2.4)

### Use Activity (Optional)

If you use `Activity` instead of `AppCompatActivity`, `register` and `unregister` are required.

```java

    private NovaEosAuth novaAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        ...
        novaAuth = new NovaEosAuth(this);
        novaAuth.register();
        ...
    }

    @Override
    override fun onDestroy() {
        super.onDestroy()
        ...
        novaAuth.unregister();
        ...
    }
```

### Testnet
_※ Once “TestNet” mode is activated, TestNet mode will remain activated until NOVA Wallet is uninstalled._

##### EOS
```java
    NovaEosAuth novaAuth = new NovaEosAuth(this);               // Init NovaWallet SDK
    novaAuth.requestTestMode("http://jungle2.cryptolions.io")   // Set TestNet
```

##### Klaytn
```java
    NovaKlaytnAuth novaAuth = new NovaKlaytnAuth(this);             // Init NovaWallet SDK
    novaAuth.requestTestMode("https://api.baobab.klaytn.net:8651"); // Set TestNet
```

-----

How to use
=====
Support Java and Kotlin.
please see example.
- EOS: 
    - [EosJavaActivity][11]
    - [EosJavaAppCompatActivity][12]
- Klaytn :
    - [KlaytnJavaActivity][13]
    - [KlaytnJavaAppCompatActivity][14]



### 1. EOS

##### 1.1 Example
```java
...

    // Init NovaWallet SDK
    NovaEosAuth novaAuth = new NovaEosAuth(this);

    // Set TestNet
    novaAuth.requestTestMode("http://jungle2.cryptolions.io")

    // Response listener
    novaAuth.setNovaListener(map -> {
                // map: NovaWallet SDK Response
            });
    
    // Account
    novaAuth.requestAccount();
    
    // Transfer
    EosTransfer transfer = new EosTransfer(from, to, "eosio.token");
    transfer.setAmount(BigDecimal.valueOf(0.001));
    transfer.setPrecision(4);
    transfer.setSymbol("EOS");
    transfer.setMemo("by NOVA");
    novaAuth.requestTransfer(transfer);
    
...

```

##### 1.2 Read Account Info
```java
    novaAuth.requestAccount();
```
##### 1.3 Transfer token
```java
    EosTransfer transfer = new EosTransfer("eosnovatest3", "novatesteos1", "eosio.token");
    transfer.setAmount(BigDecimal.valueOf(0.001));
    transfer.setPrecision(4);
    transfer.setSymbol("EOS");
    transfer.setMemo("by NOVA");
    novaAuth.requestTransfer(transfer);
```
##### 1.4 Stake / Unstake
```java
    EosStake stake = new EosStake("eosnovatest3", "novatesteos1");
    stake.setAction(EosStake.Stake.STAKE);  // Unstake : EosStake.Stake.UNSTAKE
    stake.setCpu(BigDecimal.valueOf(1.0));
    stake.setNet(BigDecimal.valueOf(1.0));
    stake.setTransfer(false);
    novaAuth.requestStake(stake);
```

##### 1.5 Signature
```java
    JsonObject sigs = new JsonObject();
    sigs.addProperty("wallet", "eosnova");
    sigs.addProperty("eos", "sample");
    
    EosSignature signature = new EosSignature("eosnovatest3", sigs);
    novaAuth.requestSignature(signature);
    
```

##### 1.6 Transaction
```java
    JsonObject transferArgs = new JsonObject();
    transferArgs.addProperty("from", "eosnovatest3");
    transferArgs.addProperty("to", "novatesteos1");
    transferArgs.addProperty("quantity", "1.0000 EOS");
    transferArgs.addProperty("memo", "test");
    
    Action transfer = new Action(from, "eosio.token", "transfer", transferArgs.toString());
    
    
    JsonObject delegatebwArgs = new JsonObject();
    delegatebwArgs.addProperty("from", "eosnovatest3");
    delegatebwArgs.addProperty("receiver", "novatesteos1");
    delegatebwArgs.addProperty("stake_cpu_quantity", "1.0000 EOS");
    delegatebwArgs.addProperty("stake_net_quantity", "1.0000 EOS");
    delegatebwArgs.addProperty("transfer", false);
    Action delegatebw = new Action(from, "eosio", "delegatebw", delegatebwArgs.toString());
    
    
    ArrayList<Action> list = new ArrayList<>();
    list.add(transfer);
    list.add(delegatebw);
    
    novaAuth.requestTransaction(list);
```




### 2. Klaytn

##### 2.1 Example
```java
...

    // Init NovaWallet SDK.
    NovaKlaytnAuth novaAuth = new NovaKlaytnAuth(this);

    // Set TestNet
    novaAuth.requestTestMode("https://api.baobab.klaytn.net:8651");
    
    // Response listener
    novaAuth.setNovaListener(map -> {
                    // map: NovaWallet SDK Response
                });

    // Account
    novaAuth.requestAccount();
    
    // Transfer
    KlaytnTransfer transfer = new KlaytnTransfer(from, to);
    transfer.setAmount(new BigDecimal(0.123));
    transfer.setMemo("By Nova");
    novaAuth.sendTransfer(transfer);
    
...
```

##### 2.2 Account
```java
    novaAuth.requestAccount();
```

##### 2.3 Value Transfer Transaction
```java
    String from = "0xea67ecf8f7fa613f16634b30cb95e4ba289ae40c";
    String to = "0x6ce1698fd5c155a1ee12ffdf878e30fcf9ad5681";
    BigDecimal amount = new BigDecimal(0.123);
    String memo = "By Nova";
    
    KlaytnTransfer transfer = KlaytnTransfer.createKlaytnTransfer(from, to, amount, memo);
    novaAuth.sendTransfer(transfer);
```

##### 2.4 SmartContract Excutation Transaction
```java
    String from = "0xea67ecf8f7fa613f16634b30cb95e4ba289ae40c";
    String to = "0x6ce1698fd5c155a1ee12ffdf878e30fcf9ad5681";
    String contract = "0x0f446a6c8e96fa124c7ab2c95d99f42ccfa7efc8";
    int decimal = 18;
    String symbol = "ICN";
    BigDecimal amount = new BigDecimal(1.111);
    
    KlaytnTransfer transfer = KlaytnTransfer.createSmartContractTransfer(
                        from, to, contract, symbol, decimal, amount);
    novaAuth.sendTransfer(transfer);
```





### 3. Common

##### 3.1 Callback
```java
    novaAuth.setNovaListener(map -> {
        String code = map.get("code");
        String msg = map.get("msg");  // simple response message
        String raw = map.get("raw");  // success or fail response message(JSON format) from network or sdk
    });
    
```


##### 3.2 Response Code
```
    0 : success

    100: User pressed 'cancel' button
    101: Parameter of your request is wrong

    200: Account list empty in nova.
    201: Can't find account in nova.
    202: Can't find account in chain.
    203: Unknown chain type.

    300: Fail to push transaction.
    400: Fail signature.

    500: User back to dapp without any action
    501: Not use yet.
```

-----

# License 


    Copyright 2018 WizardWorks Inc. All rights reserved.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


[1]: http://bit.ly/2CySJnr
[2]: http://bit.ly/2Lj7Bdu
[4]: https://github.com/EOSNOVA/Auth-SDK-ANDROID/blob/master/apk/nova_testnet.apk
[5]: https://www.youtube.com/watch?v=-nfbC_U9xcM
[11]: https://github.com/EOSNOVA/Auth-SDK-ANDROID/blob/master/app/src/main/java/io/eosnova/wallet/android/sdk/sample/eos/EosJavaActivity.java
[12]: https://github.com/EOSNOVA/Auth-SDK-ANDROID/blob/master/app/src/main/java/io/eosnova/wallet/android/sdk/sample/eos/EosJavaAppCompatActivity.java
[13]: https://github.com/EOSNOVA/Auth-SDK-ANDROID/blob/master/app/src/main/java/io/eosnova/wallet/android/sdk/sample/klaytn/KlaytnJavatActivity.java
[14]: https://github.com/EOSNOVA/Auth-SDK-ANDROID/blob/master/app/src/main/java/io/eosnova/wallet/android/sdk/sample/klaytn/KlaytnJavaAppCompatActivity.java