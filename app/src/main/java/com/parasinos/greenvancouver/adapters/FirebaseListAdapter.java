package com.parasinos.greenvancouver.adapters;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public abstract class FirebaseListAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private List<T> list;

    FirebaseListAdapter(List<T> list) {
        this.list = list;
    }

    T get(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void add(T object, T previousObject) {
        int pos = previousObject == null ? 0 : list.indexOf(previousObject) + 1;
        list.add(pos, object);
        notifyItemInserted(pos);
    }

    public T update(T object) {
        int pos = list.indexOf(object);
        T old = list.get(pos);
        list.set(pos, object);
        notifyItemChanged(pos);
        return old;
    }

    public void remove(T object) {
        int pos = list.indexOf(object);
        list.remove(object);
        notifyItemRemoved(pos);
    }

    public void move(T object, T previousObject) {
        int oldPos = list.indexOf(object);
        int newPos = previousObject == null ? 0 : list.indexOf(previousObject) + 1;

        int cur = Math.min(oldPos, newPos);
        int end = Math.max(oldPos, newPos);
        object = list.get(cur);
        while (cur < end) {
            list.set(cur, list.get(cur + 1));
            cur++;
        }
        list.set(end, object);
        notifyItemMoved(oldPos, newPos);
    }
}
