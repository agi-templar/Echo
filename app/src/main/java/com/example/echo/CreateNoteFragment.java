package com.example.echo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.UUID;

/**
 * Author:Ruibo Liu(ruibo.liu.gr@dartmoputh.edu)
 *
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class CreateNoteFragment extends Fragment {

    private static final String ARG_NOTE_ID = "note_id";

    private Note mNote;
    private EditText mTitleField;
    private EditText mNoteTextField;
    private Button mDateButton;
    private Button mImageUpload;

    /**
     * helper function to create instance of CreateNoteFragment given note_id
     *
     * @param noteID
     * @return
     */
    public static CreateNoteFragment newInstance(UUID noteID) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE_ID, noteID);

        CreateNoteFragment fragment = new CreateNoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // extract the corresponding note
        UUID noteID = (UUID) getArguments().getSerializable(ARG_NOTE_ID);
        mNote = NoteFactory.get(getActivity()).getNote(noteID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        mTitleField = view.findViewById(R.id.note_title);
        mTitleField.setText(mNote.getTitle()); // set text when given the note
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = view.findViewById(R.id.note_date);
        mDateButton.setText(mNote.getDate().toString());
        mDateButton.setEnabled(false);

        return view;
    }
}
