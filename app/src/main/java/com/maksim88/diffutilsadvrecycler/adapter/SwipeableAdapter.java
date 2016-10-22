package com.maksim88.diffutilsadvrecycler.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionRemoveItem;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.annotation.SwipeableItemDrawableTypes;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.annotation.SwipeableItemResults;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;
import com.maksim88.diffutilsadvrecycler.R;
import com.maksim88.diffutilsadvrecycler.common.DiffCallback;
import com.maksim88.diffutilsadvrecycler.viewmodel.UserListItem;

import java.util.List;

/**
 * Created by maksim on 22.10.16.
 */

public class SwipeableAdapter extends RecyclerView.Adapter<SwipeableAdapter.ViewHolder>
        implements SwipeableItemAdapter<SwipeableAdapter.ViewHolder> {

    public static String KEY_USERNAME = "username";
    public static String KEY_IMAGEURL = "imageurl";

    private List<UserListItem> items;

    private Context context;

    public SwipeableAdapter(Context context, List<UserListItem> items) {
        setHasStableIds(true); // this is required for swiping feature.
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_user, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserListItem item = items.get(position);

        Glide.with(context).load(item.getImageUrl()).into(holder.imageView);
        holder.username.setText(item.getUsername());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {

        if (payloads != null && !payloads.isEmpty()) {
            Bundle o = (Bundle) payloads.get(0);
            for (String key : o.keySet()) {
                if(key.equals(KEY_IMAGEURL)) {
                    Glide.with(context).load(o.getString(KEY_IMAGEURL)).into(holder.imageView);
                } else if(key.equals(KEY_USERNAME)) {
                    holder.username.setText(o.getString(KEY_IMAGEURL));
                }
            }
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    public void swapAndDiffList(List<UserListItem> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(this.items, newList));

        this.items.clear();
        this.items.addAll(newList);

        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public SwipeResultAction onSwipeItem(ViewHolder holder, int position, @SwipeableItemResults int result) {
        if (result == SwipeableItemConstants.RESULT_CANCELED) {
            return new SwipeResultActionDefault();
        } else {
            return new MySwipeResultActionRemoveItem(this, position);
        }
    }

    @Override
    public int onGetSwipeReactionType(ViewHolder holder, int position, int x, int y) {
        return SwipeableItemConstants.REACTION_CAN_SWIPE_BOTH_H;
    }

    @Override
    public void onSetSwipeBackground(ViewHolder holder, int position, @SwipeableItemDrawableTypes int type) {
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private static class MySwipeResultActionRemoveItem extends SwipeResultActionRemoveItem {
        private SwipeableAdapter adapter;
        private int position;

        MySwipeResultActionRemoveItem(SwipeableAdapter adapter, int position) {
            this.adapter = adapter;
            this.position = position;
        }

        @Override
        protected void onPerformAction() {
            adapter.items.remove(position);
            adapter.notifyItemRemoved(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    class ViewHolder extends AbstractSwipeableItemViewHolder {
        ViewGroup containerView;
        ImageView imageView;
        TextView username;

        ViewHolder(View itemView) {
            super(itemView);
            containerView = (ViewGroup) itemView.findViewById(R.id.listitem_container);
            username = (TextView) itemView.findViewById(R.id.listitem_username);
            imageView = (ImageView) itemView.findViewById(R.id.listitem_image);
        }

        @Override
        public View getSwipeableContainerView() {
            return containerView;
        }
    }
}
