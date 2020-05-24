package com.na21k.audioplayer.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.na21k.audioplayer.MainActivity;
import com.na21k.audioplayer.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class EqualizerFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private static final short NUM_BAND_VIEWS = 10;
    private static final short SEEKBAR_MAX = 900;
    private static final short SEEKBAR_MIN = -900;
    private static final short SEEKBAR_STEP = 10;

    private Spinner presetSpinner;
    private ArrayAdapter<?> presetAdapter;
    private List<String> eqPresets;
    private SeekBar[] bands;
    //private SeekBar preamp;
    private TextView[] textViewsBands;
    private TextView[] textViewBandLevels;
    //private TextView textViewPreampLvl;

    public EqualizerFragment() {
        // Required empty public constructor
    }

    public static EqualizerFragment newInstance() {
        EqualizerFragment fragment = new EqualizerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_equalizer, container, false);

        eqPresets = new ArrayList<>();
        eqPresets.add("Custom");
        for (short i = 0; i < MainActivity.equalizer.getNumberOfPresets(); i++) {
            eqPresets.add(MainActivity.equalizer.getPresetName(i));
        }

        presetSpinner = view.findViewById(R.id.spinner_equalizer_preset);
        presetAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.support_simple_spinner_dropdown_item, eqPresets);
        presetSpinner.setAdapter(presetAdapter);
        presetSpinner.setSelection(0);
        presetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    MainActivity.equalizer.usePreset((short) (position - 1));
                }
                updateSeekBarsAndTextViews();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        bands = new SeekBar[NUM_BAND_VIEWS];
        textViewsBands = new TextView[NUM_BAND_VIEWS];
        textViewBandLevels = new TextView[NUM_BAND_VIEWS];

        textViewsBands[0] = view.findViewById(R.id.textview_equalizer_band_0);
        textViewsBands[1] = view.findViewById(R.id.textview_equalizer_band_1);
        textViewsBands[2] = view.findViewById(R.id.textview_equalizer_band_2);
        textViewsBands[3] = view.findViewById(R.id.textview_equalizer_band_3);
        textViewsBands[4] = view.findViewById(R.id.textview_equalizer_band_4);
        textViewsBands[5] = view.findViewById(R.id.textview_equalizer_band_5);
        textViewsBands[6] = view.findViewById(R.id.textview_equalizer_band_6);
        textViewsBands[7] = view.findViewById(R.id.textview_equalizer_band_7);
        textViewsBands[8] = view.findViewById(R.id.textview_equalizer_band_8);
        textViewsBands[9] = view.findViewById(R.id.textview_equalizer_band_9);

        textViewBandLevels[0] = view.findViewById(R.id.equalizer_band_0_lvl);
        textViewBandLevels[1] = view.findViewById(R.id.equalizer_band_1_lvl);
        textViewBandLevels[2] = view.findViewById(R.id.equalizer_band_2_lvl);
        textViewBandLevels[3] = view.findViewById(R.id.equalizer_band_3_lvl);
        textViewBandLevels[4] = view.findViewById(R.id.equalizer_band_4_lvl);
        textViewBandLevels[5] = view.findViewById(R.id.equalizer_band_5_lvl);
        textViewBandLevels[6] = view.findViewById(R.id.equalizer_band_6_lvl);
        textViewBandLevels[7] = view.findViewById(R.id.equalizer_band_7_lvl);
        textViewBandLevels[8] = view.findViewById(R.id.equalizer_band_8_lvl);
        textViewBandLevels[9] = view.findViewById(R.id.equalizer_band_9_lvl);

        bands[0] = view.findViewById(R.id.seekbar_equalizer_band_0);
        bands[1] = view.findViewById(R.id.seekbar_equalizer_band_1);
        bands[2] = view.findViewById(R.id.seekbar_equalizer_band_2);
        bands[3] = view.findViewById(R.id.seekbar_equalizer_band_3);
        bands[4] = view.findViewById(R.id.seekbar_equalizer_band_4);
        bands[5] = view.findViewById(R.id.seekbar_equalizer_band_5);
        bands[6] = view.findViewById(R.id.seekbar_equalizer_band_6);
        bands[7] = view.findViewById(R.id.seekbar_equalizer_band_7);
        bands[8] = view.findViewById(R.id.seekbar_equalizer_band_8);
        bands[9] = view.findViewById(R.id.seekbar_equalizer_band_9);
        //preamp = view.findViewById(R.id.seekbar_preamp);
        /*preamp.setMax((SEEKBAR_MAX - SEEKBAR_MIN) / SEEKBAR_STEP);  //(max - min) / step
        preamp.setOnSeekBarChangeListener(this);*/

        for (short i = 0; i < NUM_BAND_VIEWS; i++) {
            bands[i].setMax((SEEKBAR_MAX - SEEKBAR_MIN) / SEEKBAR_STEP);    //(max - min) / step
            bands[i].setOnSeekBarChangeListener(this);
            textViewsBands[i].setText(hzFormat(MainActivity.equalizer.getCenterFreq(i)));
        }

        updateSeekBarsAndTextViews();

        //textViewPreampLvl = view.findViewById(R.id.preamp_lvl);

        return view;
    }

    private void updateSeekBarsAndTextViews() {
        for (short i = 0; i < NUM_BAND_VIEWS; i++) {
            textViewBandLevels[i].setText(MainActivity.equalizer.getBandLevel(i) / 100f + " dB");
            bands[i].setProgress((MainActivity.equalizer.getBandLevel(i) - SEEKBAR_MIN) / SEEKBAR_STEP);
        }
    }

    private String hzFormat(int mHz) {
        mHz /= 1000;
        if (mHz < 1000) {
            return mHz + " Hz";
        } else {
            return mHz / 1000 + " kHz";
        }
    }

    private static short seekBarToBandNo(SeekBar seekBar) {
        switch (seekBar.getId()) {
            case R.id.seekbar_equalizer_band_0:
                return 0;
            case R.id.seekbar_equalizer_band_1:
                return 1;
            case R.id.seekbar_equalizer_band_2:
                return 2;
            case R.id.seekbar_equalizer_band_3:
                return 3;
            case R.id.seekbar_equalizer_band_4:
                return 4;
            case R.id.seekbar_equalizer_band_5:
                return 5;
            case R.id.seekbar_equalizer_band_6:
                return 6;
            case R.id.seekbar_equalizer_band_7:
                return 7;
            case R.id.seekbar_equalizer_band_8:
                return 8;
            case R.id.seekbar_equalizer_band_9:
                return 9;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser)
            return;

        short newLevel = (short) (SEEKBAR_MIN + (progress * SEEKBAR_STEP));     //min + (progress * step)

        /*if (seekBar.getId() == R.id.seekbar_preamp) {
            // Pre amp
            textViewPreampLvl.setText(newLevel / 100f + " dB");
            //OpenSLPreAmp
            //TODO add preamp functionality
        } else {*/
        // Equalizer band
        textViewBandLevels[seekBarToBandNo(seekBar)].setText(newLevel / 100f + " dB");
        MainActivity.equalizer.setBandLevel(seekBarToBandNo(seekBar), newLevel);
        presetSpinner.setSelection(0);
        //}
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //if (seekBar.getId() != R.id.seekbar_preamp) {
        short bandNo = seekBarToBandNo(seekBar);
        Toast.makeText(getContext(), "Band " + hzFormat(MainActivity.equalizer.getCenterFreq(bandNo)) +
                        ": " + MainActivity.equalizer.getBandLevel(bandNo) / 100f + " dB",
                Toast.LENGTH_SHORT).show();
        //}
    }
}
