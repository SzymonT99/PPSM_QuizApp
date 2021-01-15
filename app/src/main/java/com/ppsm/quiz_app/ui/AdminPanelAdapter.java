package com.ppsm.quiz_app.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ppsm.quiz_app.R;
import com.ppsm.quiz_app.model.Question;
import java.util.List;


public class AdminPanelAdapter extends RecyclerView.Adapter<AdminPanelAdapter.AdminPanelViewHolder>{

    private List<Question> questionsList;
    private OnItemClickListener mOnItemClickListener;

    public AdminPanelAdapter(List<Question> questionsList, OnItemClickListener mOnItemClickListener) {
        this.questionsList = questionsList;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @NonNull
    @Override
    public AdminPanelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_panel_item,
                parent, false);

        AdminPanelViewHolder avh = new AdminPanelViewHolder(v, mOnItemClickListener);;
        return avh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdminPanelViewHolder holder, int position) {

        Question currentItem = questionsList.get(position);

        int currentIndex = position + 1;
        holder.index.setText(Integer.toString(currentIndex)+".");
        holder.author.setText(currentItem.getAuthor());
        holder.questionContent.setText(currentItem.getContent().length() > 56
                ? currentItem.getContent().substring(0,56) + "..."
                : currentItem.getContent());

    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }


    public static class AdminPanelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView index;
        public TextView author;
        public TextView questionContent;
        public OnItemClickListener onItemClickListener;

        public AdminPanelViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            index = itemView.findViewById(R.id.rec_index_question_text);
            author = itemView.findViewById(R.id.rec_author_text);
            questionContent = itemView.findViewById(R.id.rec_question_text);
            this.onItemClickListener = onItemClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }

    public void removeItem(int position) {
        questionsList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Question item, int position) {
        questionsList.add(position, item);
        notifyItemInserted(position);
    }

    public List<Question> getQuestionsList(){
        return questionsList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
