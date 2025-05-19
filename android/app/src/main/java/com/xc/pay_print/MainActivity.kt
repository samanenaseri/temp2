package com.xc.pay_print

import android.os.Bundle
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.plugin.common.MethodChannel
import android.content.Context

class MainActivity : FlutterFragmentActivity() {
    private val CHANNEL = "com.xc.pay_print/print"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MethodChannel(flutterEngine?.dartExecutor?.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            if (call.method == "printReceipt") {
                val printerManager = PrinterManager(this)
                val lines = call.argument<List<String>>("lines")?.toTypedArray() ?: arrayOf("Default line")
                printerManager.printReceipt(lines)
                result.success("Printed")
            } else if (call.method == "print") {
                val printerManager = PrinterManager(this)
                printerManager.print()
                result.success("Printed single line")
            } else {
                result.notImplemented()
            }
        }
    }
}