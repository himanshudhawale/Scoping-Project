package com.example.scoping_project;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder> {

    static ArrayList<Score> ShoppingItemArrayList;
    static Score score;
    Context mContext;


    public ScoreAdapter( Context context, ArrayList<Score> scoreList1)
    {
        this.ShoppingItemArrayList=scoreList1;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.score,parent,false);
       ScoreAdapter.ViewHolder viewHolder=new ScoreAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        score= ShoppingItemArrayList.get(position);

        if (score!=null){

            holder.teamname.setText(score.getTeamName());
            holder.score.setText("$"+score.getScore());

        }
    }



    @Override
    public int getItemCount() {
        return ShoppingItemArrayList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView teamname, score;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            teamname = itemView.findViewById(R.id.teamNameTextView);
            score = itemView.findViewById(R.id.scoreTextView);

        }
    }

}
