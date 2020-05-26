package com.example.echo;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * factory class for Model: Note
 * helping to compose a list Note object, and providing access methods
 */
public class ResponseFactory {
    private static ResponseFactory sResponseFactory;
    private List<Response> mResponses;

    public static ResponseFactory get(Context context) {
        if (sResponseFactory == null) {
            sResponseFactory = new ResponseFactory(context);
        }
        return sResponseFactory;
    }

    private ResponseFactory(Context context) {
        mResponses = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            Note note = new Note();
//            note.setTitle("Note #" + i);
//            note.setNoteText("Notes text here!");
//            mNotes.add(note);
//        }
    }

    public List<Response> getResponses() {
        return mResponses;
    }

    public Response getNote(UUID id) {
        for (Response response : mResponses) {
            if (response.getId().equals(id)) {
                return response;
            }
        }
        return null;
    }

    public void addResponse(Response response) {
        mResponses.add(response);
    }
}

