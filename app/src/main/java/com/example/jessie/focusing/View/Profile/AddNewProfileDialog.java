package com.example.jessie.focusing.View.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jessie.focusing.Model.Profile;
import com.example.jessie.focusing.R;

import static com.example.jessie.focusing.Utils.TimeHelper.HOUR_IN_MILLIS;

/**
 * @author : Yujie Lyu
 * @date : 26-01-2019
 * @time : 17:32
 */
public class AddNewProfileDialog extends DialogFragment {
    private Profile item;
    private OnFragmentInteractionListener listener;

    public static AddNewProfileDialog newInstance(Profile profile) {
        AddNewProfileDialog dialog = new AddNewProfileDialog();
        dialog.item = profile;
        return dialog;
    }

    private void bindingItem(View view, Profile profile) {
        if (profile == null) {
            return;
        }
        EditText evName = view.findViewById(R.id.ev_name);
        if (TextUtils.isEmpty(profile.getProfileName())) {
            Toast.makeText(getContext(), getString(R.string.profile_name_non_empty), Toast.LENGTH_LONG).show();
            return;
        }
        evName.setText(profile.getProfileName());
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_profile, container, false);
        Button btnOk = view.findViewById(R.id.btn_ok);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        bindingItem(view, item);
        btnOk.setOnClickListener(v -> {
            View rootView = v.getRootView();
            EditText evName = rootView.findViewById(R.id.ev_name);
            String name = evName.getText().toString();
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(getContext(), "Profile name cannot be empty", Toast.LENGTH_LONG).show();
                return;
            }
            Profile profile = new Profile();
            profile.setProfileName(name);
            long now = System.currentTimeMillis();
            profile.setTime(now, now + HOUR_IN_MILLIS);
            onOKPressed(profile);
            dismiss();
        });
        btnCancel.setOnClickListener(v -> getDialog().cancel());
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
