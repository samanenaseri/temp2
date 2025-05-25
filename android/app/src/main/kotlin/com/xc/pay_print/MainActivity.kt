package com.xc.pay_print

import android.os.Bundle
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import android.util.Log

class MainActivity : FlutterFragmentActivity() {

    private val CHANNEL = "com.xc.pay_print/printer"
    private val PAYMENT_CHANNEL = "com.xc.pay_print/payment"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        Log.d("MainActivity", "configureFlutterEngine called ✅")

        // Printer channel
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            val printerManager = PrinterManager(this)

            when (call.method) {
                "printReceipt" -> {
                    val lines = call.argument<List<String>>("lines")?.toTypedArray() ?: arrayOf("Default line")
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

        // Payment channel
        val paymentChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, PAYMENT_CHANNEL)
        val paymentManager = PaymentManager(this, paymentChannel)

        paymentChannel.setMethodCallHandler { call, result ->
            when (call.method) {
                "startPayment" -> {
                    val amount = call.argument<Double>("amount") ?: 0.0
                    paymentManager.startPayment(amount)
                    result.success(null)
                }
                else -> result.notImplemented()
            }
        }
    }
}
