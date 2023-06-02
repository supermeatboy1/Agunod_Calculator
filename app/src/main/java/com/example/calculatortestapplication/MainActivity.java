package com.example.calculatortestapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button numberButtons[];
    private Button operationButtons[];
    private Button buttonPoint, buttonSign, buttonClear, buttonEquals;
    private TextView textViewOperation;
    private EditText editTextNumber;

    public static class ArithmeticOperator {
        public static final int ADD = 0;
        public static final int SUBTRACT = 1;
        public static final int MULTIPLY = 2;
        public static final int DIVIDE = 3;
        public static final int EQUALS = 4;
    }
    private boolean hasInput, continuous;
    private double number1, number2;
    private int currentOperator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberButtons = new Button[10]; // Numbers from 0 to 9 (10) + Decimal point (1)
        operationButtons = new Button[4]; // Four operations

        numberButtons[0] = (Button) findViewById(R.id.button0);
        numberButtons[1] = (Button) findViewById(R.id.button1);
        numberButtons[2] = (Button) findViewById(R.id.button2);
        numberButtons[3] = (Button) findViewById(R.id.button3);
        numberButtons[4] = (Button) findViewById(R.id.button4);
        numberButtons[5] = (Button) findViewById(R.id.button5);
        numberButtons[6] = (Button) findViewById(R.id.button6);
        numberButtons[7] = (Button) findViewById(R.id.button7);
        numberButtons[8] = (Button) findViewById(R.id.button8);
        numberButtons[9] = (Button) findViewById(R.id.button9);

        operationButtons[0] = (Button) findViewById(R.id.buttonAdd);
        operationButtons[1] = (Button) findViewById(R.id.buttonSubtract);
        operationButtons[2] = (Button) findViewById(R.id.buttonMultiply);
        operationButtons[3] = (Button) findViewById(R.id.buttonDivide);

        buttonPoint = (Button) findViewById(R.id.buttonPoint);
        buttonPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkInputLength())
                    return;

                String text = editTextNumber.getText().toString();
                if (!text.contains(".")) {
                    editTextNumber.setText(
                            text + "."
                    );
                }
            }
        });
        buttonSign = (Button) findViewById(R.id.buttonSign);
        buttonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkInputLength())
                    return;

                String text = editTextNumber.getText().toString();
                if (text.startsWith("-")) {
                    editTextNumber.setText(text.substring(1));
                } else {
                    editTextNumber.setText("-" + text);
                }
            }
        });

        buttonClear = (Button) findViewById(R.id.buttonClear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearState();
            }
        });
        buttonEquals = (Button) findViewById(R.id.buttonEquals);
        buttonEquals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                evaluate(ArithmeticOperator.EQUALS);
            }
        });

        editTextNumber = (EditText) findViewById(R.id.editTextNumber);
        textViewOperation = (TextView) findViewById(R.id.textViewOperation);

        for (int i = 0; i < numberButtons.length; i++) {
            final String btn_num = numberButtons[i].getText().toString();
            numberButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (continuous) {
                        editTextNumber.setText(btn_num);
                        continuous = false;
                    } else {
                        editTextNumber.setText(editTextNumber.getText() + btn_num);
                    }
                }
            });
        }
        for (int i = 0; i < operationButtons.length; i++) {
            final int fi = i;
            operationButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    evaluate(fi);
                }
            });
        }
    }
    public void evaluate(int operator) {
        displayError(" ");

        if (hasInput) {
            if (!checkInputLength() && !checkNumberFormatting())
                return;

            number2 = Double.parseDouble(editTextNumber.getText().toString());
            switch (currentOperator) {
                case ArithmeticOperator.ADD:
                    number2 += number1;
                    updateNumberText();
                    break;
                case ArithmeticOperator.SUBTRACT:
                    number2 = number1 - number2;
                    updateNumberText();
                    break;
                case ArithmeticOperator.MULTIPLY:
                    number2 = number1 * number2;
                    updateNumberText();
                    break;
                case ArithmeticOperator.DIVIDE:
                    if (number2 == 0) {
                        clearState();
                        displayError("Division by zero is not allowed.");
                    } else {
                        number2 = (number1 / number2);
                        updateNumberText();
                    }
                    break;
                default:
                    clearState();
                    displayError("ERROR: Invalid Arithmetic Operator!");
                    break;
            }

            if (operator != ArithmeticOperator.EQUALS) {
                currentOperator = operator;
                hasInput = true;
            }
            continuous = true;

            number1 = number2;
        } else {
            if (!checkInputLength())
                return;

            if (operator < ArithmeticOperator.EQUALS && checkNumberFormatting()) {
                currentOperator = operator;
                number1 = Double.parseDouble(editTextNumber.getText().toString());
                hasInput = true;

                editTextNumber.setText("");
            }
        }
    }
    public void updateNumberText() {
        hasInput = false;
        editTextNumber.setText(Double.toString(number2));
    }
    public void clearState() {
        hasInput = false;
        number1 = number2 = 0.0d;
        currentOperator = -1;
        continuous = false;

        editTextNumber.setText("");
        textViewOperation.setText("");
    }
    public void displayError(String errorMsg) {
        textViewOperation.setText(errorMsg);
    }
    public boolean checkNumberFormatting() {
        try {
            double test = Double.parseDouble(editTextNumber.getText().toString());

            return true;
        } catch (NumberFormatException e) {
            clearState();
            displayError("Please input a properly formatted number.");
            return false;
        }
    }
    public boolean checkInputLength() {
        if (editTextNumber.getText().length() == 0) {
            displayError("Please input a number first.");
            return false;
        }
        return true;
    }
}