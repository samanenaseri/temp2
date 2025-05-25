package com.xc.pay_print;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.pos.sdk.emvcore.POIEmvCoreManager;
import com.pos.sdk.emvcore.IPosEmvCoreListener;
import com.pos.sdk.emvcore.PosEmvErrorCode;

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
        Log.d(TAG, "PaymentManager initialized.");
        if (emvCoreManager == null) {
            Log.e(TAG, "emvCoreManager is null");
        }
    }

    public void startPayment(double amount) {
        Log.d(TAG, "startPayment called with amount: " + amount);

        Bundle bundle = new Bundle();
        String formattedAmount = String.format("%012d", (int) (amount * 100));// ← اینجاست
        bundle.putString("amount", formattedAmount);

        emvCoreManager.startTransaction(bundle, new IPosEmvCoreListener() {
            @Override
            public void onEmvProcess(int type, Bundle bundle) {
                Log.d(TAG, "EMV process started. Type: " + type);
            }

            @Override
            public IBinder asBinder() {
                return new android.os.Binder();
            }

            @Override
            public void onSelectApplication(List<String> list, boolean isFirstSelect) {
                Log.d(TAG, "Application selection requested.");
            }

            @Override
            public void onConfirmCardInfo(int mode, Bundle bundle) {
                Log.d(TAG, "Confirm card info requested.");
            }

            @Override
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
                Log.d(TAG, "Transaction result code: " + result);
                if (result == PosEmvErrorCode.EMV_OK) {
                    sendStatusToFlutter("success", "Transaction Approved");
                } else {
                    sendStatusToFlutter("failed", getErrorMessage(result));
                }
            }
        });
    }

    private String getErrorMessage(int errorCode) {
        switch (errorCode) {
            case PosEmvErrorCode.EMV_DECLINED:
                return "Transaction Declined";
            case PosEmvErrorCode.EMV_TIMEOUT:
                return "Transaction Timeout";
            default:
                return "Transaction Failed: Unknown error (" + errorCode + ")";
        }
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
