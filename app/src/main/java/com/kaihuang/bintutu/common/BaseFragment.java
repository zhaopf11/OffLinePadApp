package com.kaihuang.bintutu.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.kaihuang.bintutu.common.http.OkHttpHelper;
import com.kaihuang.bintutu.common.model.BaseRespMsg;

import javax.inject.Inject;

import butterknife.ButterKnife;


public abstract class BaseFragment extends Fragment {

    @Inject
    protected OkHttpHelper mHttpHelper;

    private LinearLayout mRoot;

    LayoutInflater mInflater;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        mRoot = new LinearLayout(getActivity());
        mRoot.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        View view = createView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);

        BaseApplication.component().inject(this);

        initToolBar();

        mRoot.addView(view);

        init();
        return mRoot;

    }

    public void showEmptyView(int viewid) {
        View emptyview = mInflater.inflate(viewid, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRoot.getChildAt(0).setVisibility(View.GONE);
        mRoot.addView(emptyview, lp);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void initToolBar() {

    }


    public abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void init();


    public void startActivity(Intent intent, boolean isNeedLogin) {


        if (isNeedLogin) {

//            User user = BaseApplication.getInstance().getUser();
//            if (user != null) {
//                super.startActivity(intent);
//            } else {
//
//                BaseApplication.getInstance().putIntent(intent);
//                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
//                super.startActivity(loginIntent);
//
//            }

        } else {
            super.startActivity(intent);
        }

    }
    public void startActivityForResult(Intent intent, boolean isNeedLogin, int requestCode) {


        if (isNeedLogin) {

//            User user = BaseApplication.getInstance().getUser();
//            if (user != null) {
//                super.startActivityForResult(intent,requestCode);
//            } else {
//
//                BaseApplication.getInstance().putIntent(intent);
//                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
//                super.startActivityForResult(loginIntent,requestCode);
//
//            }
////
        } else {
            super.startActivityForResult(intent,requestCode);
        }

    }

    public void showNormalErr(BaseRespMsg respMsg) {
//        if (respMsg == null || TextUtils.isEmpty(respMsg.getMessage())) {
//            showFail();
//        } else {
//            ToastUtils.show(respMsg.getMessage());
//        }

    }

    public void showFail() {
//        if (Looper.myLooper() != Looper.getMainLooper()) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    ToastUtils.show(getString(R.string.err_net_connect));
//                }
//            });
//        } else
//            ToastUtils.show(getString(R.string.err_net_connect));
    }

}
