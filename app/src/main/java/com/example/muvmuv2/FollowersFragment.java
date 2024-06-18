package com.example.muvmuv2;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FollowersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FollowersFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FollowersFragment() {
        // Required empty public constructor
    }

    public static FollowersFragment newInstance(String param1, String param2) {
        FollowersFragment fragment = new FollowersFragment();
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
        View view = inflater.inflate(R.layout.fragment_followers, container, false);

        // Dapatkan referensi ke tombol
        final Button button1 = view.findViewById(R.id.button1);
        final Button button2 = view.findViewById(R.id.button2);

        // Atur listener untuk klik tombol button1
        button1.setOnClickListener(new View.OnClickListener() {
            private boolean isFollowing = true;

            @Override
            public void onClick(View v) {
                if (isFollowing) {
                    button1.setText("Follow Back");
                    button1.setBackgroundResource(R.drawable.button_followback);
                    button1.setTextColor(getResources().getColor(R.color.Primary));
                } else {
                    button1.setText("Following");
                    button1.setBackgroundResource(R.drawable.button_following);
                    button1.setTextColor(getResources().getColor(R.color.Tangerine));
                }
                isFollowing = !isFollowing;
            }
        });

        // Atur listener untuk klik tombol button2
        button2.setOnClickListener(new View.OnClickListener() {
            private boolean isFollowing = true;

            @Override
            public void onClick(View v) {
                if (isFollowing) {
                    button2.setText("Follow Back");
                    button2.setBackgroundResource(R.drawable.button_followback);
                    button2.setTextColor(getResources().getColor(R.color.Primary));
                } else {
                    button2.setText("Following");
                    button2.setBackgroundResource(R.drawable.button_following);
                    button2.setTextColor(getResources().getColor(R.color.Tangerine));
                }
                isFollowing = !isFollowing;
            }
        });
        return view;
    }
}
