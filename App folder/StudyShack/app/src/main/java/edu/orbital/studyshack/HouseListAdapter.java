package edu.orbital.studyshack;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class HouseListAdapter extends RecyclerView.Adapter<HouseListAdapter.HouseViewHolder> {

    private final List<House> mHouseList;
    private LayoutInflater mInflater;

    public HouseListAdapter(Context context, List<House> houseList) {
        mInflater = LayoutInflater.from(context);
        mHouseList = houseList;
    }

    @NonNull
    @Override
    public HouseListAdapter.HouseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.housecardview_item,
                parent, false);
        return new HouseViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(HouseViewHolder holder, int position) {
        House mCurrent = mHouseList.get(position);
        holder.houseItemView.setText(mCurrent.getName());
    }

    @Override
    public int getItemCount() {
        return mHouseList.size();
    }

    class HouseViewHolder extends RecyclerView.ViewHolder {
        public final TextView houseItemView;
        final HouseListAdapter mAdapter;

        public HouseViewHolder(View itemView, HouseListAdapter adapter) {
            super(itemView);
            houseItemView = itemView.findViewById(R.id.house_name);
            this.mAdapter = adapter;
        }
    }
}
