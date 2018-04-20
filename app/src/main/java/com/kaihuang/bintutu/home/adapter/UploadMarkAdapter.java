package com.kaihuang.bintutu.home.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaihuang.bintutu.R;

import java.util.List;

/**
 * Created by zhoux on 2017/9/19.
 */

public class UploadMarkAdapter extends RecyclerView.Adapter<UploadMarkAdapter.ViewHoloer> {
    private LayoutInflater mInflater;
    private Context context;
    public List<Bitmap> bitmaps;
    public List<String> remarks;
    public UploadMarkAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    class ViewHoloer extends RecyclerView.ViewHolder {

        ImageView img_mark;
        TextView text_mark;

        public ViewHoloer(View view) {
            super(view);
            img_mark = (ImageView)view.findViewById(R.id.img_mark);
            text_mark=(TextView)view.findViewById(R.id.text_mark);
        }
    }


    @Override
    public ViewHoloer onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_upload_mark,
                parent, false);
        ViewHoloer viewHolder = new ViewHoloer(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHoloer holder, int position) {
        holder.img_mark.setImageBitmap(bitmaps.get(position));
        if(null == remarks.get(position) || "".equals(remarks.get(position))){
            holder.text_mark.setVisibility(View.GONE);
        }else {
            holder.text_mark.setVisibility(View.VISIBLE);
            holder.text_mark.setText(remarks.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return null ==remarks?0:remarks.size();
    }


}
