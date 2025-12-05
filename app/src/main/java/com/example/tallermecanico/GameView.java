package com.example.tallermecanico;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable, SurfaceHolder.Callback {

    private Thread thread;
    private boolean running = false;
    private boolean paused = false;
    private SurfaceHolder holder;
    private Paint obstaclePaint, roadPaint, linePaint;
    private Random random = new Random();

    private List<Obstacle> obstacles = new ArrayList<>();
    private long lastSpawn = 0;
    private long spawnInterval = 2000; // 2 segundos inicial
    private long gameStartTime = 0;
    private int score = 0;
    private int lives = 3;
    private boolean gameOver = false;

    // Car state
    private float carX = 0f, carY = 0f;
    private float carWidth = 180f;
    private float carHeight = 220f;
    private float tiltX = 0f; // inclinación del celular
    private float carSpeed = 8f; // velocidad lateral

    // Road lines animation
    private float roadLineOffset = 0f;
    private float roadLineSpeed = 12f;

    // Bitmaps para car y obstáculos
    private Bitmap carBitmap;
    private Bitmap obstacleCar1, obstacleCar2, obstacleTree, obstacleSign;

    private OnScoreUpdateListener scoreUpdateListener;
    private OnLifeUpdateListener lifeUpdateListener;
    private OnGameOverListener gameOverListener;
    private OnObstacleEvadedListener obstacleEvadedListener;

    private final Object lock = new Object();

    public GameView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        holder = getHolder();
        holder.addCallback(this);

        roadPaint = new Paint();
        roadPaint.setColor(0xFF3A3A3A);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(0xFFFFFFFF);
        linePaint.setStrokeWidth(8f);

        obstaclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // Cargar imágenes de recursos drawable
        // Asegúrate de tener estos archivos en res/drawable/
        carBitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.car1);
        obstacleCar1 = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.obstacle_car1);
        obstacleCar2 = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.obstacle_car2);
        obstacleTree = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.obstacle_tree);
        obstacleSign = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.obstacle_sign);

        // Escalar bitmaps al tamaño deseado
        carBitmap = Bitmap.createScaledBitmap(carBitmap, 180, 220, true);
        obstacleCar1 = Bitmap.createScaledBitmap(obstacleCar1, 160, 200, true);
        obstacleCar2 = Bitmap.createScaledBitmap(obstacleCar2, 180, 220, true);
        obstacleTree = Bitmap.createScaledBitmap(obstacleTree, 150, 200, true);
        obstacleSign = Bitmap.createScaledBitmap(obstacleSign, 200, 160, true);
    }

    public void resetGame() {
        synchronized (lock) {
            score = 0;
            lives = 3;
            obstacles.clear();
            gameOver = false;
            paused = false;
            gameStartTime = SystemClock.uptimeMillis();
            spawnInterval = 2000;
            lastSpawn = 0;
            roadLineOffset = 0f;
        }
    }

    public boolean isPaused() {
        synchronized (lock) {
            return paused;
        }
    }

    public void pauseGameLoop() {
        synchronized (lock) {
            paused = true;
        }
    }

    public void resumeGameLoop() {
        synchronized (lock) {
            paused = false;
        }
    }

    public void setTilt(float tiltX) {
        synchronized (lock) {
            this.tiltX = tiltX;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (getWidth() > 0 && getHeight() > 0) {
            synchronized (lock) {
                carX = getWidth() / 2f - carWidth / 2f;
                carY = getHeight() - carHeight - 100f;
            }
        }
        resume();
    }

    @Override public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {}
    @Override public void surfaceDestroyed(SurfaceHolder surfaceHolder) { pause(); }

    public void resume() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        running = false;
        try { if (thread != null) thread.join(); } catch (InterruptedException ignored) {}
    }

    @Override
    public void run() {
        while (running) {
            if (!holder.getSurface().isValid()) {
                try { Thread.sleep(16); } catch (InterruptedException ignored) {}
                continue;
            }
            long now = SystemClock.uptimeMillis();

            synchronized (lock) {
                if (!paused && !gameOver) {
                    // Aumentar dificultad con el tiempo
                    long elapsed = now - gameStartTime;
                    long currentInterval = Math.max(600, 2000 - (elapsed / 10000) * 200);

                    if (now - lastSpawn > currentInterval) {
                        spawnObstacle(elapsed);
                        lastSpawn = now;
                    }
                    update();
                }
            }

            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {
                    synchronized (lock) {
                        drawGame(c);
                    }
                }
            } finally {
                if (c != null) {
                    holder.unlockCanvasAndPost(c);
                }
            }

            try { Thread.sleep(16); } catch (InterruptedException ignored) {}
        }
    }

    private void spawnObstacle(long gameTime) {
        int w = getWidth(), h = getHeight();
        if (w == 0 || h == 0) return;

        // Posición X aleatoria dentro de la carretera
        float margin = 40f;
        float x = margin + random.nextFloat() * (w - 2 * margin - carWidth);
        float y = -150f;

        // Tipo de obstáculo aleatorio
        int type = random.nextInt(4);
        Bitmap bmp;
        float width, height;

        switch (type) {
            case 0:
                bmp = obstacleCar1;
                width = 80f;
                height = 120f;
                break;
            case 1:
                bmp = obstacleCar2;
                width = 80f;
                height = 120f;
                break;
            case 2:
                bmp = obstacleTree;
                width = 70f;
                height = 100f;
                break;
            default:
                bmp = obstacleSign;
                width = 60f;
                height = 90f;
                break;
        }

        // Velocidad aumenta con el tiempo
        float speed = 8f + (gameTime / 15000f) * 4f;

        // Inclinación: después de 20 segundos, algunos obstáculos vienen inclinados
        float angle = 0f;

        obstacles.add(new Obstacle(x, y, width, height, speed, angle, bmp));
    }

    private void update() {
        int w = getWidth(), h = getHeight();
        if (w == 0 || h == 0) return;

        // Mover carro según inclinación
        carX += tiltX * carSpeed;

        // Mantener dentro de límites
        float margin = 20f;
        if (carX < margin) carX = margin;
        if (carX > w - carWidth - margin) carX = w - carWidth - margin;

        // Animar líneas de la carretera
        roadLineOffset += roadLineSpeed;
        if (roadLineOffset > 100f) roadLineOffset = 0f;

        // Actualizar obstáculos
        Iterator<Obstacle> it = obstacles.iterator();
        while (it.hasNext()) {
            Obstacle obs = it.next();
            obs.y += obs.speed;

            // Verificar colisión
            if (checkCollision(obs)) {
                it.remove();
                lives--;
                if (lifeUpdateListener != null) {
                    lifeUpdateListener.onLifeUpdated(lives);
                }
                if (lives <= 0) {
                    gameOver = true;
                    if (gameOverListener != null) {
                        gameOverListener.onGameOver(score);
                    }
                }
            }
            // Obstáculo esquivado
            else if (obs.y > h) {
                it.remove();
                score += 5;
                if (scoreUpdateListener != null) {
                    scoreUpdateListener.onScoreUpdated(score);
                }
                if (obstacleEvadedListener != null) {
                    obstacleEvadedListener.onObstacleEvaded();
                }
            }
        }
    }

    private boolean checkCollision(Obstacle obs) {
        // Colisión simple con rectángulos
        float carLeft = carX;
        float carRight = carX + carWidth;
        float carTop = carY;
        float carBottom = carY + carHeight;

        float obsLeft = obs.x;
        float obsRight = obs.x + obs.width;
        float obsTop = obs.y;
        float obsBottom = obs.y + obs.height;

        return carLeft < obsRight && carRight > obsLeft &&
                carTop < obsBottom && carBottom > obsTop;
    }

    private void drawGame(Canvas c) {
        int w = getWidth(), h = getHeight();

        // Dibujar carretera
        c.drawColor(0xFF228B22); // Verde césped
        roadPaint.setColor(0xFF3A3A3A);
        c.drawRect(0, 0, w, h, roadPaint);

        // Dibujar líneas divisorias de la carretera
        linePaint.setColor(0xFFFFFFFF);
        float lineY = roadLineOffset;
        while (lineY < h) {
            c.drawLine(w / 2f, lineY, w / 2f, lineY + 60f, linePaint);
            lineY += 100f;
        }

        // Bordes de carretera
        linePaint.setColor(0xFFFFFF00);
        linePaint.setStrokeWidth(12f);
        c.drawLine(20f, 0, 20f, h, linePaint);
        c.drawLine(w - 20f, 0, w - 20f, h, linePaint);

        // Dibujar obstáculos
        for (Obstacle obs : obstacles) {
            c.drawBitmap(obs.bitmap, obs.x, obs.y, null);
        }

        // Dibujar carro del jugador
        c.drawBitmap(carBitmap, carX, carY, null);
    }

    // Clase interna para obstáculos
    private static class Obstacle {
        float x, y, width, height, speed, angle;
        Bitmap bitmap;

        Obstacle(float x, float y, float width, float height, float speed, float angle, Bitmap bitmap) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.speed = speed;
            this.angle = angle;
            this.bitmap = bitmap;
        }
    }

    // Listeners
    public interface OnScoreUpdateListener { void onScoreUpdated(int score); }
    public interface OnLifeUpdateListener { void onLifeUpdated(int lives); }
    public interface OnGameOverListener { void onGameOver(int finalScore); }
    public interface OnObstacleEvadedListener { void onObstacleEvaded(); }

    public void setScoreUpdateListener(OnScoreUpdateListener l) { this.scoreUpdateListener = l; }
    public void setLifeUpdateListener(OnLifeUpdateListener l) { this.lifeUpdateListener = l; }
    public void setGameOverListener(OnGameOverListener l) { this.gameOverListener = l; }
    public void setOnObstacleEvadedListener(OnObstacleEvadedListener l) { this.obstacleEvadedListener = l; }
}