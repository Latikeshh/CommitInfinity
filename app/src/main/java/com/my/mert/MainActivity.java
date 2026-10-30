package com.my.mert;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import laki.vinox.v1.R;

public class MainActivity extends AppCompatActivity {

    private GridView inputGridView;
    private GridView outputGridView;
    private Button btnReset;
    private Button btnEqual;
    private Button btnTrk;

    private InputGridAdapter inputAdapter;
    private OutputGridAdapter outputAdapter;

    private final List<String> inputData = new ArrayList<>();
    private final List<String> outputData = new ArrayList<>();
    private boolean isInitialFocusDone = false;
    private String duple = "0";

    public String getDuple() {
        return duple;
    }

    public void setDuple(String duple) {
        this.duple = duple;
    }

    public void _req(final EditText _e, final String _a) {
        if (_e != null) {
            _e.setFocusable(true);
            _e.setFocusableInTouchMode(true);
            _e.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(_e, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    public void _sed(final EditText _e, final String _s) {
        if (_e != null && _s.length() == 1) {
            _e.setFocusable(true);
            _e.setFocusableInTouchMode(true);
            _e.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(_e, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        initData();
        setupGridDimensions();
        setupAdapters();
        setupListeners();

        _backupRestore("restore");
    }

    private void focusFirstVisibleCellOnStartup() {
        if (!isInitialFocusDone && inputGridView != null) {
            inputGridView.postDelayed(() -> {
                int firstVisible = inputGridView.getFirstVisiblePosition();
                View firstChild = inputGridView.getChildAt(12 - firstVisible);
                if (firstChild != null) {
                    EditText firstEt = firstChild.findViewById(R.id.etInputCell);
                    if (firstEt != null) {
                        isInitialFocusDone = true;
                        _req(firstEt, inputData.get(12));
                    }
                }
            }, 150);
        }
    }

    private void initViews() {
        inputGridView = findViewById(R.id.inputGridView);
        outputGridView = findViewById(R.id.outputGridView);
        btnReset = findViewById(R.id.reset);
        btnEqual = findViewById(R.id.equal);
        btnTrk = findViewById(R.id.trk);
    }

    private void initData() {
        inputData.clear();
        for (int i = 0; i < 48; i++) {
            inputData.add("");
        }

        // Initialize all 64 output TextView cells as empty on startup
        outputData.clear();
        for (int i = 0; i < 64; i++) {
            outputData.add("");
        }
    }

    private void setupGridDimensions() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;

        int paddingPx = (int) (32 * density);
        int availableWidth = metrics.widthPixels - paddingPx;
        int spacingPx = (int) (2 * density);

        int inputCellWidth = (availableWidth - (11 * spacingPx)) / 12;
        int inputCellHeight = (int) (44 * density);

        int outputCellWidth = (availableWidth - (7 * spacingPx)) / 8;
        int outputCellHeight = (int) (44 * density);

        int inputGridHeight = (3 * inputCellHeight) + (2 * spacingPx) + (int) (4 * density);
        ViewGroup.LayoutParams inputParams = inputGridView.getLayoutParams();
        if (inputParams != null) {
            inputParams.height = inputGridHeight;
            inputGridView.setLayoutParams(inputParams);
        }

        int outputGridHeight = (8 * outputCellHeight) + (7 * spacingPx) + (int) (8 * density);
        ViewGroup.LayoutParams outputParams = outputGridView.getLayoutParams();
        if (outputParams != null) {
            outputParams.height = outputGridHeight;
            outputGridView.setLayoutParams(outputParams);
        }

        inputAdapter = new InputGridAdapter(this, inputData);
        inputAdapter.setCellDimensions(inputCellWidth, inputCellHeight);

        outputAdapter = new OutputGridAdapter(this, outputData);
        outputAdapter.setCellDimensions(outputCellWidth, outputCellHeight);
    }

    private void setupAdapters() {
        inputGridView.setAdapter(inputAdapter);
        outputGridView.setAdapter(outputAdapter);

        focusFirstVisibleCellOnStartup();
    }

    private void setupListeners() {
        if (btnReset != null) {
            btnReset.setOnClickListener(v -> {
                duple = "0";
                for (int i = 0; i < inputData.size(); i++) {
                    inputData.set(i, "");
                }
                for (int i = 0; i < outputData.size(); i++) {
                    outputData.set(i, "");
                }
                if (outputAdapter != null) {
                    outputAdapter.setSelectedPosition(-1);
                }
                if (inputAdapter != null) inputAdapter.notifyDataSetChanged();
                if (outputAdapter != null) outputAdapter.notifyDataSetChanged();
                isInitialFocusDone = false;
                focusFirstVisibleCellOnStartup();
                Toast.makeText(this, "Cleared all inputs", Toast.LENGTH_SHORT).show();
            });
        }

        if (btnEqual != null) {
            btnEqual.setOnClickListener(v -> {
                duple = "0";
                _totaladd();
                _backupRestore("backup");
            });
        }

        if (btnTrk != null) {
            btnTrk.setOnClickListener(v -> {
                if ("0".equals(duple)) {
                    duple = "1";
                    applyAxisCutScan();
                }
            });
        }

        // Tap any cell in output grid to highlight that clicked cell and its vertical & horizontal axis neighbours
        if (outputGridView != null) {
            outputGridView.setOnItemClickListener((parent, view, position, id) -> {
                if (outputAdapter != null) {
                    outputAdapter.setSelectedPosition(position);
                }
            });
        }
    }

    public void _totaladd() {
        try {
            String[] etVal = new String[36];
            for (int i = 0; i < 36; i++) {
                etVal[i] = inputData.get(12 + i);
            }

            for (int i = 0; i < 8; i++) {
                for (int k = 0; k < 2; k++) {
                    int etOff = k * 18;
                    int colOffset = k * 4;

                    String val1 = _nor(etVal[etOff + 2 * i], "+", etVal[etOff + 2 * i + 2])
                                + _nor(etVal[etOff + 2 * i + 1], "-", etVal[etOff + 2 * i + 3]);

                    String val2 = _nor(etVal[etOff + 2 * i], "-", etVal[etOff + 2 * i + 2])
                                + _nor(etVal[etOff + 2 * i + 1], "+", etVal[etOff + 2 * i + 3]);

                    String val3 = _nor(etVal[etOff + i], "+", etVal[etOff + i + 2])
                                + _nor(etVal[etOff + i + 8], "+", etVal[etOff + i + 10]);

                    String val4 = _nor(etVal[etOff + i], "-", etVal[etOff + i + 2])
                                + _nor(etVal[etOff + i + 8], "-", etVal[etOff + i + 10]);

                    int baseIndex = (8 * i) + colOffset;
                    outputData.set(baseIndex, val1);
                    outputData.set(baseIndex + 1, val2);
                    outputData.set(baseIndex + 2, val3);
                    outputData.set(baseIndex + 3, val4);
                }
            }
            if (outputAdapter != null) outputAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public String _nor(final String sa, final String k, final String sb) {
        if (sa != null && !sa.trim().isEmpty() && sb != null && !sb.trim().isEmpty()) {
            try {
                double valueA = Double.parseDouble(sa.trim());
                double valueB = Double.parseDouble(sb.trim());
                if ("-".equals(k)) {
                    long result = Math.abs((long) (valueA - valueB));
                    return String.valueOf(result);
                } else if ("+".equals(k)) {
                    long result = (long) (valueA + valueB);
                    if (String.valueOf(result).length() == 2) {
                        result %= 10;
                    }
                    return String.valueOf(result);
                }
            } catch (NumberFormatException e) {
                return "";
            }
        }
        return "";
    }

    public void _backupRestore(final String _action) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean isBackup = _action.equalsIgnoreCase("backup");

        for (int i = 0; i < 36; i++) {
            String key = "edittext" + (i + 1);
            if (isBackup) {
                String data = inputData.get(12 + i);
                if (!data.isEmpty()) {
                    editor.putString(key, data);
                }
            } else {
                if (sharedPreferences.contains(key)) {
                    inputData.set(12 + i, sharedPreferences.getString(key, ""));
                }
            }
        }
        if (isBackup) {
            editor.apply();
        } else {
            if (inputAdapter != null) inputAdapter.notifyDataSetChanged();
        }
    }

    public void _pasteNumbers(final String _copied) {
        if (_copied == null) return;
        String copy = _copied.trim();
        int idx = 0;
        if (copy.matches(".*[^0-9].*")) {
            Matcher m = Pattern.compile("\\d").matcher(copy);
            while (m.find() && idx < 36) {
                inputData.set(12 + idx, m.group());
                idx++;
            }
        } else {
            for (int pos = 0; pos < copy.length() && idx < 36; pos++) {
                inputData.set(12 + idx, String.valueOf(copy.charAt(pos)));
                idx++;
            }
        }
        if (inputAdapter != null) inputAdapter.notifyDataSetChanged();
    }

    private void applyAxisCutScan() {
        String[] originalValues = new String[64];
        for (int i = 0; i < 64; i++) {
            originalValues[i] = outputData.get(i).trim();
        }

        String[] newValues = new String[64];

        for (int i = 0; i < 64; i++) {
            String val = originalValues[i];
            if (val.length() != 2) {
                newValues[i] = val;
                continue;
            }
            char d1 = val.charAt(0);
            char d2 = val.charAt(1);
            char c1 = getCut(d1);
            char c2 = getCut(d2);

            int row = i / 8;
            int col = i % 8;

            HashSet<Character> d1AxisDigits = new HashSet<>();
            HashSet<Character> d2AxisDigits = new HashSet<>();

            for (int r = 0; r < 8; r++) {
                if (r == row) continue;
                String nVal = originalValues[r * 8 + col];
                if (!nVal.isEmpty()) d1AxisDigits.add(nVal.charAt(0));
                if (nVal.length() >= 2) d2AxisDigits.add(nVal.charAt(1));
            }

            for (int c = 0; c < 8; c++) {
                if (c == col) continue;
                String nVal = originalValues[row * 8 + c];
                if (!nVal.isEmpty()) d1AxisDigits.add(nVal.charAt(0));
                if (nVal.length() >= 2) d2AxisDigits.add(nVal.charAt(1));
            }

            StringBuilder result = new StringBuilder();
            if (!d1AxisDigits.contains(c1)) result.append(d1);
            if (!d2AxisDigits.contains(c2)) result.append(d2);

            newValues[i] = result.toString();
        }

        for (int i = 0; i < 64; i++) {
            outputData.set(i, newValues[i]);
        }
        if (outputAdapter != null) outputAdapter.notifyDataSetChanged();
    }

    private char getCut(char d) {
        switch (d) {
            case '1': return '6';
            case '6': return '1';
            case '2': return '7';
            case '7': return '2';
            case '3': return '8';
            case '8': return '3';
            case '4': return '9';
            case '9': return '4';
            case '5': return '0';
            case '0': return '5';
            default: return d;
        }
    }
}
