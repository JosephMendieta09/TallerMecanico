package com.example.tallermecanico;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReconocimientoPlacaActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button btnCapture;
    private Button btnGallery;
    private Button btnConfirm;
    private TextView tvPlateNumber;
    private String detectedPlate = "";

    // Launcher para capturar foto
    private final ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    if (imageBitmap != null) {
                        imageView.setImageBitmap(imageBitmap);
                        recognizeText(imageBitmap);
                    }
                }
            }
    );

    // Launcher para seleccionar de galería
    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                    getContentResolver(),
                                    imageUri
                            );
                            imageView.setImageBitmap(bitmap);
                            recognizeText(bitmap);
                        } catch (IOException e) {
                            Toast.makeText(this, "Error al cargar imagen: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );

    // Launcher para permisos de cámara
    private final ActivityResultLauncher<String> requestCameraPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
                }
            }
    );

    // Launcher para permisos de galería (para Android 13+)
    private final ActivityResultLauncher<String> requestGalleryPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    openGallery();
                } else {
                    // En versiones más antiguas, intenta abrir la galería de todos modos
                    openGallery();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reconocimiento_placa);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initViews();
        setupListeners();
    }

    private void initViews() {
        imageView = findViewById(R.id.imageViewPlate);
        btnCapture = findViewById(R.id.btnCapture);
        btnGallery = findViewById(R.id.btnGallery);
        btnConfirm = findViewById(R.id.btnConfirm);
        tvPlateNumber = findViewById(R.id.tvPlateNumber);

        btnConfirm.setEnabled(false);
    }

    private void setupListeners() {
        btnCapture.setOnClickListener(v -> checkCameraPermission());
        btnGallery.setOnClickListener(v -> checkGalleryPermission());
        btnConfirm.setOnClickListener(v -> returnPlateNumber());
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void checkGalleryPermission() {
        // Para Android 13+ (API 33+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                requestGalleryPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            }
        } else {
            // Para versiones anteriores
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                requestGalleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePictureLauncher.launch(takePictureIntent);
        } else {
            Toast.makeText(this, "No hay aplicación de cámara disponible",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        if (pickImageIntent.resolveActivity(getPackageManager()) != null) {
            pickImageLauncher.launch(pickImageIntent);
        } else {
            Toast.makeText(this, "No hay aplicación de galería disponible",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void recognizeText(Bitmap bitmap) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        recognizer.process(image)
                .addOnSuccessListener(visionText ->
                        processTextRecognitionResult(visionText.getText())
                )
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Error al reconocer texto: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show()
                );
    }

    private void processTextRecognitionResult(String text) {
        // Patrón para placas (ajusta según tu país)
        // Ejemplo: ABC-123 o ABC123
        Pattern platePattern = Pattern.compile("[0-9]{3,4}[-]?[A-Z]{3}");

        String[] lines = text.split("\n");
        String foundPlate = "";

        for (String line : lines) {
            String cleanLine = line.replace(" ", "").toUpperCase();
            Matcher matcher = platePattern.matcher(cleanLine);

            if (matcher.find()) {
                foundPlate = matcher.group();
                break;
            }
        }

        if (!foundPlate.isEmpty()) {
            detectedPlate = foundPlate;
            tvPlateNumber.setText("Placa detectada: " + detectedPlate);
            btnConfirm.setEnabled(true);
            Toast.makeText(this, "Placa encontrada: " + detectedPlate,
                    Toast.LENGTH_SHORT).show();
        } else {
            tvPlateNumber.setText("No se detectó ninguna placa. Intenta nuevamente.");
            btnConfirm.setEnabled(false);
            Toast.makeText(this,
                    "No se detectó una placa válida. Intenta con otra imagen.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void returnPlateNumber() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("PLATE_NUMBER", detectedPlate);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // vuelve a la actividad anterior
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}