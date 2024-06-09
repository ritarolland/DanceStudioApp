package com.example.dancestudioapp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dancestudioapp.activities.AddActivity;
import com.example.dancestudioapp.activities.AuthActivity;
import com.example.dancestudioapp.activities.InfoActivity;
import com.example.dancestudioapp.adapters.MasterclassesAdapter;
import com.example.dancestudioapp.databinding.FragmentHomeBinding;
import com.example.dancestudioapp.interfaces.MasterclassesInterface;
import com.example.dancestudioapp.models.Masterclass;
import com.example.dancestudioapp.models.User;
import com.example.dancestudioapp.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements MasterclassesInterface {

    private FragmentHomeBinding binding;
    private FirebaseDatabase DanceStudio;
    private FirebaseAuth mAuth;
    private DatabaseReference users;
    private ArrayList<Masterclass> masterclasses;
    private MasterclassesAdapter masterclassesAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        init();
        setInitialData();
        masterclassesAdapter = new MasterclassesAdapter(this, requireContext(), masterclasses);
        binding.masterclassesRecyclerView.setAdapter(masterclassesAdapter);
        binding.masterclassesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.addButton.setOnClickListener(v -> startActivity(new Intent(requireContext(), AddActivity.class)));
        binding.signOut.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(requireContext(), AuthActivity.class));
        });
        return binding.getRoot();
    }

    private void init() {
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
                for (DataSnapshot child : snapshot.getChildren()) {
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
            if (user != null) {
                binding.name.setText(user.userName);
                if (user.role.equals(Constants.MANAGER_ROLE))
                    binding.addButton.setVisibility(View.VISIBLE); //для менеджера добавили кнопку добавить
            }

        });
    }

    @Override
    public void onItemClick(int position) {
        Masterclass masterclass = masterclasses.get(position);
        Intent intent = new Intent(requireContext(), InfoActivity.class);
        intent.putExtra(Constants.MASTERCLASS, masterclass);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}