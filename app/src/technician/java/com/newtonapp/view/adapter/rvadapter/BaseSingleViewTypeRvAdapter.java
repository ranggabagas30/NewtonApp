package com.newtonapp.view.adapter.rvadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class BaseSingleViewTypeRvAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    @NonNull
    private int itemLayoutRes;

    @NonNull
    private ArrayList<T> data;
    private OnItemClickListener<T> onItemClickListener;

    public BaseSingleViewTypeRvAdapter(@NonNull ArrayList<T> data, @NonNull int itemLayoutRes) {
        this.data = data;
        this.itemLayoutRes = itemLayoutRes;
    }

    public void setData(@NonNull ArrayList<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return Collections.unmodifiableList(data);
    }

    public boolean updateData(@NonNull T oldDataItem, @NonNull T newDataItem) {
        int index = data.indexOf(oldDataItem);
        if (data.isEmpty() || index == -1) return false;
        data.set(index, newDataItem);
        return true;
    }

    public boolean addData(@NonNull T... newData) {
        List<T> tempData = Arrays.asList(newData);
        return addData(tempData);
    }

    public boolean addData(@NonNull List<T> newData) {
        int insertPosition = data.size()-1;
        boolean isAdded =  data.addAll(newData);
        if (isAdded) notifyItemInserted(insertPosition);
        return isAdded;
    }

    public boolean removeData(@NonNull T dataItem) {
        int removePosition = data.indexOf(dataItem);
        boolean isRemoved  = data.remove(dataItem);
        if (isRemoved) notifyItemRemoved(removePosition);
        return isRemoved;
    }

    public T removeData(int removeDataItemIndex) {
        T removedDataItem = data.remove(removeDataItemIndex);
        if (removedDataItem != null) {
            notifyItemRemoved(removeDataItemIndex);
        }
        return removedDataItem;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public abstract VH onCreateViewHolder(@NonNull View itemView);
    public abstract void onBindViewHolder(@NonNull VH holder, int position, @NonNull T dataItem);


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(itemLayoutRes, parent, false);
        return onCreateViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (data.get(position) == null) throw new NullPointerException("data at position " + position + " can not be null");
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(view -> onItemClickListener.onItemClick(data.get(holder.getAdapterPosition())));
        }
        onBindViewHolder(holder, position, data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T dataItem);
    }
}
