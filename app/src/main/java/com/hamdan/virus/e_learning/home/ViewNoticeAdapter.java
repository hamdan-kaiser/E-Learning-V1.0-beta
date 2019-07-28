package com.hamdan.virus.e_learning.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hamdan.virus.e_learning.R;
import com.hamdan.virus.e_learning.home.teacher.AddClassModel;
import com.hamdan.virus.e_learning.home.teacher.AddNoticeModel;
import com.hamdan.virus.e_learning.home.teacher.ViewClassesTeacherAdapter;

import java.util.ArrayList;

public class ViewNoticeAdapter extends ArrayAdapter<AddNoticeModel> {

    public ArrayList<AddNoticeModel> allDetails;
    private Context context;
    private ViewHolder viewHolder;

    public ViewNoticeAdapter(@NonNull Context ctx, ArrayList<AddNoticeModel> all_Details) {
        super(ctx, R.layout.single_notice_adapter, all_Details);

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
            view = LayoutInflater.from(getContext()).inflate(R.layout.single_notice_adapter, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.noticeNameText = view.findViewById(R.id.noticeNameText);
            viewHolder.noticeText = view.findViewById(R.id.noticeText);
            viewHolder.subject = view.findViewById(R.id.Subject);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.noticeNameText.setText("Name: " + allDetails.get(position).getName());
        viewHolder.noticeText.setText("Notice: " + allDetails.get(position).getNotice());

        return view;
    }

    public class ViewHolder {
        TextView noticeNameText, noticeText,subject;
    }
}
