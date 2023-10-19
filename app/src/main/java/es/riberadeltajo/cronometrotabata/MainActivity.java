package es.riberadeltajo.cronometrotabata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //Variables de clase
    int srsleft= 0; //Series restantes.
    long work; //Variable de tiempo de trabajo
    long rest; //Variable de tiempo de descanso
    long currentTimer = 0L; //Variable de tiempo de cronometro.
    boolean isWorking = false; //Boolean para saber si se está trabajando o descansando

    EditText series; //Input donde se introducen las series

    EditText workTimer; //Input donde se introducen el tiempo de trabajo

    EditText restTimer; //Input donde se introducen el tiempo de descanso
    Button btn; //Botón que inicia el proceso
    TextView contText; //Texto del contador
    TextView currentMode;//Texto que nos indica si se está trabajando o descansando.
    ConstraintLayout fondo; //El layout, bautizado como fondo porque se utilizará para cambiar el color.

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        series = findViewById(R.id.series);
        workTimer = findViewById(R.id.workTimer);
        restTimer = findViewById(R.id.restTimer);
        btn = findViewById(R.id.counterBtn);
        contText = findViewById(R.id.contText);
        fondo = findViewById(R.id.appBackground);
        currentMode = findViewById(R.id.status);
        fondo = findViewById(R.id.appBackground);
        mp = MediaPlayer.create(this, R.raw.timer);





    }

    @Override
    protected void onResume() {



        super.onResume();

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    work = (Long.parseLong(workTimer.getText().toString()) + 1) * 1000;
                    rest = (Long.parseLong(restTimer.getText().toString()) + 1) * 1000;
                    srsleft = Integer.parseInt(series.getText().toString());
                    currentTimer = work;
                    isWorking = true;
                    series.setFocusableInTouchMode(false);
                    workTimer.setFocusableInTouchMode(false);
                    restTimer.setFocusableInTouchMode(false);
                    btn.setClickable(false);
                    fondo.setBackgroundColor(Color.GREEN);
                    currentMode.setText("Trabajando");
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(fondo.getWindowToken(), 0);
                    crearCronometro();
                }catch(NumberFormatException e){
                    Toast.makeText(getApplicationContext(), "Hay valores en blanco", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void checkForSeriesLeft(){

        if(srsleft>0){
            if(isWorking) {
                fondo.setBackgroundColor(Color.RED);
                currentMode.setText("Descansando, quedan: "+(srsleft-1));
                currentTimer = rest;
                isWorking = false;
                crearCronometro();
            }else{

                fondo.setBackgroundColor(Color.GREEN);
                currentMode.setText("Trabajando");
                currentTimer = work;
                isWorking = true;

                crearCronometro();

            }
        }else{
            currentTimer = 0l;
            fondo.setBackgroundColor(Color.WHITE);
            currentMode.setText("Esperando...");
            series.setFocusableInTouchMode(true);
            workTimer.setFocusableInTouchMode(true);
            restTimer.setFocusableInTouchMode(true);
            btn.setClickable(true);
            isWorking=false;
        }
    };


    public void crearCronometro(){
        CountDownTimer trabajo =  new CountDownTimer(currentTimer, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                contText.setText(Long.toString(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                if(!isWorking){
                    srsleft--;
                }
                mp.start();
                checkForSeriesLeft();

            }



        };
        trabajo.start();
    }
}