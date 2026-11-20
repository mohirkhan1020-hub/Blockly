package com.example.blocklyn.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;

import com.example.blocklyn.R;
import com.example.blocklyn.service.TimerService;
import com.google.android.material.button.MaterialButton;

public class FocusFragment extends Fragment {
    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle s) {
        View v = i.inflate(R.layout.fragment_focus, c, false);
        MaterialButton pause = v.findViewById(R.id.btnPause);
        MaterialButton end = v.findViewById(R.id.btnEnd);

        pause.setOnClickListener(x -> {
            Intent intent = new Intent(requireContext(), TimerService.class);
            intent.setAction(TimerService.ACTION_TOGGLE);
            requireContext().startService(intent);
        });

        end.setOnClickListener(x -> {
            Intent intent = new Intent(requireContext(), TimerService.class);
            intent.setAction(TimerService.ACTION_STOP);
            requireContext().startService(intent);
        });
        return v;
    }
}
