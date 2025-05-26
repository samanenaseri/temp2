package com.xc.pay_print

import android.os.Bundle
import android.util.Log
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import com.xc.pay_print.PaymentManager

class MainActivity : FlutterFragmentActivity() {

    private val PRINTER_CHANNEL = "com.xc.pay_print/printer"
    private val PAYMENT_CHANNEL = "com.xc.pay_print/payment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "🔥 onCreate called")
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        // Printer Channel
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, PRINTER_CHANNEL)
            .setMethodCallHandler { call, result ->
                val printerManager = PrinterManager(this)

                when (call.method) {
                    "printReceipt" -> {
                        val lines = call.argument<List<String>>("lines")?.toTypedArray()
                            ?: arrayOf("Default line")
                        printerManager.printReceipt(lines)
                        result.success("Printed")
                    }
                    "print" -> {
                        printerManager.print()
                        result.success("Printed single line")
                    }
                    else -> result.notImplemented()
                }
            }

        // Payment Channel
        val paymentChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, PAYMENT_CHANNEL)
        val paymentManager = PaymentManager(paymentChannel)

        paymentChannel.setMethodCallHandler { call, result ->
            when (call.method) {
                "startPayment" -> {
                    val amount = call.argument<Double>("amount") ?: 0.0
                    Log.d("MainActivity", "🔷 startPayment called with amount: $amount")
                    paymentManager.startPayment(amount)
                    result.success(null) // پاسخ به Flutter بعداً با invokeMethod داده می‌شود
                }
                else -> result.notImplemented()
            }
        }
    }
}
