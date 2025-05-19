package com.xc.pay_print;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

public class PrinterManager {
    private final POIPrinterManager printerManager;
    private final Context context;

    public PrinterManager(Context context) {
        this.context = context;
        this.printerManager = new POIPrinterManager(context);
    }

    public void printReceipt(String[] lines) {
        printerManager.open();
        printerManager.setPrintFont("/system/fonts/DroidSansMono.ttf");
        printerManager.setPrintGray(2000);
        printerManager.setLineSpace(Integer.valueOf(2));
        printerManager.cleanCache();

        List<TextPrintLine> printLines = new ArrayList<>();
        
        for (String line : lines) {
            TextPrintLine textPrintLine = new TextPrintLine();
            textPrintLine.setType(PrintLine.TEXT);
            textPrintLine.setPosition(PrintLine.CENTER);
            textPrintLine.setSize(TextPrintLine.FONT_NORMAL);
            textPrintLine.setContent(line);
            printLines.add(textPrintLine);
        }

        printerManager.addPrintLine(printLines);

        printerManager.beginPrint(new POIPrinterManager.IPrinterListener() {
            @Override
            public void onStart() {
                // Print started
            }

            @Override
            public void onFinish() {
                printerManager.close();
            }

            @Override
            public void onError(int code, String msg) {
                printerManager.close();
            }
        });
    }
} 