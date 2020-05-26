package com.example.echo;

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

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * factory class for Model: Note
 * helping to compose a list Note object, and providing access methods
 */
public class NoteFactory {
    private static NoteFactory sNoteFactory;
    private List<Note> mNotes;

    public static NoteFactory get(Context context) {
        if (sNoteFactory == null) {
            sNoteFactory = new NoteFactory(context);
        }

        return sNoteFactory;
    }

    private NoteFactory(Context context) {
        mNotes = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            Note note = new Note();
//            note.setTitle("Note #" + i);
//            note.setNoteText("Notes text here!");
//            mNotes.add(note);
//        }
    }

    public List<Note> getNotes() {
        return mNotes;
    }

    public Note getNote(UUID id) {
        for (Note note : mNotes) {
            if (note.getId().equals(id)) {
                return note;
            }
        }

        return null;
    }

    public void addNote(Note note) {
        mNotes.add(note);
    }
}

