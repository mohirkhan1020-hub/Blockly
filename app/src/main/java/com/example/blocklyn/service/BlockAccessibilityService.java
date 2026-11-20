package com.example.blocklyn.service;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.WindowManager;
import android.view.LayoutInflater;
import android.view.View;
import android.graphics.PixelFormat;

import com.example.blocklyn.R;

public class BlockAccessibilityService extends AccessibilityService {
    private WindowManager wm;
    private View overlay;
    private String[] blockedPackages = { };

    @Override public void onServiceConnected() {
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
    }

    @Override public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return;
        CharSequence pkg = event.getPackageName();
        if (pkg == null) return;
        for (String b : blockedPackages) {
            if (pkg.toString().equals(b)) { showOverlay(); return; }
        }
        hideOverlay();
    }

    private void showOverlay() {
        if (overlay != null) return;
        overlay = LayoutInflater.from(this).inflate(R.layout.overlay_blocked, null);
        int type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            type, 0, PixelFormat.TRANSLUCENT);
        wm.addView(overlay, lp);
    }

    private void hideOverlay() {
        if (overlay != null) { wm.removeView(overlay); overlay = null; }
    }

    @Override public void onInterrupt() { }
}
