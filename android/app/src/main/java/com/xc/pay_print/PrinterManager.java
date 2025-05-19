package com.xc.pay_print;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import com.xxx.printerlib.POIPrinterManager;
import com.xxx.printerlib.TextPrintLine;
import com.xxx.printerlib.PrintLine;

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
        printerManager.setLineSpace(2);
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
                // Do something on start
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

    public void print() {
        printerManager.open();
        printerManager.setPrintFont("/system/fonts/DroidSansMono.ttf");
        printerManager.setPrintGray(2000);
        printerManager.setLineSpace(2);
        printerManager.cleanCache();

        TextPrintLine line = new TextPrintLine();
        line.setType(PrintLine.TEXT);
        line.setPosition(PrintLine.CENTER);
        line.setSize(TextPrintLine.FONT_NORMAL);
        line.setContent("Hello Printer!");

        List<TextPrintLine> lines = new ArrayList<>();
        lines.add(line);
        printerManager.addPrintLine(lines);

        printerManager.beginPrint(new POIPrinterManager.IPrinterListener() {
            @Override
            public void onStart() {}

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