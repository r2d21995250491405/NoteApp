package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.notes.adapters.NotesRecyclerAdapter;
import com.example.notes.models.Note;
import com.example.notes.persistence.NoteRepository;
import com.example.notes.utils.ItemDecorator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class NotesListActivity extends AppCompatActivity implements NotesRecyclerAdapter.OnNoteListener,
        View.OnClickListener {

    //UI components
    private RecyclerView mRecyclerView;


    //vars
    private final ArrayList<Note> mNotes = new ArrayList<>();
    private NotesRecyclerAdapter mNotesRecyclerAdapter;
    private NoteRepository mNoteRepository;

    private static final String TAG = "NotesListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noteslist);
        mRecyclerView = findViewById(R.id.recycler_view);

        findViewById(R.id.fab).setOnClickListener(this);

        mNoteRepository = new NoteRepository(this);
        initRecyclerView();
        retrieveNotes();
//        insertFakeData();


        Log.d(TAG,"onCreate: thread: " + Thread.currentThread().getName());
        mNoteRepository.insertNoteTask(new Note());



        setSupportActionBar(findViewById(R.id.toolbar));
        setTitle("Notes");

    }

    private void retrieveNotes() {
        mNoteRepository.retrieveNoteTask().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                if (mNotes.size() > 0) {
                    mNotes.clear();
                }
                if (notes != null) {
                    mNotes.addAll(notes);
                }
                mNotesRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void insertFakeData() {
        for (int i = 0; i < 100; i++) {
            Note note = new Note();
            note.setTitle("title # " + i);
            note.setContent("content # " + i);
            note.setTimestamp("Fab 2021");
            mNotes.add(note);
        }
        mNotesRecyclerAdapter.notifyDataSetChanged();

    }

    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mNotesRecyclerAdapter = new NotesRecyclerAdapter(mNotes, this);
        ItemDecorator itemDecorator = new ItemDecorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mNotesRecyclerAdapter);
    }

    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("selected_note", mNotes.get(position));
        startActivity(intent);
        Log.d(TAG, "onNoteClick: clicked " + position);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, NoteActivity.class);
        startActivity(intent);
    }

    private void deleteNote(Note note) {
        mNotes.remove(note);
        mNotesRecyclerAdapter.notifyDataSetChanged();

        mNoteRepository.deleteNote(note);
    }

    private final ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            deleteNote(mNotes.get(viewHolder.getAdapterPosition()));
        }
    };
}