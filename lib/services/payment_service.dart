// import 'package:flutter/services.dart';
//
// class PaymentService {
//   static const MethodChannel _channel = MethodChannel('com.xc.pay_print/payment');
//
//   // Callback function to handle payment results
//   Function(String status, String message)? onPaymentResult;
//
//   PaymentService() {
//     // Set up a method call handler to receive results from the native side
//     _channel.setMethodCallHandler(_handleMethodCall);
//   }
//
//   Future<dynamic> _handleMethodCall(MethodCall call) async {
//     switch (call.method) {
//       case 'onPaymentResult':
//         if (onPaymentResult != null) {
//           final String status = call.arguments['status'];
//           final String message = call.arguments['message'];
//           onPaymentResult!(status, message); // Invoke the callback
//         }
//         break;
//       default:
//         // Log unhandled method calls for debugging
//         print('Unhandled method call received from native: ${call.method}');
//         // throw PlatformException(code: 'UNIMPLEMENTED', message: 'Method not implemented');
//     }
//   }
//
//   // Method to start a payment (now returns void)
//   Future<void> startPayment(double amount) async {
//     try {
//       // We don't await a specific return value here, as the actual result
//       // comes via the onPaymentResult callback.
//       await _channel.invokeMethod('startPayment', {'amount': amount});
//        print('Successfully invoked native startPayment.'); // Log successful invocation
//     } on PlatformException catch (e) {
//       print('Error invoking native startPayment: ${e.message}');
//       // You might want to call onPaymentResult with an error status here as well
//        onPaymentResult != null?('error', 'Invocation failed: ${e.message}'):'error';
//     }
//   }
//
//   // You might want to add a method here to listen for payment results from native
//   // For example, using EventChannel
// }


import 'package:flutter/services.dart';

class PaymentService {
  static const MethodChannel _channel = MethodChannel('com.xc.pay_print/payment');

  // Callback to receive payment result from native
  Function(String status, String message)? onPaymentResult;

  PaymentService() {
    _channel.setMethodCallHandler(_handleMethodCall);
  }

  Future<dynamic> _handleMethodCall(MethodCall call) async {
    switch (call.method) {
      case 'onPaymentResult':
        final String status = call.arguments['status'];
        final String message = call.arguments['message'];
        print("Received from native: $status - $message");
        if (onPaymentResult != null) {
          onPaymentResult!(status, message);
        }
        break;
      default:
        throw PlatformException(code: 'UNIMPLEMENTED', message: 'Method not implemented');
    }
  }

  Future<bool> startPayment(double amount) async {
    try {
      print("Invoking native payment with amount: $amount");
      final result = await _channel.invokeMethod('startPayment', {'amount': amount});
      return result == true;
    } on PlatformException catch (e) {
      print('Error starting payment: ${e.message}');
      return false;
    }
  }
}
