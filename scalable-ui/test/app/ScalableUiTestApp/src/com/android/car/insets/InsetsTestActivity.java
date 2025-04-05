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

package com.android.car.insets;

import android.graphics.Insets;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsets.Type;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class InsetsTestActivity extends AppCompatActivity {

    private static final String TAG = "InsetsTestActivity";

    private TextView statusBarText;
    private TextView navBarText;
    private TextView imeText;
    private TextView systemGestureText;
    private TextView tappableElementText;
    private TextView systemBarText;

    private FrameLayout contentLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insets_test_activity);

        statusBarText = findViewById(R.id.status_bar_inset);
        navBarText = findViewById(R.id.nav_bar_inset);
        imeText = findViewById(R.id.ime_inset);
        systemGestureText = findViewById(R.id.system_gesture_inset);
        tappableElementText = findViewById(R.id.tappable_element_inset);
        systemBarText = findViewById(R.id.system_bar_inset);
        contentLayout = findViewById(R.id.content_frame);

        View rootView = findViewById(R.id.root_view);
        rootView.setOnApplyWindowInsetsListener((view, insets) -> {
            if (insets == null) return view.onApplyWindowInsets(insets);

            Insets statusBar = insets.getInsets(Type.statusBars());
            Insets navBar = insets.getInsets(Type.navigationBars());
            Insets ime = insets.getInsets(Type.ime());
            Insets systemGesture = insets.getInsets(Type.systemGestures());
            Insets tappableElement = insets.getInsets(Type.tappableElement());
            Insets systemBars = insets.getInsets(Type.systemBars());
            Insets systemOverlay = insets.getInsets(Type.systemOverlays());
            Insets all = insets.getInsets(Type.all());

            Log.d(TAG, "Insets changed:");
            Log.d(TAG, "Status Bar Inset: " + statusBar.toString());
            Log.d(TAG, "Navigation Bar Inset: " + navBar.toString());
            Log.d(TAG, "IME Inset: " + ime.toString());
            Log.d(TAG, "System Gesture Inset: " + systemGesture.toString());
            Log.d(TAG, "Tappable Element Inset: " + tappableElement.toString());
            Log.d(TAG, "System Bars Inset: " + systemBars.toString());
            Log.d(TAG, "System overlay Inset: " + systemOverlay.toString());
            Log.d(TAG, "ALL Inset: " + all.toString());

            // Set text
            statusBarText.setText("Status Bar Inset: " + statusBar);
            navBarText.setText("Navigation Bar Inset: " + navBar);
            imeText.setText("IME Inset: " + ime);
            systemGestureText.setText("System Gesture Inset: " + systemGesture);
            tappableElementText.setText("Tappable Element Inset: " + tappableElement);
            systemBarText.setText("System Bar Inset (Combined): " + systemBars);

            // Apply padding with max of all insets per side
            int paddingTop = Math.max(statusBar.top, Math.max(systemGesture.top, systemBars.top));
            int paddingBottom = Math.max(navBar.bottom, Math.max(systemGesture.bottom, ime.bottom));
            int paddingLeft = Math.max(navBar.left, Math.max(systemGesture.left, systemBars.left));
            int paddingRight = Math.max(navBar.right,
                    Math.max(systemGesture.right, systemBars.right));

            contentLayout.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

            Log.d(TAG, "Applied Padding - Left: " + paddingLeft + ", Top: " + paddingTop +
                    ", Right: " + paddingRight + ", Bottom: " + paddingBottom);

            return view.onApplyWindowInsets(insets);
        });

        // Request insets manually if needed
        rootView.requestApplyInsets();
    }
}
