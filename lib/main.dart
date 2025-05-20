import 'package:flutter/material.dart';
import 'services/printer_service.dart';
import 'screens/payment_screen.dart';

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
      routes: {
        '/payment': (context) => const PaymentScreen(),
      },
    );
  }
}

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final List<String> receiptLines = [
      'HE68659031228',
      'هما اکسپرس',
      'Homa Express',
      'شعبه تهران',
      '----------------------------------------',
      'مشخصات فرستنده',
      'کد ملی: 0451186478',
      'فرستنده: Mohsen Jokar',
      'مبدا: ایران آذربایجان شرقی',
      'آدرس: تهران چهارراه استانبول روبه روی پاساژ پلاسکو پاساژ',
      'پروانه پلاک 272 کافه سیلور - Tehran - 1674956111',
      '----------------------------------------',
      'مشخصات گیرنده',
      'گیرنده: محمدرضا 2 گیرنده: محمدرضا 2',
      'میرزایی 2 میرزایی 2',
      'کد ملی: 0410990825',
      'مقصد: مالاوی - Chiradzulu',
      'Prizren - 3371785665 - ستی',
      '----------------------------------------',
      'مشخصات مرسوله',
      'وزن: 10 کیلوگرم',
      'ارزش اظهار شده: 100000',
      'تاریخ قبول: 28-12-1403',
      '----------------------------------------',
      'مرکز تماس: ۸۹۴۴',
      'www.homaexpressco.com',
    ];

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
                final success = await printerService.printReceipt(receiptLines);
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
            const SizedBox(height: 20),
            ElevatedButton.icon(
              onPressed: () {
                Navigator.pushNamed(context, '/payment');
              },
              icon: const Icon(Icons.payment),
              label: const Text('Process Payment'),
            ),
          ],
        ),
      ),
    );
  }
}
