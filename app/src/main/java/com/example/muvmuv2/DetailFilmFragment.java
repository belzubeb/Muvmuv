package com.example.muvmuv2;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFilmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFilmFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View backButton;

    public DetailFilmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFilm_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFilmFragment newInstance(String param1, String param2) {
        DetailFilmFragment fragment = new DetailFilmFragment();
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
        View view = inflater.inflate(R.layout.fragment_detail_film, container, false);

        Button reviewsButton = view.findViewById(R.id.reviews);
        reviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviews(v);
            }
        });
        backButton = view.findViewById(R.id.back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mendapatkan fragment manager dari activity yang memuat fragment ini
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

                // Membuat instance fragment home
                Fragment homeFragment = new HomeFragment();

                // Mulai transaksi fragment
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // Gantikan fragment yang ada di container dengan fragment home
                transaction.replace(R.id.fragment_container, homeFragment);

                // Menambahkan transaksi ke back stack
                transaction.addToBackStack(null);

                // Melakukan transaksi
                transaction.commit();
            }
        });
        Button buttonAdd = view.findViewById(R.id.buttonadd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        return view;
    }

    private void showDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog);

        // Show the dialog
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    public void reviews(View view) {
        Intent intent = new Intent(requireContext(), Reviews.class);
        startActivity(intent);
    }
}