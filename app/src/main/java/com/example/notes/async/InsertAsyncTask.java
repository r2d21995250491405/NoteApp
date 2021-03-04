package com.example.notes.async;

import android.os.AsyncTask;
import android.util.Log;

import com.example.notes.models.Note;
import com.example.notes.persistence.NoteDAO;

public class InsertAsyncTask extends AsyncTask<Note, Void, Void> {
    private static final String TAG = "InsertAsyncTask";
    private NoteDAO mNoteDao;

    public InsertAsyncTask(NoteDAO dao) {
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        Log.d(TAG, "doInBackground: thread: " + Thread.currentThread().getName());
        mNoteDao.insertNotes(notes);
        return null;
    }
}
