package edu.orbital.studyshack;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.HouseViewHolder> {

    public static int[] HOUSE_IMAGES = {R.drawable.house1, R.drawable.house2, R.drawable.house3, R.drawable.house4, R.drawable.house5};

    public LinkedList<House> mHouseList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener (OnItemClickListener listener) {
        mListener = listener;
    }

    public static class HouseViewHolder extends RecyclerView.ViewHolder {

        public TextView mHouseLabelTextView;
        public TextView mDescriptionLabelTextView;
        public TextView mDescriptionTextView;
        public ImageView mHouseImageView;
        public TextView mTimeSpentThisWeekLabelTextView;
        public TextView mTimeSpentThisWeekTextTextView;
        public TextView mTimeToUpgradeLabelTextView;
        public TextView mTimeToUpgradeTextTextView;

        public HouseViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mHouseLabelTextView = itemView.findViewById(R.id.house_label);
            mDescriptionLabelTextView = itemView.findViewById(R.id.description_label);
            mDescriptionTextView = itemView.findViewById(R.id.description_text);
            mHouseImageView = itemView.findViewById(R.id.house_imageview);
            mTimeSpentThisWeekLabelTextView = itemView.findViewById(R.id.time_spent_this_week_label);
            mTimeSpentThisWeekTextTextView = itemView.findViewById(R.id.time_spent_this_week_text);
            mTimeToUpgradeLabelTextView = itemView.findViewById(R.id.time_to_upgrade_label);
            mTimeToUpgradeTextTextView = itemView.findViewById(R.id.time_to_upgrade_text);

            //Card's on click method
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        // extra code
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public HouseAdapter(LinkedList<House> houseList) {
        this.mHouseList = houseList;
    }

    @NonNull
    @Override
    public HouseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int view) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.house_item, parent, false);
        HouseViewHolder hvh = new HouseViewHolder(v, mListener);
        return hvh;
    }

    @Override
    public void onBindViewHolder(@NonNull HouseViewHolder holder, int position) {
        House currentHouse = mHouseList.get(position);
        holder.mHouseImageView.setImageResource(HouseAdapter.HOUSE_IMAGES[currentHouse.getLevel() - 1]);
        holder.mHouseLabelTextView.setText(currentHouse.getName());
        holder.mDescriptionTextView.setText(currentHouse.getDesc());

        int weeklyTime = currentHouse.getTotalTimeWeek();
        int hoursThisWeek = weeklyTime / 60;
        int minsThisWeek = weeklyTime % 60;
        holder.mTimeSpentThisWeekTextTextView.setText(hoursThisWeek + "h " + minsThisWeek + "m");


        // Time to next upgrade
        if (currentHouse.getLevel() == 5) {
            holder.mTimeToUpgradeTextTextView.setText("MAX LVL");
            return;
        }

        int timeToUpgrade = House.timeLimit(currentHouse.getLevel()) - currentHouse.getTotaltime();
        if (timeToUpgrade <= 0) {
            return;
        }

        int hoursToUpgrade = timeToUpgrade / 60;
        int minsToUpgrade = timeToUpgrade % 60;

        holder.mTimeToUpgradeTextTextView.setText(hoursToUpgrade +"h " + minsToUpgrade +"m");
    }

    @Override
    public int getItemCount() {
        return mHouseList.size();
    }
}
