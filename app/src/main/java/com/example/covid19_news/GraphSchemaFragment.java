package com.example.covid19_news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GraphSchemaFragment extends BasicFragment {
    @Override
    protected int getLayoutResource() {
        return R.layout.open_graph_layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startActivity(new Intent(getActivity(),EntityActivity.class));

    }
}
