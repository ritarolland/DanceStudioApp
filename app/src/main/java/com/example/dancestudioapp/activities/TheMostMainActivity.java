package com.example.dancestudioapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;

import com.example.dancestudioapp.R;
import com.example.dancestudioapp.adapters.MasterclassesAdapter;
import com.example.dancestudioapp.databinding.ActivityTheMostMainBinding;
import com.example.dancestudioapp.interfaces.MasterclassesInterface;
import com.example.dancestudioapp.models.Masterclass;
import com.example.dancestudioapp.models.User;
import com.example.dancestudioapp.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TheMostMainActivity extends AppCompatActivity implements MasterclassesInterface {

    private FirebaseDatabase DanceStudio;
    FirebaseAuth mAuth;
    private DatabaseReference users;
    private ActivityTheMostMainBinding activityTheMostMainBinding;
    ArrayList<Masterclass> masterclasses;
    MasterclassesAdapter masterclassesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTheMostMainBinding = ActivityTheMostMainBinding.inflate(getLayoutInflater());
        setContentView(activityTheMostMainBinding.getRoot());
        init();
        setInitialData();
        masterclassesAdapter = new MasterclassesAdapter(this, this, masterclasses);
        activityTheMostMainBinding.masterclassesRecyclerView.setAdapter(masterclassesAdapter);
        activityTheMostMainBinding.masterclassesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        activityTheMostMainBinding.addButton.setOnClickListener(v -> startActivity(new Intent(this, AddActivity.class)));
        activityTheMostMainBinding.signOut.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(this, AuthActivity.class));
        });
    }

    private  void init() {
        mAuth = FirebaseAuth.getInstance();
        DanceStudio = FirebaseDatabase.getInstance();
        users = DanceStudio.getReference(Constants.KEY_COLLECTION_USERS);
        masterclasses = new ArrayList<>();
        DatabaseReference masterclassesReference = DanceStudio.getReference(Constants.KEY_COLLECTION_MASTERCLASSES);
        masterclassesReference.addValueEventListener(new ValueEventListener() { //получаем masterclasses из базы данных
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                masterclasses.clear();
                for(DataSnapshot child : snapshot.getChildren()) {
                    Masterclass masterclass = child.getValue(Masterclass.class);
                    masterclasses.add(masterclass);
                }
                masterclassesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setInitialData() {
        String currentUserId = mAuth.getCurrentUser().getUid(); //id текущего пользователя
        users.get().addOnCompleteListener(task -> { //DataSnapshot пользователей
            User user = task.getResult().child(currentUserId).getValue(User.class); //получили пользователя по id
            if(user != null){
                activityTheMostMainBinding.name.setText(user.userName);
                if(user.role.equals(Constants.MANAGER_ROLE))
                    activityTheMostMainBinding.addButton.setVisibility(View.VISIBLE); //для менеджера добавили кнопку добавить
            }

        });


    }

    @Override
    public void onItemClick(int position) {
        Masterclass masterclass = masterclasses.get(position);
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra(Constants.MASTERCLASS, masterclass);
        startActivity(intent);
    }
}