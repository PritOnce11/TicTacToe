package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button bStart;
    Button bRestart;
    RadioGroup radioGroup;
    RadioButton radioButtonEasy;
    RadioButton radioButtonHard;

    boolean partidaEnCurso = false;

    TextView textVictoria;
    Integer[] botones;
    int[] tablero = new int[]{
            0, 0, 0,
            0, 0, 0,
            0, 0, 0
    };

    int state = 0;
    int fichasColocadas = 0;

    int turno = 1;
    int[] posicionGanadora = new int[]{-1, -1, -1};

    boolean esTurnoJugador = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textVictoria = findViewById(R.id.textView);
        textVictoria.setVisibility(View.INVISIBLE);
        botones = new Integer[]{
                R.id.b1, R.id.b2, R.id.b3,
                R.id.b4, R.id.b5, R.id.b6,
                R.id.b7, R.id.b8, R.id.b9
        };

        bRestart = findViewById(R.id.bReiniciar);
        bStart = findViewById(R.id.bEmpezar);
        radioGroup = findViewById(R.id.radioGroup);
        radioButtonEasy = findViewById(R.id.radioButtonEasy);
        radioButtonHard = findViewById(R.id.radioButtonHard);

        radioButtonEasy.setChecked(true);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonHard) {
                // Si el usuario selecciona el nivel "Difícil", mostrar un mensaje
                Toast.makeText(MainActivity.this, "Nivel no disponible", Toast.LENGTH_SHORT).show();
            }
        });

        bStart.setOnClickListener(v -> startGame());
        bRestart.setOnClickListener(v -> restartGame());
    }

    public void startGame() {
        if (radioButtonEasy.isChecked()) {
            textVictoria.setText("EMPIEZAS TU (O)");
            textVictoria.setTextColor(Color.BLACK);
            textVictoria.setVisibility(View.VISIBLE);
            partidaEnCurso = true;
            bStart.setEnabled(false);
        } else {
            Toast.makeText(MainActivity.this, "Nivel no disponible", Toast.LENGTH_SHORT).show();
        }
    }

    public void setFicha(View v) {
        if (partidaEnCurso && radioButtonEasy.isChecked() && esTurnoJugador) {
            if (state == 0) {
                int numBoton = Arrays.asList(botones).indexOf(v.getId());
                if (tablero[numBoton] == 0) {
                    v.setBackgroundResource(R.drawable.o);
                    tablero[numBoton] = 1;
                    fichasColocadas += 1;
                    state = checkState();
                    finishGame();
                    esTurnoJugador = false;
                    if (state == 0) {
                        turno = -1;
                        mostrarMensajeTurnoBot();
                        new Handler().postDelayed(this::bot, 1000);
                    }
                }
            }
        }
    }

    private void mostrarMensajeTurnoBot() {
        if (turno == -1) {
            textVictoria.setText("TURNO (X)");
            textVictoria.setVisibility(View.VISIBLE);
        } else {
            textVictoria.setText("TURNO (O)");
            textVictoria.setVisibility(View.VISIBLE);
        }
    }

    public void bot() {
        if (radioButtonEasy.isChecked()) {
            if(turno == -1){
                textVictoria.setText("TURNO (X)");
                textVictoria.setVisibility(View.VISIBLE);
            }
            Random random = new Random();
            int posicion = random.nextInt(tablero.length);
            while (tablero[posicion] != 0) {
                posicion = random.nextInt(tablero.length);
            }
            Button button = findViewById(botones[posicion]);
            button.setBackgroundResource(R.drawable.x_victoria2);
            tablero[posicion] = -1;
            fichasColocadas += 1;
            state = checkState();
            finishGame();
            esTurnoJugador = true;
            if (state == 0) {
                turno = 1;
                mostrarMensajeTurnoBot();
            }
        } else if (radioButtonHard.isChecked()) {
            Toast.makeText(MainActivity.this, "Nivel no disponible", Toast.LENGTH_SHORT).show();
        }
    }


    public void finishGame(){
        int fichaVictoria = R.drawable.o_victoria;
        if(state == 1 || state == -1){
            if(state == 1){
                textVictoria.setText("YOU WON");
                textVictoria.setTextColor(Color.GREEN);
                textVictoria.setVisibility(View.VISIBLE);

            }else {
                textVictoria.setVisibility(View.VISIBLE);
                textVictoria.setText("YOU LOSE");
                textVictoria.setTextColor(Color.RED);
                fichaVictoria = R.drawable.x_victoria2;
            }
            for(int i = 0; i < posicionGanadora.length; i++){
                Button button = findViewById(botones[posicionGanadora[i]]);
                button.setBackgroundResource(fichaVictoria);
            }
        } else if (state == 2) {
            textVictoria.setVisibility(View.VISIBLE);
            textVictoria.setText("HAS EMPATADO");
        }
    }

    public int checkState(){
        int newState = 0;
        if(Math.abs(tablero[0] + tablero[1] + tablero[2]) == 3){
            posicionGanadora = new int[]{0, 1, 2};
            newState = 1*turno;
        } else if (Math.abs(tablero[3] + tablero[4] + tablero[5]) == 3) {
            posicionGanadora = new int[]{3, 4, 5};
            newState = 1*turno;
        } else if (Math.abs(tablero[6] + tablero[7] + tablero[8]) == 3) {
            posicionGanadora = new int[]{6, 7, 8};
            newState = 1*turno;
        } else if (Math.abs(tablero[0] + tablero[4] + tablero[8]) == 3) {
            posicionGanadora = new int[]{0, 4, 8};
            newState = 1*turno;
        } else if (Math.abs(tablero[0] + tablero[3] + tablero[6]) == 3) {
            posicionGanadora = new int[]{0, 3, 6};
            newState = 1*turno;
        } else if (Math.abs(tablero[1] + tablero[4] + tablero[7]) == 3) {
            posicionGanadora = new int[]{1, 4, 7};
            newState = 1*turno;
        } else if (Math.abs(tablero[2] + tablero[4] + tablero[6]) == 3) {
            posicionGanadora = new int[]{2, 4, 6};
            newState = 1*turno;
        } else if (Math.abs(tablero[2] + tablero[5] + tablero[8]) == 3) {
            posicionGanadora = new int[]{2, 5, 8};
            newState = 1*turno;
        } else if (fichasColocadas == 9) {
            newState = 2;
        }
        return newState;
    }

    public void restartGame(){
        if(!radioButtonHard.isChecked()){
            habilitarTurnoJugador();
            textVictoria.setText("NUEVA PARTIDA");
            textVictoria.setVisibility(View.VISIBLE);
            textVictoria.setTextColor(Color.BLACK);
            state = 0;
            turno = 1;
            tablero = new int[]{
                    0, 0, 0,
                    0, 0, 0,
                    0, 0, 0
            };
            fichasColocadas = 0;
            posicionGanadora = new int[]{-1, -1, -1};

            for (int boton : botones) {
                Button button = findViewById(boton);
                button.setBackgroundResource(android.R.drawable.btn_default);
            }
        }else {
            Toast.makeText(MainActivity.this, "Nivel no disponible", Toast.LENGTH_SHORT).show();
        }
    }

    public void habilitarTurnoJugador() {
        esTurnoJugador = true;
    }

}