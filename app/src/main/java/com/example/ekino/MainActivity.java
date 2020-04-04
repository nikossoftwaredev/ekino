package com.example.ekino;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private boolean [] pressed;
    private int [] numbers;
    private ArrayList<Button> buttonArray;
    private TextView resultText,moneyView;
    private int totalNo =0,success = 0,igame= 0;
    private Button startButton;
    private int totalMoney = 20;
    private int bet = 1;


    double A[][] = {{0,2.5},
        {0,1,5},
        {0,0,2.5,25},
        {0,0,1,4,100},
        {0,0,0,2,20,450},
        {0,0,0,1,7,50,1600},
        {0,0,0,1,3,20,100,5000},
        {0,0,0,0,2,10,50,1000,15000},
        {0,0,0,0,1,5,25,200,4000,40000},
        {2,0,0,0,0,2,20,80,500,10000,100000},
        {2,0,0,0,0,1,10,50,250,1500,15000,500000},
        {4,0,0,0,0,0,5,25,150,1000,2500,25000,1000000}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pressed = new boolean [81];
        numbers = new int[81];
        buttonArray = new ArrayList<>();


        for (int i =0 ; i <numbers.length ; i++ ){
            numbers[i] = 0;
        }


        final String[] numbers = {"1", "2", "3", "4" , "5" ,"6" ,"7" ,"8" ,"9" ,"10" , "11" , "12"};
        final TextView howMany = findViewById(R.id.howMany);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a number");

        builder.setItems(numbers, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println(numbers[which]);
                howMany.setText(numbers[which]);
            }
        });


        howMany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show();
            }
        });
        LinearLayout basicLayout = findViewById(R.id.basicLayout);


        LinearLayout horizontal = new LinearLayout(this);
        horizontal.setBackgroundColor(getResources().getColor(R.color.kino));


        for(int i=1 ; i <= 80 ; i++){
            noFactory(i,horizontal);
            if(i%10 == 0){
                basicLayout.addView(horizontal);
                horizontal = new LinearLayout(this);
                horizontal.setBackgroundColor(getResources().getColor(R.color.kino));
            }
        }

        //Start button
        startButton = new Button(this);
        startButton.setText("Πάμε");
        startButton.setEnabled(false);
        startButton.setBackgroundResource(R.drawable.button_good);
        basicLayout.addView(startButton);

        //Text
        resultText =  findViewById(R.id.howMany);
        resultText.setText("Έχεις επιλέξει " + totalNo + " αριθμούς!");

        //Reset Button
        Button resetB = new Button(this);
        resetB.setText("Ξανά");
        resetB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
        resetB.setBackgroundResource(R.drawable.button_good);

        //Money Text
        moneyView = new TextView(this);
        moneyView.setText("Your money = " + totalMoney + "€");

        basicLayout.addView(resetB);
        basicLayout.addView(moneyView);

        String tmp = "";
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    startGame();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });





    }

    private void reset(){
        for(int i =0 ; i <81 ; i++){
            if(i !=0 )
                buttonArray.get(i-1).setBackgroundResource(R.drawable.default_b);

            numbers[i] =0;
            pressed[i] = false;


        }

        totalNo = 0;
        success = 0;
        igame = 0;
        startButton.setEnabled(false);

        
        resultText.setText("Έχεις επιλέξει " + totalNo + " αριθμούς!");

    }

    private void startGame() throws InterruptedException {

       totalMoney -=bet;
        final Random random = new Random();

        Thread t = new Thread(){

            @Override
           public void run(){
                try{

                   while(true){
                        Thread.sleep(500);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int tmp = random.nextInt(79) + 1;

                                //Tyxaios arithmos
                                if (numbers[tmp] == 0) { //If it hasn't been already there

                                    igame++;
                                    numbers[tmp] = 1;

                                    if(pressed[tmp])
                                        success++;

                                    buttonArray.get(tmp - 1).setBackgroundResource(R.drawable.random_b);

                                    if (pressed[tmp])
                                        buttonArray.get(tmp - 1).setBackgroundResource(R.drawable.matched);
                                }

                                resultText.setText("Πέτυχες " + success + "/" + totalNo + " Απόδοση: " + A[totalNo-1][success] + " Απομένουν " + (20-igame) + " αριθμοί");

                            }
                        });

                       if(igame >= 20)
                           break;



                    }

                    totalMoney += A[totalNo-1][success]*bet;
                    moneyView.setText("Your money = " + totalMoney + "€");


                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }


        };

        t.start();


    }

    private void noFactory(final int number, LinearLayout horizontal){

        horizontal.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        horizontal.setLayoutParams(params);
        final Button tmpButton = new Button(this);
        tmpButton.setText("" + number);
        tmpButton.setLayoutParams(new LinearLayout.LayoutParams(110,110));
        tmpButton.setBackgroundResource(R.drawable.default_b);

        buttonArray.add(tmpButton);

        tmpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pressed[number]){
                    pressed[number] = false;
                    totalNo--;
                    tmpButton.setBackgroundResource(R.drawable.default_b);
                    if(totalNo == 1)
                        resultText.setText("Έχεις επιλέξει " + totalNo + " αριθμό!");
                    else
                        resultText.setText("Έχεις επιλέξει " + totalNo + " αριθμούς!");

                    if(totalNo == 0)
                        startButton.setEnabled(false);

                }else{
                    startButton.setEnabled(true);

                    if(totalNo >= 12){
                        resultText.setText("Μέχρι 12 αριθμούς");
                    }else {

                        pressed[number] = true;

                        totalNo++;

                        if(totalNo == 1)
                            resultText.setText("Έχεις επιλέξει " + totalNo + " αριθμό!");
                        else
                            resultText.setText("Έχεις επιλέξει " + totalNo + " αριθμούς!");

                        tmpButton.setBackgroundResource(R.drawable.selected);
                    }


                }

            }
        });

        horizontal.addView(tmpButton);



    }
}
