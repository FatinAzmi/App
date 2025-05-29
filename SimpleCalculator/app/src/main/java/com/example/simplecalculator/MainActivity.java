package com.example.simplecalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvDisplay;
    private TextView tvExpression;
    private String currentExpression = "";
    private String currentNumber = "";
    private String operator = "";
    private double firstNumber = 0;
    private boolean isNewNumber = true;
    private boolean justCalculated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplay = findViewById(R.id.tvDisplay);
        tvExpression = findViewById(R.id.tvExpression);

        // Number buttons
        setupNumberButton(R.id.btn0, "0");
        setupNumberButton(R.id.btn1, "1");
        setupNumberButton(R.id.btn2, "2");
        setupNumberButton(R.id.btn3, "3");
        setupNumberButton(R.id.btn4, "4");
        setupNumberButton(R.id.btn5, "5");
        setupNumberButton(R.id.btn6, "6");
        setupNumberButton(R.id.btn7, "7");
        setupNumberButton(R.id.btn8, "8");
        setupNumberButton(R.id.btn9, "9");

        // Operator buttons
        setupOperatorButton(R.id.btnAdd, "+");
        setupOperatorButton(R.id.btnSubtract, "-");
        setupOperatorButton(R.id.btnMultiply, "*");
        setupOperatorButton(R.id.btnDivide, "/");

        // Special buttons
        findViewById(R.id.btnDecimal).setOnClickListener(v -> addDecimal());
        findViewById(R.id.btnEquals).setOnClickListener(v -> calculate());
        findViewById(R.id.btnClear).setOnClickListener(v -> clear());
        findViewById(R.id.btnPlusMinus).setOnClickListener(v -> toggleSign());
        findViewById(R.id.btnPercent).setOnClickListener(v -> percentage());
    }

    private void setupNumberButton(int buttonId, String number) {
        findViewById(buttonId).setOnClickListener(v -> addNumber(number));
    }

    private void setupOperatorButton(int buttonId, String op) {
        findViewById(buttonId).setOnClickListener(v -> setOperator(op));
    }

    private void addNumber(String number) {
        if (justCalculated) {
            // Jika baru selesai kalkulasi, mulai ekspresi baru
            clear();
            justCalculated = false;
        }

        if (isNewNumber) {
            currentNumber = number;
            isNewNumber = false;
        } else {
            currentNumber += number;
        }

        updateExpressionDisplay();
        updateDisplay();
    }

    private void addDecimal() {
        if (justCalculated) {
            clear();
            justCalculated = false;
        }

        if (isNewNumber) {
            currentNumber = "0.";
            isNewNumber = false;
        } else if (!currentNumber.contains(".")) {
            currentNumber += ".";
        }

        updateExpressionDisplay();
        updateDisplay();
    }

    private void setOperator(String op) {
        if (!currentNumber.isEmpty()) {
            if (!operator.isEmpty() && !isNewNumber) {
                // Jika sudah ada operator dan sedang input angka, hitung dulu
                calculate();
            } else {
                firstNumber = Double.parseDouble(currentNumber);
            }

            operator = op;
            currentExpression = currentNumber + " " + getOperatorSymbol(op) + " ";
            isNewNumber = true;
            justCalculated = false;

            tvExpression.setText(currentExpression);
        }
    }

    private String getOperatorSymbol(String op) {
        switch (op) {
            case "+": return "+";
            case "-": return "-";
            case "*": return "×";
            case "/": return "÷";
            default: return op;
        }
    }

    private void calculate() {
        if (!operator.isEmpty() && !currentNumber.isEmpty()) {
            double secondNumber = Double.parseDouble(currentNumber);
            double result = 0;

            // Tampilkan ekspresi lengkap
            currentExpression += currentNumber + " = ";

            switch (operator) {
                case "+":
                    result = firstNumber + secondNumber;
                    break;
                case "-":
                    result = firstNumber - secondNumber;
                    break;
                case "*":
                    result = firstNumber * secondNumber;
                    break;
                case "/":
                    if (secondNumber != 0) {
                        result = firstNumber / secondNumber;
                    } else {
                        tvDisplay.setText("Error");
                        tvExpression.setText("Cannot divide by zero");
                        return;
                    }
                    break;
            }

            // Format result to avoid unnecessary decimals
            if (result == (long) result) {
                currentNumber = String.valueOf((long) result);
            } else {
                currentNumber = String.valueOf(result);
            }

            firstNumber = result;
            operator = "";
            isNewNumber = true;
            justCalculated = true;

            // Update displays
            tvExpression.setText(currentExpression + currentNumber);
            updateDisplay();
        }
    }

    private void clear() {
        currentNumber = "";
        currentExpression = "";
        operator = "";
        firstNumber = 0;
        isNewNumber = true;
        justCalculated = false;
        tvDisplay.setText("0");
        tvExpression.setText("");
    }

    private void toggleSign() {
        if (!currentNumber.isEmpty() && !currentNumber.equals("0")) {
            if (currentNumber.startsWith("-")) {
                currentNumber = currentNumber.substring(1);
            } else {
                currentNumber = "-" + currentNumber;
            }
            updateExpressionDisplay();
            updateDisplay();
        }
    }

    private void percentage() {
        if (!currentNumber.isEmpty()) {
            double value = Double.parseDouble(currentNumber);
            value = value / 100;

            if (value == (long) value) {
                currentNumber = String.valueOf((long) value);
            } else {
                currentNumber = String.valueOf(value);
            }
            updateExpressionDisplay();
            updateDisplay();
        }
    }

    private void updateDisplay() {
        if (currentNumber.isEmpty()) {
            tvDisplay.setText("0");
        } else {
            tvDisplay.setText(currentNumber);
        }
    }

    private void updateExpressionDisplay() {
        if (!operator.isEmpty() && !isNewNumber) {
            // Sedang input angka kedua
            String tempExpression = "";
            if (firstNumber == (long) firstNumber) {
                tempExpression = String.valueOf((long) firstNumber);
            } else {
                tempExpression = String.valueOf(firstNumber);
            }
            tempExpression += " " + getOperatorSymbol(operator) + " " + currentNumber;
            tvExpression.setText(tempExpression);
        } else if (!justCalculated) {
            // Reset expression display jika tidak sedang dalam kalkulasi
            tvExpression.setText("");
        }
    }
}