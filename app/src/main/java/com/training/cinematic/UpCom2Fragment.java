package com.training.cinematic;


import android.graphics.Movie;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpCom2Fragment extends Fragment {
    GridLayoutManager gridLayoutManager;
    TextView date;
    TextView name;
    ImageView image;
    RecyclerView mRecyclerView;
    ListAdapter mListadapter;


    public UpCom2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_up_com2, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        date = (TextView)view.findViewById(R.id.movie_date);
        image = (ImageView)view.findViewById(R.id.movie_img);


        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        ArrayList data = new ArrayList<Movie>();

        mListadapter = new ListAdapter(data);
        mRecyclerView.setAdapter(mListadapter);

        return view;
    }

    public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>
    {
        private ArrayList<Movie> dataList;

        public ListAdapter(ArrayList<Movie> data)
        {
            this.dataList = data;
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView textViewname;
            ImageView image;
            TextView textViewDate;

            public ViewHolder(View itemView)
            {
                super(itemView);
                this.textViewname = (TextView) itemView.findViewById(R.id.movie_name);
                this.image = (ImageView) itemView.findViewById(R.id.movie_img);
                this.textViewDate = (TextView) itemView.findViewById(R.id.movie_date);
            }
        }

        @Override
        public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);

            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ListAdapter.ViewHolder holder, final int position)
        {
          /*  holder.textViewname.setText(dataList.get(position).getText());
            holder.image.setimage(dataList.get(position).getComment());
            holder.textViewDate.setText(dataList.get(position).getDate());

            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Toast.makeText(getActivity(), "Item " + position + " is clicked.", Toast.LENGTH_SHORT).show();
                }
            });*/
        }

        @Override
        public int getItemCount()
        {
            return dataList.size();
        }
    }



}
