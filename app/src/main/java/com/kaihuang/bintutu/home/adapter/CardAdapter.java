package com.kaihuang.bintutu.home.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.kaihuang.bintutu.R;
import com.kaihuang.bintutu.home.bean.ShowTagPictureBean;
import com.kaihuang.bintutu.utils.BitmapUtils;
import com.kaihuang.bintutu.utilviews.CardAdapterHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaopf on 3/21/18.
 */
public  class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    public List<ShowTagPictureBean> showTagPictureBeanList = new ArrayList<>();
    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();
    private OnItemClickLitener mOnItemClickLitener;
    private Context mcontext;
    public String userId = "";
    public CardAdapter(Context context,List<ShowTagPictureBean> mList) {
        this.showTagPictureBeanList = mList;
        this.mcontext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card_item, parent, false);
        mCardAdapterHelper.onCreateViewHolder(parent, itemView);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        ShowTagPictureBean showTagPictureBean;
        if(showTagPictureBeanList != null && showTagPictureBeanList.size() > 0){
            showTagPictureBean = showTagPictureBeanList.get(position);
            String isFrom;
            String leftOrRight = "1";
            if(showTagPictureBean != null){
                isFrom = showTagPictureBean.getIsFrom();
                leftOrRight = showTagPictureBean.getLeftOrRight();
                switch (position){
                    case 0:
                        if("1".equals(leftOrRight)){
                            holder.text_foot_name.setText("左脚脚面");
                        }else if("2".equals(leftOrRight)){
                            holder.text_foot_name.setText("右脚脚面");
                        }
                        break;
                    case 1:
                        if("1".equals(leftOrRight)){
                            holder.text_foot_name.setText("左脚脚底");
                        }else if("2".equals(leftOrRight)){
                            holder.text_foot_name.setText("右脚脚底");
                        }
                        break;
                    case 2:
                        if("1".equals(leftOrRight)){
                            holder.text_foot_name.setText("左脚内侧");
                        }else if("2".equals(leftOrRight)){
                            holder.text_foot_name.setText("右脚内侧");
                        }
                        break;
                    case 3:
                        if("1".equals(leftOrRight)){
                            holder.text_foot_name.setText("左脚外侧");
                        }else if("2".equals(leftOrRight)){
                            holder.text_foot_name.setText("右脚外侧");
                        }
                        break;
                }

                //1.为本地存的 2.服务端取的  空为足型样例
                switch (isFrom){
                    case "":
                        Bitmap bitmap = BitmapUtils.drawable2Bitmap(mcontext.getResources().getDrawable(Integer.parseInt(showTagPictureBean.getFileName())));
//                        holder.mImageView.setImageResource(Integer.parseInt(showTagPictureBean.getFileName()));
                        switch (position){
                            case 2:
                                if("1".equals(leftOrRight)){
                                    //左脚内侧旋转-90度
                                    bitmap = BitmapUtils.rotateBitmap(bitmap,90);
                                }else if("2".equals(leftOrRight)){
                                    //右脚内侧旋转90度
                                    bitmap = BitmapUtils.rotateBitmap(bitmap,-90);
                                }
                                break;
                            case 3:
                                if("1".equals(leftOrRight)){
                                    //左脚外侧旋转90度
                                    bitmap = BitmapUtils.rotateBitmap(bitmap,-90);
                                }else if("2".equals(leftOrRight)){
                                    //右脚外侧旋转-90度
                                    bitmap = BitmapUtils.rotateBitmap(bitmap,90);
                                }
                                break;
                        }
                        holder.mImageView.setImageBitmap(bitmap);
                        break;
                    case "1":
                        String filename = Environment.getExternalStorageDirectory()  +"/bintutuImgage/pictureTag/" + showTagPictureBean.getFileName();
                        File file = new File(filename);
                        InputStream inputStream = null;
                        try {
                            inputStream = new FileInputStream(file);
                            Bitmap pb = BitmapFactory.decodeStream(inputStream);
                            holder.mImageView.setImageBitmap(pb);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickLitener != null){
                    mOnItemClickLitener.onItemClick(holder.itemView,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return showTagPictureBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageView;
        public final TextView text_foot_name;
        public final TextView text_desc;
        public ViewHolder(final View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);
            text_foot_name = (TextView)itemView.findViewById(R.id.text_foot_name);
            text_desc = (TextView) itemView.findViewById(R.id.text_desc);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

}
