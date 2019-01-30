package com.example.jessie.focusing.View.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.jessie.focusing.Model.Profile;
import com.example.jessie.focusing.Model.ProfileManager;
import com.example.jessie.focusing.R;

/**
 * @author : Yujie Lyu
 * @date : 30-01-2019
 * @time : 00:59
 */
public class DeleteProfileDialog extends DialogFragment{
        private Profile item;
        private OnFragmentInteractionListener listener;
        private ProfileManager profileManager;

        public DeleteProfileDialog() {
            // Required empty public constructor
        }

//        public static DeleteProfileDialog newInstance(Profile profile) {
//
//            DeleteProfileDialog dialog = new DeleteProfileDialog();
//            dialog.item = profile;
//            return dialog;
//        }


        @Override
        public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dialog_delete_confirm, container, false);
            Button btnOk =  view.findViewById(R.id.btn_delete_confirm);
            Button btnCancel = view.findViewById(R.id.btn_cancel_confirm);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    profileManager.deleteProfile(profileId);

                    onOKPressed();
                    dismiss();
//
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().cancel();
                }
            });

            return view;
        }

        private void onOKPressed() {
            if (listener != null) {
                listener.onFragmentInteraction();
            }
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            if (context instanceof DeleteProfileDialog.OnFragmentInteractionListener) {
                listener = (DeleteProfileDialog.OnFragmentInteractionListener) context;
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
    void onFragmentInteraction();

}
}
