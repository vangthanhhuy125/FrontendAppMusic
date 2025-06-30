package com.example.manhinhappmusic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ConfirmDeletingGenreFragment extends DialogFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private static final String ARG_GENRE = "genre";

    public ConfirmDeletingGenreFragment() {
        // Required empty public constructor
    }

    public static ConfirmDeletingGenreFragment newInstance(Genre genre) {
        ConfirmDeletingGenreFragment fragment = new ConfirmDeletingGenreFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_GENRE, genre); // ✅ dùng Parcelable thay vì String
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog_NoActionBar);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_confirm_deleting_genre, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button cancelButton = view.findViewById(R.id.cancel_button);
        Button deleteButton = view.findViewById(R.id.create_button);

        cancelButton.setOnClickListener(v -> dismiss());

        deleteButton.setOnClickListener(v -> {
            // TODO: Thực hiện thao tác xóa thể loại tại đây, ví dụ gọi callback hoặc ViewModel
            // Ví dụ: nếu bạn muốn thông báo lại cho Activity
            if (getActivity() instanceof OnGenreDeleteListener) {
                ((OnGenreDeleteListener) getActivity()).onDeleteConfirmed();
            }
            dismiss();
        });
    }

    // Giao tiếp với Activity chứa Fragment này
    public interface OnGenreDeleteListener {
        void onDeleteConfirmed();
    }
}
