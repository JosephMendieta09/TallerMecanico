package com.example.tallermecanico;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SensoresActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelSensor;
    private float tiltX = 0f;
    private GameView gameView;
    private TextView scoreText, tvHighScore, tvFinalScore, livesText;
    private LinearLayout menuLayout, gameOverLayout;
    private Button btnStart, btnRestart;

    private Vibrator vibrator;

    private SharedPreferences prefs;
    private int highScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensores);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        gameView = findViewById(R.id.gameView);
        scoreText = findViewById(R.id.scoreText);
        livesText = findViewById(R.id.livesText);
        menuLayout = findViewById(R.id.menuLayout);
        tvHighScore = findViewById(R.id.tvHighScore);
        gameOverLayout = findViewById(R.id.gameOverLayout);
        tvFinalScore = findViewById(R.id.tvFinalScore);
        btnStart = findViewById(R.id.btnStart);
        btnRestart = findViewById(R.id.btnRestart);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        prefs = getSharedPreferences("autoLocoData", MODE_PRIVATE);
        highScore = prefs.getInt("highScore", 0);
        tvHighScore.setText("Récord: " + highScore + " pts");

        // Inicio del juego
        btnStart.setOnClickListener(v -> startGame());
        btnRestart.setOnClickListener(v -> {
            gameOverLayout.setVisibility(View.GONE);
            startGame();
        });

        // Configurar listeners de la vista de juego
        gameView.setScoreUpdateListener(score -> runOnUiThread(() -> {
            scoreText.setText("Puntos: " + score);
            if (score > highScore) {
                highScore = score;
                tvHighScore.setText("Récord: " + highScore + " pts");
                prefs.edit().putInt("highScore", highScore).apply();
            }
        }));

        gameView.setLifeUpdateListener(lives -> runOnUiThread(() ->
                livesText.setText("❤ " + lives)
        ));

        gameView.setGameOverListener(finalScore -> runOnUiThread(() -> {
            gameView.setVisibility(View.GONE);
            scoreText.setVisibility(View.GONE);
            livesText.setVisibility(View.GONE);
            gameOverLayout.setVisibility(View.VISIBLE);

            String message = "GAME OVER\n\nPuntuación: " + finalScore + " pts";
            if (finalScore >= highScore) {
                message += "\n\n🏆 ¡NUEVO RÉCORD! 🏆";
            }
            tvFinalScore.setText(message);
        }));

        gameView.setOnObstacleEvadedListener(() -> {
            // Vibración corta al esquivar
            if (vibrator != null) vibrator.vibrate(50);
        });
    }

    private void startGame() {
        menuLayout.setVisibility(View.GONE);
        gameOverLayout.setVisibility(View.GONE);
        gameView.resetGame();
        gameView.setVisibility(View.VISIBLE);
        scoreText.setVisibility(View.VISIBLE);
        livesText.setVisibility(View.VISIBLE);
        scoreText.setText("Puntos: 0");
        livesText.setText("❤ 3");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelSensor != null) {
            sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_GAME);
        }
        gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        gameView.pause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Usar inclinación lateral (X) para mover el carro
            // Invertir el signo para que sea intuitivo
            tiltX = -event.values[0];

            // Limitar el rango para mejor control
            if (tiltX > 5f) tiltX = 5f;
            if (tiltX < -5f) tiltX = -5f;

            // Normalizar a rango -1 a 1
            tiltX = tiltX / 5f;

            gameView.setTilt(tiltX);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // vuelve a la actividad anterior
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}