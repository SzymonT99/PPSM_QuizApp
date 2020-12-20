package com.ppsm.quiz_app.ui;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ppsm.quiz_app.R;
import com.ppsm.quiz_app.model.QuizResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static java.security.AccessController.getContext;

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
        holder.position.setText(Integer.toString(currentRanking));
        holder.userName.setText(currentItem.getUserName());
        if (currentItem.getUserName().equals(currentLogin)) holder.userName.setTextColor(Color.RED);

        holder.points.setText(currentItem.getPoints().toString() + " pkt.");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd", Locale.ENGLISH);
        String formattedDate = simpleDateFormat.format(currentItem.getDate());
        holder.date.setText(formattedDate);

        if (currentRanking == 1) { holder.podiumImage.setBackgroundResource(R.drawable.master);
            holder.podiumImage.setImageResource(R.drawable.master);
            holder.podiumImage.setMinimumWidth(40);
            holder.podiumImage.setMinimumHeight(40);
        }
        if (currentRanking == 2) {
            holder.podiumImage.setImageResource(R.drawable.silver);
            holder.podiumImage.setMinimumWidth(40);
            holder.podiumImage.setMinimumHeight(40);

        }
        if (currentRanking == 3) {
            holder.podiumImage.setImageResource(R.drawable.bronze);
            holder.podiumImage.setMinimumWidth(40);
            holder.podiumImage.setMinimumHeight(40);
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

        public RankingViewHolder(@NonNull View itemView) {
            super(itemView);

            position = itemView.findViewById(R.id.rec_position_text);
            userName = itemView.findViewById(R.id.rec_user_name_text);
            points = itemView.findViewById(R.id.rec_points_text);
            date = itemView.findViewById(R.id.rec_date_text);
            podiumImage = itemView.findViewById(R.id.image_podium);

        }
    }

}
