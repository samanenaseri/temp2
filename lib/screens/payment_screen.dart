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
    // Set the callback to receive payment results
    _paymentService.onPaymentResult = (status, message) {
      setState(() {
        _paymentStatus = "Result: \$status - \$message"; // Use double quotes for interpolation
      });
    };
  }

  @override
  void dispose() {
    _amountController.dispose();
    // Remove the callback to prevent memory leaks
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

    // Call the native payment method
    final bool success = await _paymentService.startPayment(amount);

    if (success) {
      // Note: This success just means the native method was called.
      // The actual payment result will come back asynchronously.
      setState(() {
        _paymentStatus = 'Payment initiated. Waiting for result...';
      });
      // In a real app, you would listen for the payment result from the native side
      // and update the UI accordingly.

    } else {
      setState(() {
        _paymentStatus = 'Failed to initiate payment.';
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Process Payment'),
      ),
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