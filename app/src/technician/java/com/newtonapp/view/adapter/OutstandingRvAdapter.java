package com.newtonapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.newtonapp.R;
import com.newtonapp.model.OutstandingRvModel;

import java.util.ArrayList;

public class OutstandingRvAdapter extends RecyclerView.Adapter<OutstandingRvAdapter.ViewHolder> {

    private OnClickListener onClickListener;
    private ArrayList<OutstandingRvModel> outstandingList;

    public OutstandingRvAdapter(ArrayList<OutstandingRvModel> outstandingList) {
        this.outstandingList = outstandingList;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View outstandingItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_outstanding_task, parent, false);
        return new ViewHolder(outstandingItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OutstandingRvModel outstanding = outstandingList.get(position);
        holder.setCustomer(outstanding.getCustomer());
        holder.setAddress(outstanding.getAddress());
        holder.itemView.setOnClickListener(view -> onClickListener.onClick(outstanding));
    }

    @Override
    public int getItemCount() {
        if (outstandingList == null) return 0;
        else return outstandingList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView customer;
        private AppCompatTextView address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customer = itemView.findViewById(R.id.outstanding_item_tv_customer);
            address  = itemView.findViewById(R.id.outstanding_item_tv_address);
        }

        private void setCustomer(String customer) {
            this.customer.setText(customer);
        }

        private void setAddress(String address) {
            this.address.setText(address);
        }
    }

    public interface OnClickListener {
        void onClick(OutstandingRvModel outstanding);
    }
}
