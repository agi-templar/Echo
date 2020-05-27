package com.example.echo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ResponseListFragment extends Fragment {
    private RecyclerView mResponseRecyclerView;
    private ResponseAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_response_list, container, false);

        mResponseRecyclerView = view.findViewById(R.id.response_recycler_view);
        mResponseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    // for option menu function
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.no_actions, menu);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);  // aware of option menu callback
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        ResponseFactory responseFac = ResponseFactory.get(getActivity());
        List<Response> responses = responseFac.getResponses();

        if (mAdapter == null) {
            mAdapter = new ResponseAdapter(responses);
            mResponseRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private class ResponseHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Response mResponse;

        private ImageView mImageView;
        private TextView mTitleTextView;
        private TextView mURL;
        private TextView mDateTextView;

        public ResponseHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_response, parent, false));
            itemView.setOnClickListener(this);

            mImageView = itemView.findViewById(R.id.response_image);
            mTitleTextView = itemView.findViewById(R.id.response_title);
            mURL = itemView.findViewById(R.id.response_url);
            mDateTextView = itemView.findViewById(R.id.response_date);
        }

        public void bind(Response response) {
            mResponse = response;

            mImageView.setImageResource(mResponse.getImage());
            mTitleTextView.setText(mResponse.getTitle());
            mURL.setText(mResponse.getURL());
            mDateTextView.setText(mResponse.getDate().toString());
        }
        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(),
                    mResponse.getTitle() + " clicked!", Toast.LENGTH_SHORT)
                    .show();

            //Intent intent = CreateNoteActivity.newIntent(getActivity(), mResponse.getId());
            Uri hyperlink = Uri.parse(mResponse.getURL());
            Intent intent = new Intent(Intent.ACTION_VIEW, hyperlink);
            startActivity(intent);
        }
    }

    private class ResponseAdapter extends RecyclerView.Adapter<ResponseHolder> {

        private List<Response> mResponses;

        public ResponseAdapter(List<Response> responses) {
            mResponses = responses;
        }

        @Override
        public ResponseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ResponseHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ResponseHolder holder, int position) {
            Response response = mResponses.get(position);
            holder.bind(response);
        }

        @Override
        public int getItemCount() {
            return mResponses.size();
        }
    }
}
