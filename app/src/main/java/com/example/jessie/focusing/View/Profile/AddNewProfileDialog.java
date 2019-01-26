package com.example.jessie.focusing.View.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.jessie.focusing.Model.Profile;
import com.example.jessie.focusing.R;

/**
 * @author : Yujie Lyu
 * @date : 26-01-2019
 * @time : 17:32
 */
public class AddNewProfileDialog extends DialogFragment {
    private Profile item;
    private OnFragmentInteractionListener listener;

    public AddNewProfileDialog() {
        // Required empty public constructor
    }

    public static AddNewProfileDialog newInstance(Profile profile) {

        AddNewProfileDialog dialog = new AddNewProfileDialog();
        dialog.item = profile;
        return dialog;
    }

    private void bingdingItem(View view, Profile profile) {
        if (profile == null) {
            return;
        }
        EditText evName = view.findViewById(R.id.ev_name);
        evName.setText(profile.getProfileName().toLowerCase());
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_profile_dialog, container, false);
        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View rootView = v.getRootView();

                EditText evName = rootView.findViewById(R.id.ev_name);
                String name = evName.getText().toString();
                Profile profile = new Profile();
//                profile.setId(profileId);
                profile.setProfileName(name);
                onOKPressed(profile);
//                Intent intent = new Intent(getActivity(), ProfileDetailActivity.class);
//                startActivity(intent);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });
        bingdingItem(view, item);
        return view;
    }

    private void onOKPressed(Profile profile) {
        if (listener != null) {
            listener.onFragmentInteraction(profile);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Profile profile);

    }

}
