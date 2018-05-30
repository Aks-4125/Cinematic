package com.training.cinematic.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.training.cinematic.Adapter.PopularTvAdapter;
import com.training.cinematic.R;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class PopularTvFragment extends Fragment {

    private static final String TAG = "upcoming movie fragment";
    PopularTvAdapter movieadapter;
    @BindView(R.id.recyclerview2)
    RecyclerView mRecyclerView;
    int movieimage[] = {R.drawable.cardb, R.drawable.blur, R.drawable.pin, R.drawable.newback, R.drawable.blackba};
    String moviename[];
    Unbinder unbinder;

    public PopularTvFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_populartv, container, false);
        moviename = getResources().getStringArray(R.array.mname);
        unbinder = ButterKnife.bind(this, view);


        return view;
    }

    public void onActivityCreated(@Nullable Bundle saveInstance) {

        super.onActivityCreated(saveInstance);
        final LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        movieadapter = new PopularTvAdapter(getActivity(), movieimage, moviename);

        mRecyclerView.setAdapter(movieadapter);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
