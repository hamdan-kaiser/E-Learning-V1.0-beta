package com.hamdan.virus.e_learning.home.teacher;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hamdan.virus.e_learning.R;

import java.util.ArrayList;
import java.util.List;

public class ViewClassesTeacherAdapter extends ArrayAdapter<AddClassModel> {

    public ArrayList<AddClassModel> allDetails;
    private Context context;
    private ViewHolder viewHolder;

    public ViewClassesTeacherAdapter(@NonNull Context ctx, ArrayList<AddClassModel> all_Details) {
        super(ctx, R.layout.teacher_class_adapter, all_Details);

        context = ctx;
        allDetails = all_Details;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (allDetails.size() > 0) {
            return allDetails.size();
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public View getView(final int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.teacher_class_adapter, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.eventNameText = view.findViewById(R.id.eventNameText);
            viewHolder.eventMsgText = view.findViewById(R.id.eventMsgText);
            viewHolder.eventDateText = view.findViewById(R.id.eventDateText);
            viewHolder.eventTimeText = view.findViewById(R.id.eventTimeText);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.eventNameText.setText("Class: " + allDetails.get(position).getEventName());
        viewHolder.eventMsgText.setText("Message: " + allDetails.get(position).getEventMessage());
        viewHolder.eventDateText.setText("" + allDetails.get(position).getEventDate());
        viewHolder.eventTimeText.setText("" + allDetails.get(position).getEventTime());

        return view;
    }

    public class ViewHolder {
        TextView eventNameText, eventMsgText, eventTimeText, eventDateText;
    }
}
