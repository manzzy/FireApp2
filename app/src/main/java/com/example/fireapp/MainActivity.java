package com.example.fireapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final String KEY_TITLE = "title";
    private static final String KEY_DESC = "description";

    private EditText title;
    private EditText desc;
    private EditText num;
    private Button btn;
    private TextView txt;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference cf = db.collection("Notebook");
    private DocumentReference nf = db.collection("Notebook").document("My Notebook");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = (EditText) findViewById(R.id.title);
        desc = (EditText) findViewById(R.id.desc);
        num = findViewById(R.id.num);
        txt = findViewById(R.id.txt);
        btn = findViewById(R.id.btn);

    }

    @Override
    protected void onStart() {
        super.onStart();

        cf.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    return;
                }

                String data="";

                for(QueryDocumentSnapshot qf: value){
                    Note note = qf.toObject(Note.class);

                    data += "title: " + note.getTitle() + "\nDescription: "+ note.getDescription() +" \nPriority: " +
                            note.getPriority()+ "\n\n";
                }

                txt.setText(data);
            }
        });

    }

//    public void fn(View view){
//        String t = title.getText().toString();
//        String d = desc.getText().toString();
//        title.setText("");
//        desc.setText("");
//
//        Note note = new Note(t,d);
//
//        nf.set(note)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(MainActivity.this, "Successful upload", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(MainActivity.this, "Failure ", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//    }


//    public void loadNote(View view) {
//        nf.get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        if(documentSnapshot.exists()){
//                            String t = documentSnapshot.getString(KEY_TITLE);
//                            String d = documentSnapshot.getString(KEY_DESC);
//
//                            txt.setText("title: "+ t + "\n Description: "+ d);
//                        } else {
//                            Toast.makeText(MainActivity.this, "Document Does't Exist", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(MainActivity.this, "Could't load document", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

//    public void updateNote(View view) {
//        String d = desc.getText().toString();
//
//        Map<String, Object> note = new HashMap<>();
//        note.put(KEY_DESC,d);
//        nf.set(note, SetOptions.merge());
//        //nf.update(KEY_DESC, d);
//    }
//
//    public void deleteDesc(View view) {
//        nf.update(KEY_DESC, FieldValue.delete());
//    }
//
//    public void deleteNote(View view) {
//        nf.delete();
//    }

    public void addNotes(View view) {
        String t = title.getText().toString();
        String d = desc.getText().toString();
        if( num.getText().length() == 0 ){
            num.setText("0");
        }
        int n = Integer.parseInt(num.getText().toString());


        Note note = new Note(t,d,n);

        cf.add(note);
    }

    public void loadNotes(View view){

        cf.whereGreaterThanOrEqualTo("priority", 2)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    String data="";
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot snap: queryDocumentSnapshots){
                            Note note= snap.toObject(Note.class);

                            data = data + "title: "+ note.getTitle() + "\nDescription: "+ note.getDescription() + "\nPriority: "+
                             note.getPriority()+ "\n\n";
                        }
                        txt.setText(data);
                    }
                });
    }
}