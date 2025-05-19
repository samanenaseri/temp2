import 'package:flutter/material.dart';
import 'services/printer_service.dart';

void main() {
  runApp(const PayPrintApp());
}

class PayPrintApp extends StatelessWidget {
  const PayPrintApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Pay Print',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        useMaterial3: true,
      ),
      home: const HomeScreen(),
    );
  }
}

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Pay Print'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            ElevatedButton.icon(
              onPressed: () async {
                final printerService = PrinterService();
                final lines = [
                  '11111',
                  '22222',
                  '33333',
                ];
                final success = await printerService.printReceipt(lines);
                if (success) {
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(content: Text('Receipt printed successfully')),
                  );
                } else {
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(content: Text('Failed to print receipt')),
                  );
                }
              },
              icon: const Icon(Icons.print),
              label: const Text('Print Test Receipt'),
            ),
          ],
        ),
      ),
    );
  }
}
