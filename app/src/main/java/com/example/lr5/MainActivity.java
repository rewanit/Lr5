package com.example.lr5;

import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    public static class MorseCodeConverter {
        private static final Map<Character, String> morseCodeMap = new HashMap<>();

        static {
            morseCodeMap.put('А', ".-");
            morseCodeMap.put('Б', "-...");
            morseCodeMap.put('В', ".--");
            morseCodeMap.put('Г', "--.");
            morseCodeMap.put('Д', "-..");
            morseCodeMap.put('Е', ".");
            morseCodeMap.put('Ж', "...-");
            morseCodeMap.put('З', "--..");
            morseCodeMap.put('И', "..");
            morseCodeMap.put('Й', ".---");
            morseCodeMap.put('К', "-.-");
            morseCodeMap.put('Л', ".-..");
            morseCodeMap.put('М', "--");
            morseCodeMap.put('Н', "-.");
            morseCodeMap.put('О', "---");
            morseCodeMap.put('П', ".--.");
            morseCodeMap.put('Р', ".-.");
            morseCodeMap.put('С', "...");
            morseCodeMap.put('Т', "-");
            morseCodeMap.put('У', "..-");
            morseCodeMap.put('Ф', "..-.");
            morseCodeMap.put('Х', "....");
            morseCodeMap.put('Ц', "-.-.");
            morseCodeMap.put('Ч', "---.");
            morseCodeMap.put('Ш', "----");
            morseCodeMap.put('Щ', "--.-");
            morseCodeMap.put('Ъ', "--.--");
            morseCodeMap.put('Ы', "-.--");
            morseCodeMap.put('Ь', "-..-");
            morseCodeMap.put('Э', "..-..");
            morseCodeMap.put('Ю', "..--");
            morseCodeMap.put('Я', ".-.-");
            morseCodeMap.put('A', ".-");
            morseCodeMap.put('B', "-...");
            morseCodeMap.put('C', "-.-.");
            morseCodeMap.put('D', "-..");
            morseCodeMap.put('E', ".");
            morseCodeMap.put('F', "..-.");
            morseCodeMap.put('G', "--.");
            morseCodeMap.put('H', "....");
            morseCodeMap.put('I', "..");
            morseCodeMap.put('J', ".---");
            morseCodeMap.put('K', "-.-");
            morseCodeMap.put('L', ".-..");
            morseCodeMap.put('M', "--");
            morseCodeMap.put('N', "-.");
            morseCodeMap.put('O', "---");
            morseCodeMap.put('P', ".--.");
            morseCodeMap.put('Q', "--.-");
            morseCodeMap.put('R', ".-.");
            morseCodeMap.put('S', "...");
            morseCodeMap.put('T', "-");
            morseCodeMap.put('U', "..-");
            morseCodeMap.put('V', "...-");
            morseCodeMap.put('W', ".--");
            morseCodeMap.put('X', "-..-");
            morseCodeMap.put('Y', "-.--");
            morseCodeMap.put('Z', "--..");
        }

        public static String textToMorse(String text) {
            text = text.toUpperCase();
            StringBuilder morseCode = new StringBuilder();

            for (int i = 0; i < text.length(); i++) {
                char character = text.charAt(i);

                if (character == ' ') {
                    morseCode.append(" ");
                } else if (morseCodeMap.containsKey(character)) {
                    morseCode.append(morseCodeMap.get(character));
                    morseCode.append(" ");
                }
            }

            return morseCode.toString();
        }
    }

    private CameraManager cameraManager;
    private String cameraId;
    private boolean isFlashlightOn = false;
    private Vibrator vibrator;

    private LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Switch flashlightSwitch = findViewById(R.id.switch1);
        Button morseButton = findViewById(R.id.morseButton);
        Button morseButton2 = findViewById(R.id.morseButton2);
         layout = findViewById(R.id.linearLayout);
        EditText editText = findViewById(R.id.editTextText);
        TextView textView = findViewById(R.id.textView);

        flashlightSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                turnOnFlashlight();
                layout.setBackgroundColor(Color.WHITE);
            } else {
                turnOffFlashlight();
                layout.setBackgroundColor(Color.BLACK);
            }
        });

        morseButton.setOnClickListener(v -> {
            String text = editText.getText().toString();
            String morseCode = textToMorse(text);
            textView.setText(morseCode);
        });

        morseButton2.setOnClickListener(v -> {
            String text = editText.getText().toString();
            String morseCode = textToMorse(text);
            textView.setText(morseCode);
            vibrateMorseCode(morseCode);
        });

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            flashlightSwitch.setEnabled(false);
        }

        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);


    }




    private void turnOnFlashlight() {
        try {
            if (cameraManager != null && cameraId != null) {
                cameraManager.setTorchMode(cameraId, true);
                isFlashlightOn = true;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void turnOffFlashlight() {
        try {
            if (cameraManager != null && cameraId != null) {
                cameraManager.setTorchMode(cameraId, false);
                isFlashlightOn = false;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private String textToMorse(String text) {
        return MorseCodeConverter.textToMorse(text);
    }

    private void vibrateMorseCode(String morseCode) {
        long dotDuration = 500; // Длительность точки (миллисекунды)
        long dashDuration = dotDuration * 2; // Длительность тире (3 раза дольше точки)
        long letterSpaceDuration = dashDuration ; // Длительность паузы между буквами

        final Handler handler = new Handler();
        long lastDuration = 0;
        for (char c : morseCode.toCharArray()) {

            if (c == '.') {
                vibrator.vibrate(VibrationEffect.createOneShot(dotDuration, 150));
                lastDuration = dotDuration;
            } else if (c == '-') {
                vibrator.vibrate(VibrationEffect.createOneShot(dashDuration, 100));
                lastDuration = dashDuration;
            }else if (c==' ')
            {
                lastDuration =0;
            }

            try {
                Thread.sleep(lastDuration+letterSpaceDuration);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // Добавляем паузу между символами

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFlashlightOn) {
            turnOffFlashlight();
        }
    }
}
