package com.my.mert;

import android.content.Context;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;

import java.util.List;

import laki.vinox.v1.R;

public class InputGridAdapter extends BaseAdapter {

    private final Context context;
    private final List<String> inputValues;
    private final LayoutInflater inflater;
    private int cellWidth = -1;
    private int cellHeight = -1;
    private ViewGroup parentGrid;

    private static final int[] DIGIT_COLORS = {
        0xFFFFFF00, // 0,5
        0xFF00E5FF, // 1,6
        0xFF00FF00, // 2,7
        0xFFFF6D00, // 3,8
        0xFFFF4081  // 4,9
    };

    public InputGridAdapter(Context context, List<String> inputValues) {
        this.context = context;
        this.inputValues = inputValues;
        this.inflater = LayoutInflater.from(context);
    }

    public void setCellDimensions(int width, int height) {
        this.cellWidth = width;
        this.cellHeight = height;
    }

    public List<String> getInputValues() {
        return inputValues;
    }

    public static void applySpanToEditable(Editable s) {
        if (s == null || s.length() == 0) return;
        // Remove existing ForegroundColorSpans
        ForegroundColorSpan[] spans = s.getSpans(0, s.length(), ForegroundColorSpan.class);
        for (ForegroundColorSpan span : spans) {
            s.removeSpan(span);
        }
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (Character.isDigit(ch)) {
                int digit = ch - '0';
                int color = DIGIT_COLORS[Math.abs(digit) % 5];
                s.setSpan(
                    new ForegroundColorSpan(color),
                    i,
                    i + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }
        }
    }

    public void _req(final EditText _e, final String _a) {
        if (_e != null && _a.equals("")) {
            _e.setFocusable(true);
            _e.setFocusableInTouchMode(true);
            _e.requestFocus();
            _e.post(() -> {
                InputMethodManager imm = (InputMethodManager) _e.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(_e, InputMethodManager.SHOW_IMPLICIT);
                }
            });
        }
    }

    public void _sed(final EditText _e, final String _s) {
        if (_e != null && _s.length() == 1) {
            _e.setFocusable(true);
            _e.setFocusableInTouchMode(true);
            _e.requestFocus();
            _e.post(() -> {
                InputMethodManager imm = (InputMethodManager) _e.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(_e, InputMethodManager.SHOW_IMPLICIT);
                }
            });
        }
    }

    @Override
    public int getCount() {
        return inputValues.size();
    }

    @Override
    public Object getItem(int position) {
        return inputValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        EditText etCell;
        CellTextWatcher watcher;
    }

    private class CellTextWatcher implements TextWatcher {
        private int position = -1;
        private boolean isPasted = false;

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Check if count > 1 (paste operation from clipboard)
            if (count > 1) {
                isPasted = true;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (position >= 0 && position < inputValues.size()) {
                String val = s.toString();

                // If text was pasted from clipboard, run _pasteNumbers
                if (isPasted && val.length() > 1) {
                    isPasted = false;
                    if (context instanceof MainActivity) {
                        ((MainActivity) context)._pasteNumbers(val);
                    }
                    return;
                }
                isPasted = false;

                // When user types manually, enforce max 1 digit per cell
                if (val.length() > 1) {
                    String singleChar = String.valueOf(val.charAt(val.length() - 1));
                    s.replace(0, s.length(), singleChar);
                    return;
                }

                applySpanToEditable(s);
                inputValues.set(position, val);

                // When 1 digit is filled, move focus to the next EditText using _sed
                if (val.length() == 1 && parentGrid != null) {
                    int nextPosition = position + 1;
                    if (nextPosition < inputValues.size()) {
                        int firstVisible = ((AbsListView) parentGrid).getFirstVisiblePosition();
                        View nextChild = parentGrid.getChildAt(nextPosition - firstVisible);
                        if (nextChild != null) {
                            EditText nextEt = nextChild.findViewById(R.id.etInputCell);
                            if (nextEt != null) {
                                _sed(nextEt, val);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        this.parentGrid = parent;
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_input_cell, parent, false);
            holder = new ViewHolder();
            holder.etCell = convertView.findViewById(R.id.etInputCell);
            holder.watcher = new CellTextWatcher();
            holder.etCell.addTextChangedListener(holder.watcher);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Hide first 12 cells (1st row)
        if (position < 12) {
            convertView.setVisibility(View.GONE);
            convertView.setLayoutParams(new AbsListView.LayoutParams(0, 0));
        } else {
            convertView.setVisibility(View.VISIBLE);
            if (cellWidth > 0 && cellHeight > 0) {
                AbsListView.LayoutParams params = new AbsListView.LayoutParams(cellWidth, cellHeight);
                convertView.setLayoutParams(params);
            }

            // Set alphabetical hints starting from 2nd row (a, b, c, d, e, f, ...)
            int visibleIndex = position - 12;
            char hintLetter = (char) ('a' + (visibleIndex % 26));
            holder.etCell.setHint(String.valueOf(hintLetter));
        }

        // Ensure cell is focusable for human touch interactions
        holder.etCell.setFocusable(true);
        holder.etCell.setFocusableInTouchMode(true);

        // Unset position to avoid unwanted updates during setText
        holder.watcher.setPosition(-1);
        holder.etCell.setText(inputValues.get(position));
        if (position >= 12 && !inputValues.get(position).isEmpty()) {
            applySpanToEditable(holder.etCell.getText());
        }
        holder.watcher.setPosition(position);

        return convertView;
    }
}
