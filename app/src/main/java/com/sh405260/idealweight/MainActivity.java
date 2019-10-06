package com.sh405260.idealweight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    //format double into 1 decimal place
    DecimalFormat df = new DecimalFormat("#.#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    //calculate BMI
    public void calculateBMI(View view) throws ParseException {


        // close keyboard after the user click on calculate BMI button
        closeKeyboard();


        //connect views to variables
        EditText weightEditTex = (EditText) findViewById(R.id.weight_edit_box);
        EditText heightEditText = (EditText) findViewById(R.id.height_edit_box);

        TextView bmiStatus = (TextView) findViewById(R.id.result_status);
        TextView bmiResult = (TextView) findViewById(R.id.bmi_result);
        ImageView bmiImage = (ImageView) findViewById(R.id.bmi_result_image);

        //connect List information to views
        TextView weightTextView = (TextView) findViewById(R.id.weight);
        TextView heightTextView = (TextView) findViewById(R.id.height);
        TextView bmiTextView = (TextView) findViewById(R.id.bmi);
        TextView bmiStatusInListInformation = (TextView) findViewById(R.id.result);

        //connect ideal weight for you and conclusion to views
        TextView information1 = (TextView) findViewById(R.id.ideal_weight_textView1);
        TextView information2 = (TextView) findViewById(R.id.ideal_weight_textView2);
        TextView information3 = (TextView) findViewById(R.id.ideal_weight_textView3);
        TextView conclusion = (TextView) findViewById(R.id.ideal_weight_advice);



       // LinearLayout containerOfBmiResult = findViewById(R.id.container_mbi_result);
        LinearLayout containerOfBmiResult = findViewById(R.id.list_information);
        RelativeLayout containerOFIntroduction = findViewById(R.id.container_for_introduction);
        RelativeLayout containerForConclusion = findViewById(R.id.ideal_weight_conclusion_container);


        /* Validation: if the user press calculate BMI button and there is
         * no values in weigh and height, then the button does not do any action
         */
        String receiveWeight = weightEditTex.getText().toString(); //receive weight
        String receiveHeight = heightEditText.getText().toString(); //receive height

        if(receiveWeight.equals("")){
            weightEditTex.setError(getResources().getString(R.string.enter_weight_editText_error)); //show error massage
            return; //don't do any thing
        } else if (receiveHeight.equals("")) {
            heightEditText.setError(getResources().getString(R.string.enter_height_editText_error)); //show error massage
            return; //don't do any thing
        }


        //parse value of editText from String to double
        double weighInKG = Double.parseDouble(weightEditTex.getText().toString());
        double heightInCM = Double.parseDouble(heightEditText.getText().toString());

        //calculate bmi (receive height in CM and convert it to M)
        double heightInM = heightInCM /100;
        double bmi = weighInKG / (heightInM * heightInM);
        String bmiInStringAfterCutDecimalPlace = df.format(bmi); //format bmi into 1 decimal place (Output is string)
        // Note: when we format number to 1 decimal place, the number will be String type and will show in mobile
        // language . (ex: if mobile language is ENGLISH 17 and if mobile language is arabic will
        //show ١٧ ). so if we want to use this number for calculation, we have to change the number format from ARABIC
        //to English because calculation use english number format. then parse the number from String to int, float or double.


       //parse  bmi from string to double:
        //first format bmi into 1 decimal place and English locale format number, then parse it from string to double
        double bmiInDoubleAfterCutDecimalPlace = Double.parseDouble(String.format(Locale.ENGLISH, "%.1f", bmi));




        //print listInformation: (format weight and height to 1 decimal place and print them)
        weightTextView.setText(" " + df.format(weighInKG) + " " + getResources().getString(R.string.kg));
        heightTextView.setText(" " + df.format(heightInCM) + " " + getResources().getString(R.string.cm));
        bmiTextView.setText(" " + bmiInStringAfterCutDecimalPlace);


        //calculate (min ideal weight, max ideal weight, weight to loose , perfect ideal weight)
        double minIdealWeightDouble = 18.5 * (heightInM * heightInM); //min ideal weight
        double maxIdealWeightDouble = 24.9 * (heightInM * heightInM); //max ideal weight
        double weightToLooseDouble = weighInKG - maxIdealWeightDouble; //how many kg need to loose to be in ideal weight
        double weightToIncreaseDouble = minIdealWeightDouble - weighInKG; //how many kg need to increase to be in ideal weight
        double bestIdealWeightDouble = (minIdealWeightDouble + maxIdealWeightDouble) / 2;

        //format (min ideal weight, max ideal weight, weight to loose , perfect ideal weight) into 1 decimal place
        // also round the number
        //(decimalFormat.format() method: cut the decimal point and round the number)
        String minIdealWeight =df.format(minIdealWeightDouble) ;
        String maxIdealWeight = df.format(maxIdealWeightDouble) ;
        String weightToLoose = df.format(weightToLooseDouble) ;
        String weightToIncrease = df.format(weightToIncreaseDouble) ;
        String bestIdealWeight =  df.format(bestIdealWeightDouble) ;



        //print  information  for ideal weight:
        information1.setText(getResources().getString(R.string.ideal_weight1) + " " + minIdealWeight +" " + getResources().getString(R.string.kg) + " " + getResources().getString(R.string.ideal_weight11) + " " + maxIdealWeight + " " + getResources().getString(R.string.kg));
        information3.setText(getResources().getString(R.string.ideal_weight3) + " " + bestIdealWeight + " " + getResources().getString(R.string.kg));




        /*print the status and show the image, the status and image depends on the BMI result
         * There are are 4 status: Underweight, Normal, Overweight and Obese*/


        if(bmiInDoubleAfterCutDecimalPlace < 18.5){
            bmiStatus.setText(getResources().getString(R.string.underweight)); //change the status text
            bmiStatus.setTextColor(Color.RED); //set the status color
            bmiImage.setImageResource(R.drawable.under_weight); //change the status image

            //bmi Status In List Information
            bmiStatusInListInformation.setText(" " + getResources().getString(R.string.underweight)); //change the status text.(also create space at the beginning by " " )

            // information: how many kg need to increase to be in ideal weight
            information2.setText(getResources().getString(R.string.ideal_weight_Increase) + " " + weightToIncrease+ " " +  getResources().getString(R.string.kg));

            //conclusion: underweight advice
            conclusion.setText(getResources().getString(R.string.underweight_advice));

        } else if(bmiInDoubleAfterCutDecimalPlace < 25){
            bmiStatus.setText(getResources().getString(R.string.normal));
            bmiStatus.setTextColor(Color.parseColor("#008577")); //change the textColor by hex programmatically.
            bmiImage.setImageResource(R.drawable.normal);

            //bmi Status In List Information
            bmiStatusInListInformation.setText(" "+ getResources().getString(R.string.normal)); //change the status text. (also create space at the beginning by " " )

            // information:  Congratulations your weight is normal.
            information2.setText(getResources().getString(R.string.ideal_weight_normal));

            //conclusion: normal weight advice
            conclusion.setText(getResources().getString(R.string.normal_advice));


        } else if(bmiInDoubleAfterCutDecimalPlace < 30){
            bmiStatus.setText(getResources().getString(R.string.overweight));
            bmiStatus.setTextColor(Color.GRAY);
            bmiImage.setImageResource(R.drawable.over_weight);

            //bmi Status In List Information
            bmiStatusInListInformation.setText(" " + getResources().getString(R.string.overweight)); //change the status text. (also create space at the beginning by " " )

            // information: how many kg need to lose to be in ideal weight
            information2.setText(getResources().getString(R.string.ideal_weight_lose) + " " + weightToLoose+ " " +  getResources().getString(R.string.kg));

            //conclusion: overweight advice
            conclusion.setText(getResources().getString(R.string.overweight_advice));

        } else {
            bmiStatus.setText(getResources().getString(R.string.obese));
            bmiStatus.setTextColor(Color.RED);
            bmiImage.setImageResource(R.drawable.obese);

            //bmi Status In List Information
            bmiStatusInListInformation.setText(" " + getResources().getString(R.string.obese)); //change the status text. (also create space at the beginning by " " )

            // information: how many kg need to lose to be in ideal weight
            information2.setText(getResources().getString(R.string.ideal_weight_lose) + " " + weightToLoose+ " " +  getResources().getString(R.string.kg));

            //conclusion: obese weight advice
            conclusion.setText(getResources().getString(R.string.obese_advice));
        }






        containerOFIntroduction.setVisibility(View.GONE); //hide container of introduction
        containerOfBmiResult.setVisibility(View.VISIBLE); //show container of bmi result (bmi List information)
        containerForConclusion.setVisibility(View.VISIBLE); // show container of conclusion




    }



    // method to close keyboard
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }


}
