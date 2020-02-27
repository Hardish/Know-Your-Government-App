package com.example.government;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GovernmentViewHolder  extends RecyclerView.ViewHolder{

    public TextView title;
    public TextView name;
    public TextView party;


    public GovernmentViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.txttitle);
        name = itemView.findViewById(R.id.txtname);
        party = itemView.findViewById(R.id.txtparty);

    }
}
