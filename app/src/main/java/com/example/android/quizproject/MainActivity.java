package com.example.android.quizproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public String finalCallResult = " ";
    boolean intermittent = false; //result of question 1
    boolean router = false; //result of question 2 //whether or not router is present
    boolean routerReset = false; //All lights not ok, reset
    boolean modemReset = false;//Modem lights not ok, reset

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Spinner code for question 2
        //Create the Spinner
        Spinner spinner = findViewById(R.id.spinner);

        //Create the click listener
        spinner.setOnItemSelectedListener(this);

        //Populate spinner drop down elements
        List<String> categories = new ArrayList<>();
        categories.add("I am not sure");
        categories.add("Yes");
        categories.add("No");

        //Create adapter for spinner object
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);

        //Define style for drop down - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attach data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }


    //Visibility Section
    //Show the spinner for selection of router or not.
    public void routerParent() {
        LinearLayout routerPresent = findViewById(R.id.routerPresent);
        routerPresent.setVisibility(VISIBLE);
    }

    //Show check boxes for for router light check
    public void routerPresentCheck() {
        LinearLayout routerLightCheck = findViewById(R.id.router_parent);
        routerLightCheck.setVisibility(VISIBLE);
    }

    //Show the check boxes for modem light check
    public void modemPresentCheck() {
        LinearLayout modemLightCheck = findViewById(R.id.modem_parent);
        modemLightCheck.setVisibility(VISIBLE);
    }

    //Show the remove power check box to start the reset process for router.
    public void routerResetStart() {
        LinearLayout routerPowerOut = findViewById(R.id.router_1);
        routerPowerOut.setVisibility(VISIBLE);
    }

    //Show the remove power check box to start the reset process for modem.
    public void modemResetStart() {//set to run if modem check has invalid light pattern.
        LinearLayout modemPowerOut = findViewById(R.id.modem_1);
        modemPowerOut.setVisibility(VISIBLE);
    }

    //Show the results radio buttons for connectionGood or connectionBad
    public void resultsSelector() {
        LinearLayout results = findViewById(R.id.results);
        results.setVisibility(VISIBLE);
    }

    //Generates the final result text and sets it to visible
    public void result() {
        TextView resultsText = findViewById(R.id.resultsFinal);
        resultsText.setText(getString(R.string.u_should) + finalCallResult + getString(R.string.call_company));
        resultsText.setVisibility(VISIBLE);
    }

    //Router light check with assignment of reset if all good or power cycle fixes it.
    private void routerCheck() {

        CheckBox routerPower = findViewById(R.id.routerPower);
        boolean routerPowerCheck = routerPower.isChecked();

        CheckBox routerInternet = findViewById(R.id.routerInternet);
        boolean routerInternetCheck = routerInternet.isChecked();

        CheckBox routerWifi = findViewById(R.id.routerWifi);
        boolean routerWifiCheck = routerWifi.isChecked();

        if (routerInternetCheck && routerPowerCheck && routerWifiCheck) {
            //set the router check result to good.
            routerReset = true;  //valid light pattern means no reset needed.
        } else {
            routerResetStart();
        }
    }

    //Modem light check with assignment of reset if all good or power cycle fixes it.
    private void modemCheck() {
        CheckBox modemPower = findViewById(R.id.modemPower);
        boolean modemPowerCheck = modemPower.isChecked();

        CheckBox modemSignal = findViewById(R.id.modemSignal);
        boolean modemSignalCheck = modemSignal.isChecked();

        CheckBox modemInternet = findViewById(R.id.modemInternet);
        boolean modemInternetCheck = modemInternet.isChecked();

        if (modemPowerCheck && modemSignalCheck && modemInternetCheck) {
            //set the modem check result to good.
            modemReset = true;
        } else {
            modemResetStart();
        }

    }

    //Show hidden messageField to deliver verdict of to call or not.
    public void showConnection() {
        LinearLayout results = findViewById(R.id.results);
        results.setVisibility(View.VISIBLE);
        TextView result = findViewById(R.id.resultsFinal);
        result.setText("");
    }

    //Hide the result of first round of resets to use later.
    public void hideConnection() {
        LinearLayout results = findViewById(R.id.results);
        results.setVisibility(View.GONE);
        TextView result = findViewById(R.id.resultsFinal);
        result.setText("");
    }


    //30 Sec timer that at completion is to show the step to insert power to router.
    public void thirtySecondsRouter() {
        final TextView mTextField = findViewById(R.id.mTextField);
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {

                mTextField.setText(getString(R.string.sec_rem) + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mTextField.setText(getString(R.string.done));
                LinearLayout routerPowerIn = findViewById(R.id.router_2);
                routerPowerIn.setVisibility(VISIBLE);
            }
        }.start();
    }

    //5 Min timer that at completion is to show the step to check the lights on router.
    public void fiveMinutesRouter() {
        final TextView mTextField2 = findViewById(R.id.mTextField2);
        new CountDownTimer(300000, 1000) {

            public void onTick(long millisUntilFinished) {

                mTextField2.setText(getString(R.string.sec_rem) + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mTextField2.setText(getString(R.string.done));
                routerReset = true;
                LinearLayout routerCheck = findViewById(R.id.router_3);
                routerCheck.setVisibility(VISIBLE); //show the router verification view boolean lights ok check.
                //use radiobutton selector to move forward.
                //need to implement final result check in onRadioButton selector

            }
        }.start();
    }

    //30 Sec timer that at completion is to show the step to insert power to modem.
    public void thirtySecondsModem() {
        final TextView mTextField3 = findViewById(R.id.mTextField3);
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {

                mTextField3.setText(getString(R.string.sec_rem) + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mTextField3.setText(getString(R.string.done));
                LinearLayout modemPowerIn = findViewById(R.id.modem_2);
                modemPowerIn.setVisibility(VISIBLE);

            }
        }.start();
    }

    //5 min timer that at completion is to show the step to check the lights on modem.
    public void fiveMinutesModem() {
        final TextView mTextField4 = findViewById(R.id.mTextField4);
        new CountDownTimer(300000, 1000) {

            public void onTick(long millisUntilFinished) {

                mTextField4.setText(getString(R.string.sec_rem) + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mTextField4.setText(R.string.done);
                modemReset = true;
                LinearLayout modemReset = findViewById(R.id.modem_3);
                modemReset.setVisibility(VISIBLE);
                //need to implement final result check in onRadioButton selector
            }
        }.start();
    }

    //Spinner Question 2
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

        //Compare item selected value to set router variable.
        switch (item) {
            case "I am not sure":
                router = false;
                break;
            case "Yes":
                router = true;
                modemPresentCheck();
                break;
            case "No":
                router = false;
                //set modem light check to true
                modemPresentCheck();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Nothing to do here carry on.
    }

    //Button Handler to deal with clicking the next button.
    public void onNextClick(View view) {

        switch (view.getId()) {
            case R.id.modemLightCheck:
                modemCheck();
                if (modemReset) {
                    resultsSelector();
                }
                break;

            case R.id.routerLightCheck:
                routerCheck();
                if (routerReset)//True condition is all good for lights. No reset.
                {
                    resultsSelector();
                }
                break;

        }
    }

    //Button Handlers
    //Radio Buttons handles whole form using generic view with case select to id radio button.
    public void onRadioButtonClick(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.intermittentTrue:
                if (checked) {
                    intermittent = true;
                    routerParent();
                    break;
                }
            case R.id.intermittentFalse:
                if (checked) {
                    intermittent = false;
                    routerParent();
                    break;
                }

            case R.id.routerOk:
                if (checked) {

                    resultsSelector();
                    break;
                }


            case R.id.routerBad:
                if (checked) {

                    resultsSelector();
                }
                break;

            case R.id.modemOk:
                if (checked) {
                    resultsSelector();
                }
                break;

            case R.id.modemBad:
                if (checked) {
                    resultsSelector();
                }
                break;

            case R.id.connectionOk:
                if (checked && intermittent) { //If connection is intermittent call provider either way
                    finalCallResult = " ";
                    result();
                    Toast message = Toast.makeText(this, "You should call your Internet provider", Toast.LENGTH_LONG);
                    message.show();
                } else {
                    finalCallResult = " not ";
                    result();
                    Toast message = Toast.makeText(this, "You should not call your Internet provider", Toast.LENGTH_LONG);
                    message.show();
                }
                break;

            case R.id.connectionBad:
                if (checked) {//Bad connection
                    if (router) {//With Router
                        routerPresentCheck();
                        hideConnection();
                        if (routerReset) {//That refuses to connect.
                            showConnection();
                            finalCallResult = " definitely call ";
                            result();
                            Toast message = Toast.makeText(this, "You should definitely call your Internet provider", Toast.LENGTH_LONG);
                            message.show();

                        }

                    } else {
                        showConnection();
                        finalCallResult = " definitely call ";
                        result();
                        Toast message = Toast.makeText(this, "You should definitely call your Internet provider", Toast.LENGTH_LONG);
                        message.show();
                    }
                }

        }

    }


    //Pass in whole app view and use case select to id correct item on form.
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked handles all check boxes in form
        switch (view.getId()) {
            case R.id.routerPowerOut:
                if (checked)
                    // Start the 30 sec timer for PWC.
                    thirtySecondsRouter();
                // Move along
                break;
            case R.id.routerPowerIn:
                if (checked)
                    // Start the 5 min timer for PWC
                    fiveMinutesRouter();

                break;
            // Modem
            case R.id.modemPowerOut:
                if (checked)
                    //Start the 30 sec timer for PWC
                    thirtySecondsModem();
                break;
            case R.id.modemPowerIn:
                if (checked)
                    //Start the 5 min timer for PWC.
                    fiveMinutesModem();
                break;
        }
    }

    public void feedback(View view){
        EditText message = findViewById(R.id.feedbackbody);
        String contents = message.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SENDTO);  // Create the new intent and init it for an email action.
        intent.setType("text/plain");
        intent.setData(Uri.parse("mailto:"));  //only used by mail applications.
        intent.putExtra(Intent.EXTRA_EMAIL,new String[] { "lastditchdev@yahoo.com" });
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback about Should I call");  //The Subject Line.
        intent.putExtra(Intent.EXTRA_TEXT, contents);//Body of the email. Derived from the order summary section.
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}





