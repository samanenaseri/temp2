package com.xc.pay_print;

import android.util.Log;
import androidx.annotation.NonNull;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import java.util.HashMap;
import java.util.Map;

public class PaymentPlugin implements FlutterPlugin, MethodCallHandler {
    private MethodChannel channel;
    private PaymentManager paymentManager;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPlugin.FlutterPluginBinding binding) {
        channel = new MethodChannel(binding.getBinaryMessenger(), "com.xc.pay_print/payment");
        channel.setMethodCallHandler(this);
        paymentManager = new PaymentManager(binding.getApplicationContext(), channel);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        switch (call.method) {
            case "startPayment":
                if (call.argument("amount") != null) {
                    double amount = (double) call.argument("amount");
                    paymentManager.startPayment(amount);
                    result.success(true);
                } else {
                    result.error("INVALID_ARGUMENTS", "Amount cannot be null", null);
                }
                break;
            case "onPaymentResult":
                String status = call.argument("status");
                String message = call.argument("message");
                Log.d("PaymentPlugin", "Received payment result: " + status + " - " + message);
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPlugin.FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }
} 