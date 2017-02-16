package com.example.estudiante.controlcito;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer{
    private Button up;
    private Button down;
    private Button right;
    private Button left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Comunicacion.getInstance().addObserver(this);

        up = (Button) findViewById(R.id.up);
        down = (Button) findViewById(R.id.down);
        right = (Button) findViewById(R.id.right);
        left =  (Button) findViewById(R.id.left);

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comunicacion.getInstance().enviar("up", Comunicacion.MULTI_ADDRESS, Comunicacion.PUERTO);
            }
        });

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comunicacion.getInstance().enviar("down", Comunicacion.MULTI_ADDRESS, Comunicacion.PUERTO);
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comunicacion.getInstance().enviar("right", Comunicacion.MULTI_ADDRESS, Comunicacion.PUERTO);
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comunicacion.getInstance().enviar("left", Comunicacion.MULTI_ADDRESS, Comunicacion.PUERTO);
            }
        });
     }

    @Override
    public void update(Observable observable, Object o) {

    }
}
