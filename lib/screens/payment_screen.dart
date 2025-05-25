// import 'package:flutter/material.dart';
// import '../services/payment_service.dart';
//
// class PaymentScreen extends StatefulWidget {
//   const PaymentScreen({super.key});
//
//   @override
//   _PaymentScreenState createState() => _PaymentScreenState();
// }
//
// class _PaymentScreenState extends State<PaymentScreen> {
//   final TextEditingController _amountController = TextEditingController();
//   final PaymentService _paymentService = PaymentService();
//   String _paymentStatus = '';
//
//   @override
//   void initState() {
//     super.initState();
//     // Set the callback to receive payment results
//     _paymentService.onPaymentResult = (status, message) {
//       setState(() {
//         _paymentStatus = "Result: \$status - \$message";
//       });
//     };
//   }
//
//   @override
//   void dispose() {
//     _amountController.dispose();
//     // Remove the callback to prevent memory leaks
//     _paymentService.onPaymentResult = null;
//     super.dispose();
//   }
//
//   void _startPayment() async {
//     final double? amount = double.tryParse(_amountController.text);
//     if (amount == null || amount <= 0) {
//       ScaffoldMessenger.of(context).showSnackBar(
//         const SnackBar(content: Text('Please enter a valid amount')),
//       );
//       return;
//     }
//
//     setState(() {
//       _paymentStatus = 'Processing payment...';
//     });
//
//     // Call the native payment method (no longer expecting a boolean return)
//     await _paymentService.startPayment(amount);
//
//     // The status will be updated by the onPaymentResult callback
//   }
//
//   @override
//   Widget build(BuildContext context) {
//     return Scaffold(
//       appBar: AppBar(
//         title: const Text('Process Payment'),
//       ),
//       body: Padding(
//         padding: const EdgeInsets.all(16.0),
//         child: Column(
//           mainAxisAlignment: MainAxisAlignment.center,
//           children: [
//             TextField(
//               controller: _amountController,
//               keyboardType: TextInputType.number,
//               decoration: const InputDecoration(
//                 labelText: 'Enter Amount',
//                 border: OutlineInputBorder(),
//               ),
//             ),
//             const SizedBox(height: 20),
//             ElevatedButton(
//               onPressed: _startPayment,
//               child: const Text('Start Payment'),
//             ),
//             const SizedBox(height: 20),
//             Text(
//               _paymentStatus,
//               style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
//               textAlign: TextAlign.center,
//             ),
//           ],
//         ),
//       ),
//     );
//   }
// }

import 'package:flutter/material.dart';
import '../services/payment_service.dart';

class PaymentScreen extends StatefulWidget {
  const PaymentScreen({super.key});

  @override
  _PaymentScreenState createState() => _PaymentScreenState();
}

class _PaymentScreenState extends State<PaymentScreen> {
  final TextEditingController _amountController = TextEditingController();
  final PaymentService _paymentService = PaymentService();
  String _paymentStatus = '';

  @override
  void initState() {
    super.initState();
    _paymentService.onPaymentResult = (status, message) {
      setState(() {
        _paymentStatus = "Result: $status - $message";// ✅ fixed interpolation
      });
    };
  }

  @override
  void dispose() {
    _amountController.dispose();
    _paymentService.onPaymentResult = null;
    super.dispose();
  }

  void _startPayment() async {
    final double? amount = double.tryParse(_amountController.text);
    if (amount == null || amount <= 0) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please enter a valid amount')),
      );
      return;
    }

    setState(() {
      _paymentStatus = 'Processing payment...';
    });

    final bool success = await _paymentService.startPayment(amount);

    if (success) {
      setState(() {
        _paymentStatus = 'Payment initiated. Waiting for result...';
      });
    } else {
      setState(() {
        _paymentStatus = 'Failed to initiate payment.';
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Process Payment')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            TextField(
              controller: _amountController,
              keyboardType: TextInputType.number,
              decoration: const InputDecoration(
                labelText: 'Enter Amount',
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: _startPayment,
              child: const Text('Start Payment'),
            ),
            const SizedBox(height: 20),
            Text(
              _paymentStatus,
              style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
              textAlign: TextAlign.center,
            ),
          ],
        ),
      ),
    );
  }
}
