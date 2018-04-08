package com.example.root.railways;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 4/1/18.
 */

class TrainCancelledAdapter extends RecyclerView.Adapter<TrainCancelledAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "Adaptr" ;
    private final List<Train> trains;
    private List<Train> trainsListFiltered;
   private TrainsAdapterListener listener;
    public TrainCancelledAdapter(List<Train> trains,TrainsAdapterListener listener) {
        this.trains = trains;
        this.trainsListFiltered = trains;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView trainName;
        private final TextView trainNumber;
        private final RelativeLayout row;

        public ViewHolder(View itemView) {
            super(itemView);
            trainName = (TextView)itemView.findViewById(R.id.train_name);
            trainNumber = (TextView)itemView.findViewById(R.id.train_number);
            row = (RelativeLayout)itemView.findViewById(R.id.row);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTrainSelected(trainsListFiltered.get(getAdapterPosition()));
                }
            });
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
     View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tc_row_item,parent,false);
     return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: "+position);
        
        try{
            final Train train  = trainsListFiltered.get(position);
            holder.trainName.setText(train.getName().toString());
            holder.trainNumber.setText(train.getNumber().toString());

        }
        catch (Exception e)
        {
            Log.d(TAG, "onBindViewHolder: null");
        }

      //  Log.d(TAG, "onBindViewHolder: "+trains.get(position).getName().toString());
    }

    @Override
    public int getItemCount() {
        return trainsListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return  new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String Charstring = constraint.toString();
                if(Charstring == null)
                {
                    trainsListFiltered = trains;
                }
                else
                {
                    List<Train> filteredList = new ArrayList<>();
                    for(Train row : trains)
                    {
                       if( row.getName().contains(Charstring.toString().toUpperCase()) || row.getNumber().contains(constraint))
                       {
                           filteredList.add(row);
                       }
                    }
                    trainsListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = trainsListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                trainsListFiltered= (ArrayList<Train>)results.values;
                notifyDataSetChanged();
            }
        };
    }
    public interface TrainsAdapterListener {
        void onTrainSelected(Train train);
    }
}
