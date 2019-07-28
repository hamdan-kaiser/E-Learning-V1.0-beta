package com.hamdan.virus.e_learning.home.student;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hamdan.virus.e_learning.R;

import java.util.ArrayList;

public class TeacherListAdapter extends ArrayAdapter<TeacherListModel> {

    public ArrayList<TeacherListModel> allDetails;
    private Context context;
    private ViewHolder viewHolder;

    public TeacherListAdapter(@NonNull Context ctx, ArrayList<TeacherListModel> all_Details) {
        super(ctx, R.layout.teacher_list_adapter, all_Details);

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
            view = LayoutInflater.from(getContext()).inflate(R.layout.teacher_list_adapter, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.teacherName = view.findViewById(R.id.publicteacherNameText);
            viewHolder.teacherEmail = view.findViewById(R.id.publicTeacherEmail);
            viewHolder.chatLayout = view.findViewById(R.id.chatLayout);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.teacherName.setText("Name: " + allDetails.get(position).getName());
        viewHolder.teacherEmail.setText("Email: " + allDetails.get(position).getEmail());

        viewHolder.chatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("occupation","student");
                intent.putExtra("name","" + allDetails.get(position).getName());
                intent.putExtra("email","" + allDetails.get(position).getEmail());
                context.startActivity(intent);
            }
        });

        return view;
    }

    public class ViewHolder {
        TextView teacherName, teacherEmail;
        LinearLayout chatLayout;

    }

}
