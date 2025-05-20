package com.xc.pay_print;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.os.IBinder;

import com.pos.sdk.emvcore.POIEmvCoreManager;
import com.pos.sdk.emvcore.IPosEmvCoreListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import io.flutter.plugin.common.MethodChannel;

public class PaymentManager {

    private static final String TAG = "PaymentManager";
    private final Context context;
    private final MethodChannel channel;
    private final POIEmvCoreManager emvCoreManager;

    public PaymentManager(Context context, MethodChannel channel) {
        this.context = context;
        this.channel = channel;
        this.emvCoreManager = POIEmvCoreManager.getDefault();
        Log.d(TAG, "PaymentManager initialized with POIEmvCoreManager.");
    }

    public void startPayment(double amount) {
        Log.d(TAG, "Starting payment for amount: " + amount);

        Bundle bundle = new Bundle();
        bundle.putString("amount", String.valueOf((int) amount));

        emvCoreManager.startTransaction(bundle, new IPosEmvCoreListener() {
            @Override
            public void onEmvProcess(int type, Bundle bundle) {
                Log.d(TAG, "EMV process started. Type: " + type);
            }
            @Override
            public IBinder asBinder() {
                return null; // یا یک Binder واقعی اگر SDK نیاز دارد
            }

            @Override
            public void onSelectApplication(List<String> list, boolean isFirstSelect) {
                Log.d(TAG, "Application selection requested.");
            }

            @Override
            public void onConfirmCardInfo(int mode, Bundle bundle) {
                Log.d(TAG, "Confirm card info requested.");
            }


            public void onKernelType(int type) {
                Log.d(TAG, "Kernel type detected: " + type);
            }

            @Override
            public void onSecondTapCard() {
                Log.d(TAG, "Second card tap requested.");
            }

            @Override
            public void onRequestInputPin(Bundle bundle) {
                Log.d(TAG, "PIN input requested.");
            }

            @Override
            public void onRequestOnlineProcess(Bundle bundle) {
                Log.d(TAG, "Online process requested.");
            }

            @Override
            public void onTransactionResult(int result, Bundle bundle) {
                Log.d(TAG, "Transaction completed. Result code: " + result);
                if (result == 0) {
                    sendStatusToFlutter("success", "Transaction Approved");
                } else {
                    sendStatusToFlutter("failed", "Transaction Failed with code: " + result);
                }
            }
        });
    }

    private void sendStatusToFlutter(String status, String message) {
        if (channel != null) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", status);
            result.put("message", message);
            channel.invokeMethod("onPaymentResult", result);
        }
    }
}
