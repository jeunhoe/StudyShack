package edu.orbital.studyshack;

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

    public static class HouseViewHolder extends RecyclerView.ViewHolder {

        public TextView mHouseLabelTextView;
        public TextView mDescriptionLabelTextView;
        public TextView mDescriptionTextView;
        public ImageView mHouseImageView;

        public HouseViewHolder(@NonNull View itemView) {
            super(itemView);
            mHouseLabelTextView = itemView.findViewById(R.id.house_label);
            mDescriptionLabelTextView = itemView.findViewById(R.id.description_label);
            mDescriptionTextView = itemView.findViewById(R.id.description_text);
            mHouseImageView = itemView.findViewById(R.id.house_imageview);
        }
    }

    public HouseAdapter(LinkedList<House> houseList) {
        this.mHouseList = houseList;
    }

    @NonNull
    @Override
    public HouseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int view) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.house_item, parent, false);
        HouseViewHolder hvh = new HouseViewHolder(v);
        return hvh;
    }

    @Override
    public void onBindViewHolder(@NonNull HouseViewHolder holder, int position) {
        House currentHouse = mHouseList.get(position);
        holder.mHouseImageView.setImageResource(HouseAdapter.HOUSE_IMAGES[currentHouse.getLevel() - 1]);
        holder.mHouseLabelTextView.setText(currentHouse.getName());
        holder.mDescriptionTextView.setText(currentHouse.getDesc());
    }

    @Override
    public int getItemCount() {
        return mHouseList.size();
    }
}
