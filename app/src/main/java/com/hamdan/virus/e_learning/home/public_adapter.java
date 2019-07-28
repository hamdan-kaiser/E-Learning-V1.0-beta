package com.hamdan.virus.e_learning.home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hamdan.virus.e_learning.R;
import com.hamdan.virus.e_learning.SendInteltodb;
import com.hamdan.virus.e_learning.home.teacher.AddClassModel;
import com.hamdan.virus.e_learning.home.teacher.ViewClassesTeacherAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class public_adapter extends ArrayAdapter<SendInteltodb>  {

    public ArrayList<SendInteltodb> allDetails;
    public ArrayList<AddClassModel> eventList;
    private Context context;
    ViewClassesTeacherAdapter adapter;
    private com.hamdan.virus.e_learning.home.public_adapter.ViewHolder viewHolder;

    public public_adapter(@NonNull Context ctx, ArrayList<SendInteltodb> all_Details) {

        super(ctx,R.layout.activity_public_adapter, all_Details);

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
            view = LayoutInflater.from(getContext()).inflate(R.layout.activity_public_adapter, parent, false);
            viewHolder = new com.hamdan.virus.e_learning.home.public_adapter.ViewHolder();
            viewHolder.teacherName = view.findViewById(R.id.publicteacherNameText);
            viewHolder.teacherEmail = view.findViewById(R.id.publicTeacherEmail);

            view.setTag(viewHolder);
        } else {
            viewHolder = (com.hamdan.virus.e_learning.home.public_adapter.ViewHolder) view.getTag();
        }

        viewHolder.teacherName.setText("Name: " + allDetails.get(position).getUserName());
        viewHolder.teacherEmail.setText("Email: " + allDetails.get(position).getUserEmail());

        Log.e("thgcjrd:   "," "+allDetails.get(position).getUserName());

     /*   viewHolder.chatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("occupation","student");
                intent.putExtra("name","" + allDetails.get(position).getName());
                intent.putExtra("email","" + allDetails.get(position).getEmail());
                context.startActivity(intent);
            }
        });*/

        return view;
    }

    public class ViewHolder {
        TextView teacherName, teacherEmail;
        //LinearLayout chatLayout;

    }

}
