package com.newtonapp.view.adapter.rvadapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.newtonapp.R;
import com.newtonapp.model.rvmodel.OutstandingRvModel;

import java.util.ArrayList;

public class OutstandingRvAdapter extends BaseSingleViewTypeRvAdapter<OutstandingRvModel, OutstandingRvAdapter.ViewHolder> {

    public OutstandingRvAdapter(ArrayList<OutstandingRvModel> data, int itemLayoutRes) {
        super(data, itemLayoutRes);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull OutstandingRvModel outstanding) {
        holder.setCustomer(outstanding.getCustomer());
        holder.setAddress(outstanding.getAddress());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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
}
