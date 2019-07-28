package com.hamdan.virus.e_learning.home.student;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hamdan.virus.e_learning.R;

public class ChatHolder extends RecyclerView.ViewHolder  {

    public TextView leftText,rightText;

    public ChatHolder(View itemView) {
        super(itemView);

        leftText = itemView.findViewById(R.id.leftText);
        rightText = itemView.findViewById(R.id.rightText);


    }
}
