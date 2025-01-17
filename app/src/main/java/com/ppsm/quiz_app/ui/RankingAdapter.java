package com.ppsm.quiz_app.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.ppsm.quiz_app.R;
import com.ppsm.quiz_app.model.QuizResult;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankingViewHolder> {

    public ArrayList<QuizResult> rankingList;
    public String currentLogin;

    public RankingAdapter(ArrayList<QuizResult> rankingList, String currentLogin) {
        this.rankingList = rankingList;
        this.currentLogin = currentLogin;
    }

    @NonNull
    @Override
    public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_item,
            parent, false);

        RankingViewHolder rvh = new RankingViewHolder(v);
        return rvh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RankingViewHolder holder, int position) {

        QuizResult currentItem = rankingList.get(position);

        int currentRanking = position + 1;
        holder.position.setText(Integer.toString(currentRanking)+".");
        holder.userName.setText(currentItem.getUserName());
        holder.points.setText(currentItem.getPoints().toString() + " pkt.");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd", Locale.ENGLISH);
        String formattedDate = simpleDateFormat.format(currentItem.getDate());
        holder.date.setText(formattedDate);

        if (currentRanking == 1) {
            holder.container.setBackgroundResource(R.drawable.ranking_item_p1_bc);
            holder.podiumImage.setImageResource(R.drawable.master);
            holder.podiumImage.setMinimumWidth(40);
            holder.podiumImage.setMinimumHeight(40);
        }
        else if (currentRanking == 2) {
            holder.container.setBackgroundResource(R.drawable.ranking_item_p2_bc);
            holder.podiumImage.setImageResource(R.drawable.silver);
            holder.podiumImage.setMinimumWidth(40);
            holder.podiumImage.setMinimumHeight(40);

        }
        else if (currentRanking == 3) {
            holder.container.setBackgroundResource(R.drawable.ranking_item_p3_bc);
            holder.podiumImage.setImageResource(R.drawable.bronze);
            holder.podiumImage.setMinimumWidth(40);
            holder.podiumImage.setMinimumHeight(40);
        }
        else {
            holder.container.setBackgroundResource(R.drawable.ranking_item_bc);
            holder.podiumImage.setImageResource(0);
        }

        if (currentItem.getUserName().equals(currentLogin)) {
            holder.userName.setTextColor(Color.RED);
        }
        else {
            holder.userName.setTextColor(Color.BLACK);
        }

    }

    @Override
    public int getItemCount() {
        return rankingList.size();
    }

    public static class RankingViewHolder extends RecyclerView.ViewHolder {

        public TextView position;
        public TextView userName;
        public TextView points;
        public TextView date;
        public ImageView podiumImage;
        public ConstraintLayout container;

        public RankingViewHolder(@NonNull View itemView) {
            super(itemView);

            position = itemView.findViewById(R.id.rec_position_text);
            userName = itemView.findViewById(R.id.rec_user_name_text);
            points = itemView.findViewById(R.id.rec_points_text);
            date = itemView.findViewById(R.id.rec_date_text);
            podiumImage = itemView.findViewById(R.id.image_podium);
            container = itemView.findViewById(R.id.ranking_item_container);

        }
    }

}
