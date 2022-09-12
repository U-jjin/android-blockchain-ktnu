package com.universeluvv.ktnuvoting.VoteItemFragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.universeluvv.ktnuvoting.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EndVoteFragment extends Fragment {

    private    String userid, major, votename;


    public EndVoteFragment(String userid, String major, String votename) {

        this.userid =userid;
        this.major =major;
        this.votename =votename;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_end_vote, container, false);
    }

}
