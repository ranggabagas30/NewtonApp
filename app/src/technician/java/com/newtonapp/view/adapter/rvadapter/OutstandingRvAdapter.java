package com.newtonapp.view.adapter.rvadapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.newtonapp.R;
import com.newtonapp.model.rvmodel.OutstandingRvModelNew;

import java.util.ArrayList;

public class OutstandingRvAdapter extends BaseSingleViewTypeRvAdapter<OutstandingRvModelNew, OutstandingRvAdapter.ViewHolder> {

    private OnCallPicListener onCallPicListener;

    public OutstandingRvAdapter(ArrayList<OutstandingRvModelNew> data) {
        super(data, R.layout.item_outstanding_task);
    }

    public void setOnCallPicListener(OnCallPicListener onCallPicListener) {
        this.onCallPicListener = onCallPicListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull OutstandingRvModelNew outstanding) {
        holder.setCustomer(outstanding.getCustomer().getNama());
        holder.setAddress(outstanding.getCustomer().getAlamat());
        holder.setPicName(outstanding.getCustomer().getPic());
        holder.getBtnCallPic().setText(TextUtils.isEmpty(outstanding.getCustomer().getNoTelp())?
                        holder.itemView.getContext().getString(R.string.error_pic_phone_unavailable) : outstanding.getCustomer().getNoTelp());
        holder.getBtnCallPic().setOnClickListener(view -> {
            if (onCallPicListener != null) {
                onCallPicListener.onCall(outstanding.getCustomer().getNoTelp());
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView customer;
        private AppCompatTextView address;
        private AppCompatTextView picName;
        private AppCompatButton callPic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customer = itemView.findViewById(R.id.outstanding_item_tv_customer);
            address  = itemView.findViewById(R.id.outstanding_item_tv_address);
            picName  = itemView.findViewById(R.id.outstanding_item_tv_pic_name);
            callPic  = itemView.findViewById(R.id.outstanding_item_tv_pic_telp);
        }

        private void setCustomer(String customer) {
            this.customer.setText(customer);
        }

        private void setAddress(String address) {
            this.address.setText(address);
        }

        private void setPicName(String picName) { this.picName.setText(picName); }

        private AppCompatButton getBtnCallPic() {
            return callPic;
        }
    }

    public interface OnCallPicListener {
        void onCall(String picContactNumber);
    }
}
