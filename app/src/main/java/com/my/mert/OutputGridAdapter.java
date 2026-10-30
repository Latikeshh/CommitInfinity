package com.my.mert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import laki.vinox.v1.R;

public class OutputGridAdapter extends BaseAdapter {

    private final List<String> outputValues;
    private final LayoutInflater inflater;
    private int cellWidth = -1;
    private int cellHeight = -1;
    private int selectedPosition = -1;

    public OutputGridAdapter(Context context, List<String> outputValues) {
        this.outputValues = outputValues;
        this.inflater = LayoutInflater.from(context);
    }

    public void setCellDimensions(int width, int height) {
        this.cellWidth = width;
        this.cellHeight = height;
    }

    public void setSelectedPosition(int position) {
        if (this.selectedPosition == position) {
            this.selectedPosition = -1; // Toggle off if clicked again
        } else {
            this.selectedPosition = position;
        }
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public List<String> getOutputValues() {
        return outputValues;
    }

    @Override
    public int getCount() {
        return outputValues.size();
    }

    @Override
    public Object getItem(int position) {
        return outputValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView tvCell;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_output_cell, parent, false);
            holder = new ViewHolder();
            holder.tvCell = convertView.findViewById(R.id.tvOutputCell);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Set dimensions if specified
        if (cellWidth > 0 && cellHeight > 0) {
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(cellWidth, cellHeight);
            convertView.setLayoutParams(params);
        }

        // Bind calculated output text value to the TextView
        holder.tvCell.setText(outputValues.get(position));
        LakiActivity.span(holder.tvCell);

        // Highlight selected cell and its vertical & horizontal axis neighbours
        if (selectedPosition != -1) {
            int selectedRow = selectedPosition / 8;
            int selectedCol = selectedPosition % 8;
            int currentRow = position / 8;
            int currentCol = position % 8;

            if (position == selectedPosition) {
                holder.tvCell.setBackgroundResource(R.drawable.cell_border_selected);
            } else if (currentRow == selectedRow || currentCol == selectedCol) {
                holder.tvCell.setBackgroundResource(R.drawable.cell_border_axis);
            } else {
                holder.tvCell.setBackgroundResource(R.drawable.cell_border_output);
            }
        } else {
            holder.tvCell.setBackgroundResource(R.drawable.cell_border_output);
        }

        return convertView;
    }
}
