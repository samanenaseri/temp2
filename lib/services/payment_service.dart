import 'package:flutter/services.dart';

class PaymentService {
  static const MethodChannel _channel = MethodChannel('com.xc.pay_print/payment');

  // Callback function to handle payment results
  Function(String status, String message)? onPaymentResult;

  PaymentService() {
    // Set up a method call handler to receive results from the native side
    _channel.setMethodCallHandler(_handleMethodCall);
  }

  Future<dynamic> _handleMethodCall(MethodCall call) async {
    switch (call.method) {
      case 'onPaymentResult':
        if (onPaymentResult != null) {
          final String status = call.arguments['status'];
          final String message = call.arguments['message'];
          onPaymentResult!(status, message); // Invoke the callback
        }
        break;
      default:
        throw PlatformException(code: 'UNIMPLEMENTED', message: 'Method not implemented');
    }
  }

  // Method to start a payment
  Future<bool> startPayment(double amount) async {
    try {
      // The native side will handle the actual payment process asynchronously.
      // The result here only indicates if the call to the native method was successful.
      // The actual payment outcome (success/failure) needs to be communicated
      // back from the native side, perhaps using event channels or callbacks.
      final bool result = await _channel.invokeMethod('startPayment', {'amount': amount});
      return result;
    } on PlatformException catch (e) {
      print('Error starting payment: ${e.message}');
      return false;
    }
  }

  // You might want to add a method here to listen for payment results from native
  // For example, using EventChannel
} 