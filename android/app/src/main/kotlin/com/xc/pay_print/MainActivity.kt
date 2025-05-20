package com.xc.pay_print

import android.os.Bundle
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterFragmentActivity() {
    private val CHANNEL = "com.xc.pay_print/printer"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        flutterEngine.plugins.add(PrinterPlugin())
        flutterEngine.plugins.add(PaymentPlugin())

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
    }
}
