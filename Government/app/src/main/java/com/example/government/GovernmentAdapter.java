package com.example.government;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GovernmentAdapter  extends RecyclerView.Adapter<GovernmentViewHolder>{

    private static final String TAG = "GovernmentAdapter";
    private List<Government> governmentList;
    private MainActivity mainAct;

    public GovernmentAdapter(List<Government> governmentList, MainActivity mainAct) {
        this.governmentList = governmentList;
        this.mainAct = mainAct;
    }

    @NonNull
    @Override
    public GovernmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: CREATING NEW");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_item, parent, false);

        itemView.setOnClickListener(mainAct);

        return new GovernmentViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull GovernmentViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: SETTING DATA");

        Government selectedGov = governmentList.get(position);

        holder.title.setText(selectedGov.getTitle());
        holder.name.setText(selectedGov.getName());
        String partyString = "(" + selectedGov.getParty() + ")";
        //holder.party.setText(selectedGov.getParty());
        holder.party.setText(partyString);
    }

    @Override
    public int getItemCount() {
        return governmentList.size();
    }
}
