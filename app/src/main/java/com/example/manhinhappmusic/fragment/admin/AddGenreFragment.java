package com.example.manhinhappmusic.fragment.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.model.Category;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddGenreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddGenreFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddGenreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters

    private EditText editName, editDescription;
    private ImageView imageView;
    private Button btnSave;
    private String selectedImagePath = "";

    public static AddGenreFragment newInstance(String param1, String param2) {
        AddGenreFragment fragment = new AddGenreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_genre, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editName = view.findViewById(R.id.editName);
        editDescription = view.findViewById(R.id.editDescription);
        imageView = view.findViewById(R.id.imageView);
        btnSave = view.findViewById(R.id.btnSave);

        imageView.setOnClickListener(v -> {
//            Toast.makeText(getContext(),   , Toast.LENGTH_SHORT).show();

        });

        btnSave.setOnClickListener( v-> {
            String name = editName.getText().toString().trim();
            String description = editDescription.getText().toString().trim();

            if (name.isEmpty())
            {

            }

            Category category = new Category(name, description, selectedImagePath);

            // Chưa thêm các lệnh


        });
    }
}