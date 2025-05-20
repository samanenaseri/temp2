package com.xc.pay_print;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.pos.sdk.emvcore.POIEmvCoreManager;
import com.pos.sdk.emvcore.IPosEmvCoreListener;

import java.util.HashMap;
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
        this.emvCoreManager = new POIEmvCoreManager(context);

        Log.d(TAG, "PaymentManager initialized with POIEmvCoreManager");
    }

    public void startPayment(double amount) {
        Log.d(TAG, "Starting EMV payment for amount: " + amount);

        emvCoreManager.startEmv(amount, new IPosEmvCoreListener() {
            @Override
            public void onTransactionResult(Bundle result) {
                String resultCode = result.getString("resultCode");
                String resultMsg = result.getString("resultMsg");

                Log.d(TAG, "Transaction Result - Code: " + resultCode + ", Message: " + resultMsg);

                if ("00".equals(resultCode)) {
                    sendStatusToFlutter("success", resultMsg);
                } else {
                    sendStatusToFlutter("failed", resultMsg);
                }
            }

            @Override
            public void onEmvProcess(int code) {
                Log.d(TAG, "EMV process code: " + code);
            }

            @Override
            public void onSelectApplication(java.util.List<String> apps) {
                Log.d(TAG, "Select Application callback");
                // Optional: implement application selection
            }

            @Override
            public void onConfirmCardInfo(int type, String cardNo) {
                Log.d(TAG, "Card Info Confirmed - Type: " + type + ", CardNo: " + cardNo);
            }

            @Override
            public void onKernelType(int kernelType) {
                Log.d(TAG, "Kernel Type: " + kernelType);
            }

            @Override
            public void onSecondTapCard() {
                Log.d(TAG, "Second Tap Card required");
            }

            @Override
            public void onRequestInputPin(Bundle data) {
                Log.d(TAG, "PIN Input Requested");
            }

            @Override
            public void onRequestOnlineProcess(Bundle data) {
                Log.d(TAG, "Online Process Requested");
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
