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

    // callback function for the option menu
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.add_note:
//                Note note = new Note();
//                NoteFactory.get(getActivity()).addNote(note);
//                Intent intent = CreateNoteActivity.newIntent(getActivity(), note.getId());
//                startActivity(intent);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

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

        private TextView mTitleTextView;
        private TextView mDateTextView;

        public ResponseHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_note, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = itemView.findViewById(R.id.note_title);
            mDateTextView = itemView.findViewById(R.id.note_date);
        }

        public void bind(Response response) {
            mResponse = response;
            mTitleTextView.setText(mResponse.getTitle());
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
