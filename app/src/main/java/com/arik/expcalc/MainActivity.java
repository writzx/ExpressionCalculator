package com.arik.expcalc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private AppCompatButton _1;
    private AppCompatButton _2;
    private AppCompatButton _3;
    private AppCompatButton _4;
    private AppCompatButton _5;
    private AppCompatButton _6;
    private AppCompatButton _7;
    private AppCompatButton _8;
    private AppCompatButton _9;
    private AppCompatButton _0;
    private AppCompatButton plus;
    private AppCompatButton minus;
    private AppCompatButton divide;
    private AppCompatButton multiply;
    private AppCompatButton point;
    private AppCompatButton equals;
    private AppCompatButton ac;
    private AppCompatButton del;
    private AppCompatButton lbracket;
    private AppCompatButton rbracket;
    private AppCompatButton plusminus;
    private AppCompatButton sqroot;
    private AppCompatButton curoot;
    private AppCompatButton power;
    private AppCompatButton percent;

    private AppCompatEditText numbox;

    private AppCompatButton[] char_btns;
    private AppCompatButton[] cmd_btns;
    private AppCompatButton[] op_btns;
    private AppCompatButton[] fun_btns;
    private AppCompatButton[] num_btns;

    private int countChars(String s, char ch) {
        int counter = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ch) {
                counter++;
            }
        }
        return counter;
    }

    private boolean isOperator(char ch) {
        for (AppCompatButton abtn : op_btns) {
            if (abtn.getText().equals(ch + "")) {
                return true;
            }
        }
        return false;
    }

    private boolean isFunction(char ch) {
        if (ch == '-') return true;
        for (AppCompatButton abtn : fun_btns) {
            if (abtn.getText().equals(ch + "")) {
                return true;
            }
        }
        return false;
    }

    private boolean isNumber(char ch) {
        for (AppCompatButton abtn : num_btns) {
            if (abtn.getText().equals(ch + "") && abtn != rbracket) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        for (AppCompatButton btn : char_btns) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pend_brack = countChars(numbox.getText().toString(), '(') - countChars(numbox.getText().toString(), ')');
                    boolean last_fun = false, last_op = false, last_num = false, last_dot = false;
                    if (numbox.length() > 0) {
                        char ch = numbox.getText().charAt(numbox.length() - 1);
                        last_dot = ch == '.';
                        last_op = last_dot || isOperator(ch);
                        last_fun = isFunction(ch);

                        if ((Arrays.asList(op_btns).contains(v) && (last_fun || last_op)) || (Arrays.asList(fun_btns).contains(v) && ((!last_op && !last_fun) || last_dot))) {
                            return;
                        }
                    } else {
                        if (Arrays.asList(op_btns).contains(v)) {
                            return;
                        }
                    }

                    switch (v.getId()) {
                        case R.id.button_dot:
                            if (numbox.length() > 0 && numbox.getText().charAt(numbox.length() - 1) == ')') {
                                return;
                            }

                            for (int i = numbox.length() - 1; i > -1; i--) {
                                char cch = numbox.getText().charAt(i);
                                if (isFunction(cch) || isOperator(cch)) {
                                    break;
                                }
                                if (cch == '.') {
                                    return;
                                }
                            }

                            numbox.append(".");
                            break;
                        case R.id.button_rbracket:
                            if (pend_brack <= 0 || last_fun || last_op) break;
                            numbox.append(((AppCompatButton) v).getText());
                            break;
                        default:
                            if (numbox.length() > 0 && numbox.getText().charAt(numbox.length() - 1) == ')' && !Arrays.asList(op_btns).contains(v)) {
                                return;
                            }
                            if (v.getId() == R.id.button_plusminus) {
                                numbox.append("-");
                            } else {
                                numbox.append(((AppCompatButton) v).getText());
                            }
                            break;
                    }
                }
            });
        }

        for (AppCompatButton btn : cmd_btns) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.button_equals:
                            Queue<String> rs = null;
                            String result;
                            try {
                                rs = evaluate(numbox.getText().toString());
                            } catch (Exception ex) {
                                Toast.makeText(MainActivity.this, "Error parsing the equation!\nPlease check if you have the correct expression.", Toast.LENGTH_LONG).show();
                                ex.printStackTrace();
                            }
                            try {
                                assert rs != null;
                                result = calculate(rs);
                                Toast.makeText(MainActivity.this, "Result: " + result, Toast.LENGTH_LONG).show();
                            } catch (Exception ex) {
                                Toast.makeText(MainActivity.this, "Error calculating the equation!\nThis generally happens when one of the values is out of range of double.", Toast.LENGTH_LONG).show();
                                ex.printStackTrace();
                            }
                            break;
                        case R.id.button_percent:
                            break;
                        case R.id.button_ac:
                            numbox.getText().clear();
                            break;
                        case R.id.button_del:
                            if (numbox.length() > 0) {
                                numbox.getText().delete(numbox.length() - 1, numbox.length());
                            }
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    private void initViews() {
        _1 = findViewById(R.id.button1);
        _2 = findViewById(R.id.button2);
        _3 = findViewById(R.id.button3);
        _4 = findViewById(R.id.button4);
        _5 = findViewById(R.id.button5);
        _6 = findViewById(R.id.button6);
        _7 = findViewById(R.id.button7);
        _8 = findViewById(R.id.button8);
        _9 = findViewById(R.id.button9);
        _0 = findViewById(R.id.button0);
        plus = findViewById(R.id.button_plus);
        minus = findViewById(R.id.button_minus);
        divide = findViewById(R.id.button_divide);
        multiply = findViewById(R.id.button_multiply);
        point = findViewById(R.id.button_dot);
        equals = findViewById(R.id.button_equals);
        ac = findViewById(R.id.button_ac);
        del = findViewById(R.id.button_del);
        lbracket = findViewById(R.id.button_lbracket);
        rbracket = findViewById(R.id.button_rbracket);
        plusminus = findViewById(R.id.button_plusminus);
        sqroot = findViewById(R.id.button_sqroot);
        curoot = findViewById(R.id.button_curoot);
        power = findViewById(R.id.button_power);
        percent = findViewById(R.id.button_percent);

        numbox = findViewById(R.id.numBox);

        char_btns = new AppCompatButton[]{_1, _2, _3, _4, _5, _6, _7, _8, _9, _0, point, plus, minus, divide, multiply, lbracket, rbracket, power, plusminus, sqroot, curoot};
        num_btns = new AppCompatButton[]{_1, _2, _3, _4, _5, _6, _7, _8, _9, _0, point, rbracket};
        op_btns = new AppCompatButton[]{plus, minus, divide, multiply, power};
        fun_btns = new AppCompatButton[]{lbracket, sqroot, curoot, plusminus};
        cmd_btns = new AppCompatButton[]{equals, ac, del, percent};
    }

    int precedence(char ch) {
        if (isFunction(ch) && ch != '(') {
            return 7;
        } else if (isOperator(ch)) {
            switch (ch) {
                case '^':
                    return 5;
                case '÷':
                case '×':
                    return 4;
                case '−':
                case '+':
                    return 2;
                default:
                    break;
            }
        }
        return -1;
    }

    boolean isLeftAssociative(char ch) {
        return ch != '^';
    }

    private Queue<String> evaluate(String expr) {
        ListIterator<String> tokens = parseTokens(expr).listIterator();
        Queue<String> output = new LinkedList<>();
        Stack<String> op_stack = new Stack<>();
        while (tokens.hasNext()) {
            String token = tokens.next();
            char ch = token.charAt(0);
            if (isNumber(ch)) {
                output.add(token);
            }
            if (isFunction(ch)) {
                op_stack.push(token); // also '('
            }
            if (isOperator(ch)) {
                if (!op_stack.empty()) {
                    char che = op_stack.peek().charAt(0);
                    while (che != '(' && (isFunction(che) || precedence(che) > precedence(ch) ||
                            (precedence(ch) == precedence(che) && isLeftAssociative(che))) && !op_stack.empty()) {
                        output.add(op_stack.pop());
                        if (op_stack.empty()) {
                            break;
                        } else {
                            che = op_stack.peek().charAt(0);
                        }
                    }
                }
                op_stack.push(ch + "");
            }
            if (ch == ')') {
                if (!op_stack.empty()) {
                    while (!op_stack.peek().equals("(")) {
                        output.add(op_stack.pop());
                    }
                    op_stack.pop();
                }
            }
        }
        if (!tokens.hasNext()) {
            while (!op_stack.empty()) {
                output.add(op_stack.pop());
            }
        }
        return output;
    }
/*
    private int charType(char ch) {
        if (isFunction(ch) || ch == ')' || ch == '-') {
            return 0;
        } else if (isOperator(ch)) {
            return 1;
        } else if (isNumber(ch)) {
            return 2;
        }
        return −1;
    }*/

    private String operate(char operator, String operand1, String operand2) {
        switch (operator) {
            case '+':
                return doubleToString(Double.valueOf(operand1) + Double.valueOf(operand2));
            case '−':
                return doubleToString(Double.valueOf(operand1) - Double.valueOf(operand2));
            case '÷':
                return doubleToString(Double.valueOf(operand1) / Double.valueOf(operand2));
            case '×':
                return doubleToString(Double.valueOf(operand1) * Double.valueOf(operand2));
            case '^':
                return doubleToString(Math.pow(Double.valueOf(operand1), Double.valueOf(operand2)));
        }
        return null;
    }

    private String doubleToString(double param) {
        return BigDecimal.valueOf(param).toPlainString();
    }

    private String fun(char func, String param) {
        switch (func) {
            case '√':
                return doubleToString(Math.sqrt(Double.valueOf(param)));
            case '∛':
                return doubleToString(Math.cbrt(Double.valueOf(param)));
            case '-':
                System.out.println("FUCK HERE------------" + func + " " + param);
                return doubleToString(-Double.valueOf(param));
        }
        return null;
    }

    private String calculate(Queue<String> postfix) {
        // String res = "";

        Stack<String> operands = new Stack<>();
        for (String it : postfix) {
            if (isNumber(it.charAt(0))) {
                operands.push(it);
            }
            if (isOperator(it.charAt(0))) {
                String b = operands.pop();
                String a = operands.pop();

                operands.push(operate(it.charAt(0), a, b));
            }
            if (isFunction(it.charAt(0))) {
                String val = operands.pop();
                operands.push(fun(it.charAt(0), val));
            }
        }
        return operands.pop();
    }

    private List<String> parseTokens(String expr) {
        List<String> tokens = new ArrayList<>();

        boolean wasnum = false;

        for (int i = 0; i < expr.length(); i++) {
            char ch = expr.charAt(i);
            if (wasnum && isNumber(ch)) {
                int li = tokens.size() - 1;
                tokens.set(li, tokens.get(li) + ch);
            } else {
                tokens.add(ch + "");
            }
            wasnum = isNumber(ch);
        }
        return tokens;
    }
}
