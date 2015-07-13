package com.studiocodeworks.t0_p1.quickcalc;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity implements View.OnClickListener
{
    private static final int NONE       = 0;
    private static final int ADD        = 1;
    private static final int SUBTRACT   = 2;
    private static final int MULTIPLY   = 3;
    private static final int DIVIDE     = 4;

    private boolean m_replaceValue      = false;
    private int     m_operation         = NONE;
    private double  m_firstValue        = 0.0;
    private double  m_secondValue       = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up button listeners, route all clicks here
        Button oneButton        = (Button)findViewById(R.id.button_1);
        Button twoButton        = (Button)findViewById(R.id.button_2);
        Button threeButton      = (Button)findViewById(R.id.button_3);
        Button fourButton       = (Button)findViewById(R.id.button_4);
        Button fiveButton       = (Button)findViewById(R.id.button_5);
        Button sixButton        = (Button)findViewById(R.id.button_6);
        Button sevenButton      = (Button)findViewById(R.id.button_7);
        Button eightButton      = (Button)findViewById(R.id.button_8);
        Button nineButton       = (Button)findViewById(R.id.button_9);
        Button zeroButton       = (Button)findViewById(R.id.button_0);

        Button clearButton      = (Button)findViewById(R.id.button_clear);
        Button decimalButton    = (Button)findViewById(R.id.button_decimal);
        Button equalsButton     = (Button)findViewById(R.id.button_equals);
        Button negateButton     = (Button)findViewById(R.id.button_negate);
        Button percentButton    = (Button)findViewById(R.id.button_percent);

        Button addButton        = (Button)findViewById(R.id.button_add);
        Button subtractButton   = (Button)findViewById(R.id.button_subtract);
        Button multiplyButton   = (Button)findViewById(R.id.button_multiply);
        Button divideButton     = (Button)findViewById(R.id.button_divide);

        // set the onclick listeners
        oneButton.setOnClickListener(this);
        twoButton.setOnClickListener(this);
        threeButton.setOnClickListener(this);
        fourButton.setOnClickListener(this);
        fiveButton.setOnClickListener(this);
        sixButton.setOnClickListener(this);
        sevenButton.setOnClickListener(this);
        eightButton.setOnClickListener(this);
        nineButton.setOnClickListener(this);
        zeroButton.setOnClickListener(this);
        // operations
        clearButton.setOnClickListener(this);
        decimalButton.setOnClickListener(this);
        equalsButton.setOnClickListener(this);
        negateButton.setOnClickListener(this);
        percentButton.setOnClickListener(this);

        addButton.setOnClickListener(this);
        subtractButton.setOnClickListener(this);
        multiplyButton.setOnClickListener(this);
        divideButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v)
    {
        // group the button clicks together based upon
        // whether it is a numeric button
        // math op
        // expression op
        switch(v.getId())
        {
            case R.id.button_0:
            case R.id.button_1:
            case R.id.button_2:
            case R.id.button_3:
            case R.id.button_4:
            case R.id.button_5:
            case R.id.button_6:
            case R.id.button_7:
            case R.id.button_8:
            case R.id.button_9:
                // get the button object
                Button numericButton = (Button)findViewById(v.getId());
                // pull out the title of the button
                // it's the string representation of the button we
                // want to display on the screen
                String numberAsString = numericButton.getText().toString();
                // update the display
                updateCalcDisplay(numberAsString);
                break;

            // math ops
            case R.id.button_add:
            case R.id.button_subtract:
            case R.id.button_multiply:
            case R.id.button_divide:
                mathOperationButtonTapped(v.getId());
                break;

            // expression ops
            case R.id.button_clear:
            case R.id.button_negate:
            case R.id.button_percent:
                expressionButtonTapped(v.getId());
                break;

            case R.id.button_decimal:
                EditText    expressionTextField = (EditText)findViewById(R.id.edittext_expression);
                String      expressionText      = expressionTextField.getText().toString();

                // check to see if there's a decimal there
                // if not add one
                if(!expressionText.contains("."))
                {
                    String text = expressionText + ".";
                    expressionTextField.setText(text);
                }
                break;

            case R.id.button_equals:
                equalsButtonTapped();
                break;

            default:
                break;
        }
    }

    // implementation
    private void equalsButtonTapped()
    {
        EditText    expressionTextField = (EditText)findViewById(R.id.edittext_expression);

        // set the first and second values
        setExpressionValue();
        // compute
        if(m_operation != NONE)
        {
            double result = 0.0;

            // check for potential divide by zero
            if(m_operation == DIVIDE && m_secondValue == 0.0)
            {
                // the user tried to divide by zero
                expressionTextField.setText("NaN");
            }
            else
            {
                switch(m_operation)
                {
                    case ADD:
                        result = m_firstValue + m_secondValue;
                        break;

                    case SUBTRACT:
                        result = m_firstValue - m_secondValue;
                        break;

                    case MULTIPLY:
                        result = m_firstValue * m_secondValue;
                        break;

                    case DIVIDE:
                        result = m_firstValue / m_secondValue;
                        break;

                    default:
                        break;
                }

                try
                {
                    // convert the number to a string
                    // and set the text field
                    String numberAsText = Double.toString(result);
                    expressionTextField.setText(numberAsText);
                    m_replaceValue  = true;
                    m_operation     = NONE;
                }
                catch(Exception ex)
                {
                    Log.e("MainActivity", "An error occurred while converting string, Error: " + ex);
                }
            }
        }
    }

    private  void expressionButtonTapped(int buttonId)
    {
        EditText    expressionTextField = (EditText)findViewById(R.id.edittext_expression);

        switch(buttonId)
        {
            case R.id.button_clear:
                m_operation                 = NONE;
                m_replaceValue              = false;
                expressionTextField.setText("0");
                break;

            case R.id.button_negate:
                // either prepend or remove a beginning minus sign
                // then
                // flip the negated switch
                // do this using numerical ops

                if(expressionTextField.getText().length() > 0)
                {
                    double value = 0.0;

                    try
                    {
                        String expressionText = expressionTextField.getText().toString();
                        value = Double.parseDouble(expressionText);

                        if(Math.abs(value) > 0.0)
                        {
                            // flip the sign of the value
                            value = -(value);
                            // convert the number to a string
                            String numberAsText = Double.toString(value);
                            expressionTextField.setText(numberAsText);
                        }
                    }
                    catch (Exception ex)
                    {
                        Log.e("MainActivity", "An error occurred during string/number conversion, Error: " + ex);
                    }
                    finally
                    {

                    }
                }
                break;

            case R.id.button_percent:
                // take whatever is on the screen and divide it by 100
                // it doesn't matter if the first or second expression
                // is filled in, ops will fill in the appropriate value
                if(expressionTextField.getText().length() > 0)
                {
                    double value = 0.0;

                    try
                    {
                        String expressionText = expressionTextField.getText().toString();
                        value = Double.parseDouble(expressionText);
                        // convert to a percentage value
                        value = value / 100.0;

                        // convert the number to a string
                        String numberAsText = Double.toString(value);
                        expressionTextField.setText(numberAsText);
                    }
                    catch (Exception ex)
                    {
                        Log.e("MainActivity", "An error occurred during string/number conversion, Error: " + ex);
                    }
                    finally
                    {

                    }
                }
                break;

            default:
                break;
        }
    }

    private boolean isZeroDisplayed()
    {
        boolean     result              = false;
        EditText    expressionTextField = (EditText)findViewById(R.id.edittext_expression);
        String      expressionText      = expressionTextField.getText().toString();


        if(expressionText.length() == 1)
        {
            if(expressionText.compareTo("0") == 0)
            {
                result = true;
            }
        }

        return  result;
    }

    private void mathOperationButtonTapped(int buttonId)
    {
        if(m_operation != NONE)
        {
            // the user entered in an expression, but
            // equals wasn't hit yet
            // trigger it ourselves then continue processing
            equalsButtonTapped();
        }

        setExpressionValue();
        // make sure the value is replaced on the next number keypress
        m_replaceValue  = true;

        // set the appropriate value based upon the button
        // that was tapped
        switch (buttonId)
        {
            case R.id.button_add:
                m_operation     = ADD;
                break;

            case R.id.button_subtract:
                m_operation     = SUBTRACT;
                break;

            case R.id.button_multiply:
                m_operation     = MULTIPLY;
                break;

            case R.id.button_divide:
                m_operation     = DIVIDE;
                break;

            default:
                break;
        }
    }

    private void setExpressionValue()
    {
        // set the first and second value in our expression
        // if the user hasn't tapped an operation button
        // set the first expression, else the second
        EditText expressionTextField = (EditText)findViewById(R.id.edittext_expression);
        String expressionText = expressionTextField.getText().toString();
        // we need to convert the string displayed in the edit text to a number
        double expressionValue = 0.0;
        // wrap the conversion in a try/catch block so that we don't crash the application
        // java doesn't have a direct string-to-number conversion from the object
        // but we can use a class method
        // ask questions about this block, it's new

        try
        {
            expressionValue = Double.parseDouble(expressionText);
        }
        catch(Exception ex)
        {
            // if an error occurred, log it to logcat so we can find out what
            // happened, log the value we attempted to parse, plus the exception
            // this information will help out with debugging
            Log.e("MainActivity",   "An error occurred while converting expression text: " +
                                    expressionText + ", Error: " + ex);
        }
        finally
        {
            // finally, after the conversion set the values appropriately
            if(m_operation == NONE)
            {
                m_firstValue = expressionValue;
            }
            else
            {
                m_secondValue = expressionValue;
            }
        }
    }

    private void updateCalcDisplay(String value)
    {
        EditText    expressionTextField = (EditText)findViewById(R.id.edittext_expression);
        String      expressionText      = expressionTextField.getText().toString();

        // check for zero, or potential division by zero by some fool
        if(m_operation == NONE)
        {
            if(m_replaceValue)
            {   // if the flag is set to replace what's on the screen do that here
                // flip the flag back
                expressionTextField.setText(value);
                m_replaceValue = false;
            }
            else if(isZeroDisplayed() || expressionText.compareToIgnoreCase("NaN") == 0)
            {
                // replace what's there
                expressionTextField.setText(value);
            }
            else
            {
                // append what's there
                String text = expressionText + value;
                // set the value back
                expressionTextField.setText(text);
            }
        }
        else
        {   // is this else block really necessary, could this be written in one single block?
            if(m_replaceValue)
            {
                expressionTextField.setText(value);
                m_replaceValue = false;
            }
            else
            {
                // append what's there
                String text = expressionText + value;
                // set the value back
                expressionTextField.setText(text);
            }
        }
    }
}
