package com.example.dancestudioapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.dancestudioapp.R;
import com.example.dancestudioapp.adapters.MasterclassesAdapter;
import com.example.dancestudioapp.adapters.MembersAdapter;
import com.example.dancestudioapp.databinding.ActivityInfoBinding;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InfoActivity extends AppCompatActivity {

    ActivityInfoBinding activityInfoBinding;
    ArrayList<String> membersIds;
    FirebaseAuth mAuth;
    FirebaseDatabase DanceStudio;
    DatabaseReference masterclasses;

    Masterclass masterclass;
    MembersAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityInfoBinding = ActivityInfoBinding.inflate(getLayoutInflater());
        setContentView(activityInfoBinding.getRoot());
        init();
        setInitialData();
        adapter = new MembersAdapter(this, membersIds);
        activityInfoBinding.membersRecyclerView.setAdapter(adapter);
        activityInfoBinding.membersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        activityInfoBinding.buttonBack.setOnClickListener(v -> finish());
    }

    private  void init() {
        masterclass = getIntent().getParcelableExtra(Constants.MASTERCLASS);
        membersIds = new ArrayList<>();
        DanceStudio = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        masterclasses = DanceStudio.getReference(Constants.KEY_COLLECTION_MASTERCLASSES);
    }
    private void setInitialData() {
        activityInfoBinding.title.setText(masterclass.getName());
        activityInfoBinding.choreographerName.setText(masterclass.getChoreograph());
        activityInfoBinding.hallName.setText(masterclass.getHallName());
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat sdfDate = new SimpleDateFormat("E dd.MM", Locale.getDefault());
        Date date = masterclass.getDateObject();
        activityInfoBinding.time.setText(sdfTime.format(date));
        activityInfoBinding.date.setText(sdfDate.format(masterclass.getDateObject()));
        membersIds = new ArrayList<>(masterclass.getMemberIds());

        DatabaseReference membersReference = masterclasses.child(masterclass.getId()).child(Constants.KEY_MEMBER_IDS);
        membersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int size = (int) snapshot.getChildrenCount();
                masterclasses.child(masterclass.getId()).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Masterclass masterclass = task.getResult().getValue(Masterclass.class);
                        if (masterclass != null) {
                            String places = (masterclass.getMaxMembersNumber() - size) + "/" + masterclass.getMaxMembersNumber();
                            activityInfoBinding.places.setText(places);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String currentUserId = mAuth.getCurrentUser().getUid(); //id текущего пользователя
        DatabaseReference users = DanceStudio.getReference(Constants.KEY_COLLECTION_USERS);
        users.get().addOnCompleteListener(task -> { //DataSnapshot пользователей
            User user = task.getResult().child(currentUserId).getValue(User.class); //получили пользователя по id
            if(user != null){
                if(user.role.equals(Constants.MANAGER_ROLE)) {
                    activityInfoBinding.signUpButton.setText(R.string.delete_button);
                    activityInfoBinding.signUpButton.setOnClickListener(this::deleteMasterclass);
                }
                else {
                    for(int i = 0; i < membersIds.size(); i++) {
                        if(membersIds.get(i).equals(currentUserId)) {
                            activityInfoBinding.signUpButton.setText(R.string.not_take_part);
                        }
                    }
                    activityInfoBinding.signUpButton.setOnClickListener(this::onClickSignUp);
                }
            }

        });
    }

    private void onClickSignUp(View view) {
        String buttonText = activityInfoBinding.signUpButton.getText().toString();
        String currentUserId = mAuth.getCurrentUser().getUid();

        if (buttonText.equals(getString(R.string.sign_up))) {
            if (masterclass.getMaxMembersNumber() <= membersIds.size()) {
                Toast.makeText(this, "All places are taken", Toast.LENGTH_SHORT).show();
            } else {
                addMemberToMasterclass(masterclass.getId(), currentUserId);
            }
        } else if (buttonText.equals(getString(R.string.not_take_part))) {
            removeMemberFromMasterclass(masterclass.getId(), currentUserId);
        }
    }

    private void addMemberToMasterclass(String masterclassKey, String memberId) {
        DatabaseReference masterclassRef = masterclasses.child(masterclassKey).child(Constants.KEY_MEMBER_IDS);
        masterclassRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> memberIds = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot memberSnapshot : snapshot.getChildren()) {
                        memberIds.add(memberSnapshot.getValue(String.class));
                    }
                }

                if (!memberIds.contains(memberId)) {
                    memberIds.add(memberId);
                    masterclassRef.setValue(memberIds).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Member added successfully", Toast.LENGTH_SHORT).show();
                            activityInfoBinding.signUpButton.setText(R.string.not_take_part);
                            adapter.updateData(memberIds);
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to add member", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to read members", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeMemberFromMasterclass(String masterclassKey, String memberId) {
        DatabaseReference masterclassRef = masterclasses.child(masterclassKey).child(Constants.KEY_MEMBER_IDS);
        masterclassRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> memberIds = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot memberSnapshot : snapshot.getChildren()) {
                        memberIds.add(memberSnapshot.getValue(String.class));
                    }
                }

                if (memberIds.contains(memberId)) {
                    memberIds.remove(memberId);
                    masterclassRef.setValue(memberIds).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Member removed successfully", Toast.LENGTH_SHORT).show();
                            activityInfoBinding.signUpButton.setText(R.string.sign_up);
                            adapter.updateData(memberIds);
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to remove member", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to read members", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteMasterclass(View view) {
        String masterclassId = masterclass.getId();
        if (masterclassId != null) {
            masterclasses.child(masterclassId).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Masterclass deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Failed to delete masterclass", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}