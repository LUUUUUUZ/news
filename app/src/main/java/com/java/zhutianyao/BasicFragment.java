package com.java.zhutianyao;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;

public abstract class BasicFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //碎片的创建，用资源layout（通过函数返回int值），还有容器里的view
        View view=inflater.inflate(getLayoutResource(),container,false);
        ButterKnife.bind(this,view);
        return view;
    }
    protected abstract int getLayoutResource();

}
