package com.behavior.calculator;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * @author Lix
 * @date 2020-04-15
 */

public class SimpleRvAdapter extends RecyclerView.Adapter<SimpleRvAdapter.VH> {
    private Context mContext;
    private LinkedList<String> list = new LinkedList<>();

    public SimpleRvAdapter(Context context) {
        mContext = context;
    }

    public void add(String item) {
        list.addFirst(item);
        notifyItemInserted(list.size());
    }

    public void clear() {
        if (list.size() > 0)
            list.removeAll(list);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_record_item, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        holder.textView.setText(list.get(position));
        holder.textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.textView.setSingleLine();
        holder.textView.setMovementMethod(ScrollingMovementMethod.getInstance());
//        holder.textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, "position:" + position, Toast.LENGTH_LONG).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        private TextView textView;

        VH(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.record);
        }
    }
}