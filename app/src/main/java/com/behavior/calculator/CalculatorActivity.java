package com.behavior.calculator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.javia.arity.Symbols;
import org.javia.arity.SyntaxException;

import me.grantland.widget.AutofitTextView;

/**
 * @author Lix
 * @date 2020-04-15
 */
public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "Calculator";
    /**
     * 功能按键
     * AC
     * ±
     * %
     */
    private TextView ac;
    private TextView negative;
    private TextView percentage;
    /**
     * 数字键盘
     * 9
     * 8
     * 7
     * 6
     * 5
     * 4
     * 3
     * 2
     * 1
     * 0
     */
    private TextView nine;
    private TextView eight;
    private TextView seven;
    private TextView six;
    private TextView five;
    private TextView four;
    private TextView three;
    private TextView two;
    private TextView one;
    private TextView zero;
    private TextView point;
    /**
     * 算数符号键盘
     * ÷
     * *
     * +
     * -
     * =
     */
    private TextView except;
    private TextView ride;
    private TextView reduce;
    private TextView plus;
    private TextView equal;
    /**
     * 历史记录
     */
    private RecyclerView mRecyclerView;
    private SimpleRvAdapter mSimpleRvAdapter;
    /**
     * 计算TextView
     */
    private AutofitTextView mAutofitTextView;

    //已经输入的字符
    private String existedText = "";
    //是否计算过
    private boolean isCounted = false;
    //是否计算过
    private boolean isCount = false;
    //运算符
    private String operator;
    //计算结果
    private Object mResult = null;
    private Symbols symbols = new Symbols();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        findViewByIds();
        initView();
        initRecycle();
        initListener();
    }

    private void findViewByIds() {
        mAutofitTextView = findViewById(R.id.auto_fit);
        mRecyclerView = findViewById(R.id.recycler_view);
        ac = findViewById(R.id.ac);
        negative = findViewById(R.id.negative);
        percentage = findViewById(R.id.percentage);
        nine = findViewById(R.id.nine);
        eight = findViewById(R.id.eight);
        seven = findViewById(R.id.seven);
        six = findViewById(R.id.six);
        five = findViewById(R.id.five);
        four = findViewById(R.id.four);
        three = findViewById(R.id.three);
        two = findViewById(R.id.two);
        one = findViewById(R.id.one);
        zero = findViewById(R.id.zero);
        point = findViewById(R.id.point);
        except = findViewById(R.id.except);
        ride = findViewById(R.id.ride);
        reduce = findViewById(R.id.reduce);
        plus = findViewById(R.id.plus);
        equal = findViewById(R.id.equal);
    }

    /**
     * titleView
     */
    private void initView() {
        mAutofitTextView.setMaxLines(1);
        mAutofitTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mAutofitTextView.setSingleLine();
        mAutofitTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    /**
     * recycle历史记录
     * 待完善，这版没有加入这个功能，有需要可以自行添加
     */
    private void initRecycle() {
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        layout.setReverseLayout(true);//列表翻转
        mRecyclerView.setLayoutManager(layout);

        mSimpleRvAdapter = new SimpleRvAdapter(this);
        mRecyclerView.setAdapter(mSimpleRvAdapter);
    }

    /**
     * 绑定事件
     */
    private void initListener() {
        ac.setOnClickListener(this);
        negative.setOnClickListener(this);
        percentage.setOnClickListener(this);
        nine.setOnClickListener(this);
        eight.setOnClickListener(this);
        seven.setOnClickListener(this);
        six.setOnClickListener(this);
        five.setOnClickListener(this);
        four.setOnClickListener(this);
        three.setOnClickListener(this);
        two.setOnClickListener(this);
        one.setOnClickListener(this);
        zero.setOnClickListener(this);
        point.setOnClickListener(this);
        except.setOnClickListener(this);
        ride.setOnClickListener(this);
        reduce.setOnClickListener(this);
        plus.setOnClickListener(this);
        equal.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ac://清空
                clear();
                break;
            case R.id.negative://正负
                negative();
                break;
            case R.id.zero://0
                existedText = isOverRange(existedText, "0");
                break;
            case R.id.one://1
                existedText = isOverRange(existedText, "1");
                break;
            case R.id.two://2
                existedText = isOverRange(existedText, "2");
                break;
            case R.id.three://3
                existedText = isOverRange(existedText, "3");
                break;
            case R.id.four://4
                existedText = isOverRange(existedText, "4");
                break;
            case R.id.five://5
                existedText = isOverRange(existedText, "5");
                break;
            case R.id.six://6
                existedText = isOverRange(existedText, "6");
                break;
            case R.id.seven://7
                existedText = isOverRange(existedText, "7");
                break;
            case R.id.eight://8
                existedText = isOverRange(existedText, "8");
                break;
            case R.id.nine://9
                existedText = isOverRange(existedText, "9");
                break;
            case R.id.point://小数点
                point();
                break;
            case R.id.percentage://百分号
                existedText = isOverRange(existedText, "%");
                break;
            case R.id.except://除
                operator(3);
                break;
            case R.id.ride://乘
                operator(2);
                break;
            case R.id.reduce://减
                operator(1);
                break;
            case R.id.plus://加
                operator(0);
                break;
            case R.id.equal://等于，计算结果
                executeExpression();
                break;
        }
        mAutofitTextView.setText(existedText);
    }

    /**
     * 判断是否运算过
     * 否
     * 判断是否有运算符，有 判断运算符之后的数字，无 判断整个数字
     * 判断数字是否过长，是则不能添加小数点，否则可以添加
     * 判断已经存在的数字里是否有小数点
     * 是
     * 字符串置为 0.
     */
    private void point() {
        if (!isCounted) {
            if (!TextUtils.isEmpty(existedText) && !"ERROR".equals(existedText)) {
                if (existedText.contains("+") || existedText.contains("-") ||
                        existedText.contains("×") || existedText.contains("÷")) {
                    String param2 = "";
                    if (existedText.contains("+")) {
                        param2 = existedText.substring(existedText.indexOf("+") + 1);
                    } else if (existedText.contains("-")) {
                        param2 = existedText.substring(existedText.indexOf("-") + 1);
                    } else if (existedText.contains("×")) {
                        param2 = existedText.substring(existedText.indexOf("×") + 1);
                    } else if (existedText.contains("÷")) {
                        param2 = existedText.substring(existedText.indexOf("÷") + 1);
                    }
                    boolean isContainedDot = param2.contains(".");
                    if (!isContainedDot) {
                        if (param2.equals("")) {
                            existedText += "0.";
                        } else {
                            existedText += ".";
                        }
                    }
                } else {
                    boolean isContainedDot = existedText.contains(".");
                    if (!isContainedDot) {
                        existedText += ".";
                    }
                }
            } else {
                existedText = "0.";
            }
        } else {
            existedText = "0.";
        }
    }

    /**
     * 先判断是否计算过
     * 是 按数字则显示当前数字
     * 否 在已有的表达式后添加字符
     *
     * @param existedText
     * @param s
     * @return
     */
    private String isOverRange(String existedText, String s) {
        /**
         * 判断是否计算过
         */
        if (!isCounted) {
            if (existedText.contains("e")) {
                existedText = "0";
            } else if (existedText.equals("0")) {
                existedText = "";
            } else if ("ERROR".equals(existedText)) {
                existedText = "";
            }
            if (TextUtils.isEmpty(existedText)) {
                if (!s.equals("%")) {
                    existedText += s;
                }
            } else {
                existedText += s;
            }
            isCounted = false;
            isCount = false;
        } else {
            if (existedText.contains("e")) {
                existedText = "0";
            } else if (existedText.equals("0")) {
                existedText = "";
            } else if ("ERROR".equals(existedText)) {
                existedText = "";
            }
            if (isCount) {
                if (!s.equals("%")) {
                    existedText = s;
                }
            } else {
                existedText += s;
            }
            isCount = false;
            isCounted = false;
        }
        return existedText;
    }

    /**
     * 添加运算符
     *
     * @param index
     */
    private void operator(int index) {
        if (!TextUtils.isEmpty(existedText)) {
            isCount = false;
            isCounted = false;
            if (!"ERROR".equals(existedText)) {
                switch (index) {
                    case 0:
                        operator = "+";
                        if ((existedText.substring(existedText.length() - 1)).equals("-")) {
                            existedText = existedText.substring(0, existedText.length() - 1) + "+";
                        } else if ((existedText.substring(existedText.length() - 1)).equals("×")) {
                            existedText = existedText.substring(0, existedText.length() - 1) + "+";
                        } else if ((existedText.substring(existedText.length() - 1)).equals("÷")) {
                            existedText = existedText.substring(0, existedText.length() - 1) + "+";
                        } else if (!(existedText.substring(existedText.length() - 1)).equals("+")) {
                            existedText += "+";
                        }
                        break;
                    case 1:
                        operator = "-";
                        if ((existedText.substring(existedText.length() - 1)).equals("+")) {
                            existedText = existedText.substring(0, existedText.length() - 1) + "-";
                        } else if ((existedText.substring(existedText.length() - 1)).equals("×")) {
                            existedText = existedText.substring(0, existedText.length() - 1) + "-";
                        } else if ((existedText.substring(existedText.length() - 1)).equals("÷")) {
                            existedText = existedText.substring(0, existedText.length() - 1) + "-";
                        } else if (!(existedText.substring(existedText.length() - 1)).equals("-")) {
                            existedText += "-";
                        }
                        break;
                    case 2:
                        operator = "×";
                        if ((existedText.substring(existedText.length() - 1)).equals("+")) {
                            existedText = existedText.substring(0, existedText.length() - 1) + "×";
                        } else if ((existedText.substring(existedText.length() - 1)).equals("-")) {
                            existedText = existedText.substring(0, existedText.length() - 1) + "×";
                        } else if ((existedText.substring(existedText.length() - 1)).equals("÷")) {
                            existedText = existedText.substring(0, existedText.length() - 1) + "×";
                        } else if (!(existedText.substring(existedText.length() - 1)).equals("×")) {
                            existedText += "×";
                        }
                        break;
                    case 3:
                        operator = "÷";
                        if ((existedText.substring(existedText.length() - 1)).equals("+")) {
                            existedText = existedText.substring(0, existedText.length() - 1) + "÷";
                        } else if ((existedText.substring(existedText.length() - 1)).equals("-")) {
                            existedText = existedText.substring(0, existedText.length() - 1) + "÷";
                        } else if ((existedText.substring(existedText.length() - 1)).equals("×")) {
                            existedText = existedText.substring(0, existedText.length() - 1) + "÷";
                        } else if (!(existedText.substring(existedText.length() - 1)).equals("÷")) {
                            existedText += "÷";
                        }
                        break;
                }
            }
        }
    }

    /**
     * 判断数字是否有运算符
     * 是 不做任何操作
     * 否 进行下一步
     * <p>
     * 判断数字是否是 0
     * 是 不做任何操作
     * 否 进行乘-1
     * <p>
     * 将字符串转换成double类型，进行运算后，再转换成String型
     */

    private void negative() {
        int last = -1;
        String s;
        String s1;
        if (!TextUtils.isEmpty(operator)) {
            last = existedText.lastIndexOf(operator);
            Log.e(TAG + "--last", last + "");
        }
        if (!isCounted) {
            if (!TextUtils.isEmpty(existedText)) {
                if (!"ERROR".equals(existedText)) {
                    if (!existedText.equals("0")) {
                        if (last > 0) {
                            s1 = existedText.substring(0, last);
                            s = existedText.substring(last + 1, existedText.length());
                            if (!TextUtils.isEmpty(s)) {
                                if (!s.equals("0")) {
                                    if (existedText.contains("%")) {
                                        try {
                                            double temp = symbols.eval(existedText);
                                            temp = temp * -1;
                                            existedText = subZeroAndDot(String.valueOf(temp));
                                        } catch (SyntaxException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        double temp = Double.parseDouble(s);
                                        temp = temp * -1;
                                        existedText = s1 + operator + subZeroAndDot(String.valueOf(temp));
                                    }
                                }
                            }
                        } else {
                            if (existedText.contains("%")) {
                                try {
                                    double temp = symbols.eval(existedText);
                                    temp = temp * -1;
                                    existedText = subZeroAndDot(String.valueOf(temp));
                                } catch (SyntaxException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                double temp = Double.parseDouble(existedText);
                                temp = temp * -1;
                                existedText = subZeroAndDot(String.valueOf(temp));
                            }
                        }
                    }
                }
            }
        } else {
            mAutofitTextView.setText("0");
        }
    }


    /**
     * 执行 待计算表达式,当用户按下 = 号时,调用这个方法
     */
    private void executeExpression() {
        try {
            mResult = symbols.eval(existedText);
            isCounted = true;
            isCount = true;
            Log.e(TAG + "result", mResult.toString());
        } catch (SyntaxException e) {
            /**
             * 如果捕获到异常,表示表达式执行失败,
             * 这里设置为false是因为并没有执行成功,还不能新的表达式求值
             */
            e.printStackTrace();
            isCounted = false;
            isCount = false;
            if (!TextUtils.isEmpty(existedText)) {
                existedText = "ERROR";
            }
            return;
        }
        /**
         * 将计算得到的结果进行处理,
         * 去掉多余的.与0
         */
        mResult = subZeroAndDot(mResult.toString());
        existedText = mResult.toString();
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s 传入的字符串
     * @return 修改之后的字符串
     */
    public String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 如果按下 清除键,就把
     */
    private void clear() {
        if (!TextUtils.isEmpty(existedText)) {
            existedText = "";
        }
        isCount = false;
        isCounted = false;
        mAutofitTextView.setText("");
    }
}
