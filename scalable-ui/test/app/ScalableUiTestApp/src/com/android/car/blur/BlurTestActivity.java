/*
 * Copyright 2025 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.car.blur;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.util.Log;

import com.android.car.insets.R;

public class BlurTestActivity extends AppCompatActivity {

    private static final String TAG = "BlurTestActivity";
    private static final int BLUR_RADIUS = 25;
            // Adjust blur intensity (0-100 might be typical range)

    private Button buttonBlurBehind;
    private Button buttonBlurWithin;
    private Button buttonClearBlurs;
    private ColorDrawable originalWindowBackground; // To restore original background

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blur_test_activity);

        buttonBlurBehind = findViewById(R.id.button_blur_behind);
        buttonBlurWithin = findViewById(R.id.button_blur_within);
        buttonClearBlurs = findViewById(R.id.button_clear_blurs);

        // Store the original background if it's a ColorDrawable
        if (getWindow().getDecorView().getBackground() instanceof ColorDrawable) {
            originalWindowBackground = (ColorDrawable) getWindow().getDecorView().getBackground();
        } else {
            // Set a default fallback if needed, or handle other drawable types
            originalWindowBackground = new ColorDrawable(ContextCompat.getColor(this,
                    android.R.color.background_light)); // Example fallback
        }

        buttonBlurBehind.setOnClickListener(v -> {
            Log.d(TAG, "Attempting to enable Blur Behind");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                getWindow().getAttributes().setBlurBehindRadius(BLUR_RADIUS);
                getWindow().setAttributes(getWindow().getAttributes());
                Toast.makeText(this, "Blur Behind Enabled (Radius: " + BLUR_RADIUS + ")",
                        Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Blur Behind flags/radius set.");
                // IMPORTANT: For FLAG_BLUR_BEHIND to work, the Activity theme
                // usually needs to be translucent or floating. See Manifest/Theme section below.
            } else {
                Toast.makeText(this, "Blur Behind requires Android 12 (API 31+)",
                        Toast.LENGTH_LONG).show();
                Log.w(TAG, "Blur Behind requires Android 12+");
            }
        });

        buttonBlurWithin.setOnClickListener(v -> {
            Log.d(TAG, "Attempting to enable Blur Within");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // IMPORTANT: For setBackgroundBlurRadius to have a visible effect,
                // the window *must* have a translucent background DRAWABLE.
                // Let's set a semi-transparent background color.
                getWindow().setBackgroundDrawable(new ColorDrawable(
                        Color.parseColor("#80FFFFFF"))); // Example: 50% transparent white
                // Now apply the blur radius
                getWindow().setBackgroundBlurRadius(BLUR_RADIUS);
                Toast.makeText(this, "Blur Within Enabled (Radius: " + BLUR_RADIUS + ")",
                        Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Background blur radius set.");
            } else {
                Toast.makeText(this, "Background Blur requires Android 12 (API 31+)",
                        Toast.LENGTH_LONG).show();
                Log.w(TAG, "Background Blur requires Android 12+");
            }
        });

        buttonClearBlurs.setOnClickListener(v -> {
            Log.d(TAG, "Attempting to clear blurs");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // Clear Blur Behind flag and radius
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                getWindow().getAttributes().setBlurBehindRadius(0); // Reset radius

                // Clear Background Blur radius
                getWindow().setBackgroundBlurRadius(0); // Reset radius

                // Restore original background drawable if possible
                getWindow().setBackgroundDrawable(originalWindowBackground);

                // Re-apply attributes just in case
                getWindow().setAttributes(getWindow().getAttributes());

                Toast.makeText(this, "Blurs Cleared", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Blurs cleared.");
            } else {
                Toast.makeText(this, "Blurs only applicable on Android 12+",
                        Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Clear blurs called on pre-Android 12");
            }
        });
    }
}