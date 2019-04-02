package com.ibm.mysampleapp;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.push.DeviceRegistrationResult;
import com.google.firebase.FirebaseApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context context;
    private View rootView;

    private List<ClsRadio> faveList;
    private Tab1ListAdapter adapter;
    private ListView listView;

    private OnFragmentInteractionListener mListener;

    public Tab1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab1.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab1 newInstance(String param1, String param2) {
        Tab1 fragment = new Tab1();
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
        rootView =  inflater.inflate(R.layout.fragment_tab1, container, false);
        setPointer();
        return rootView;
    }

    private void setPointer() {
        context = getActivity();
        listView = rootView.findViewById(R.id.list);
        faveList = new ArrayList<>();
        adapter = new Tab1ListAdapter(context,faveList);
        listView.setAdapter(adapter);

        new Tab1.DataTask().execute();

    }

    private class DataTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {

        }

//        @Override
       protected String doInBackground(Void... voids) {
////
////            HttpURLConnection connection = null;
////
////            try {
////                //connection
////                connection = (HttpURLConnection) new URL("http://be.repoai.com:5080/WebRTCAppEE/rest/broadcast/getVodList/0/100?fbclid=IwAR3T5numCWbEGoiDcbAbd9zlqUepMifjMOx-W3m5DpEIjXCMRR8u3lTFpFI").openConnection();
////
////                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
////                ////insert to string
////                StringBuilder sb = new StringBuilder();
////                String line;
////
////                while ((line = reader.readLine()) != null) {
////                    sb.append(line);
////                }
//
////                reader.close();
//
////                return sb.toString();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (connection != null) {
//                    connection.disconnect();
//                }
//            }
//
           return null;
        }

        @Override
        protected void onPostExecute(String jsonString) {

//            if (jsonString != null) {
//                try {
//                    JSONArray jsonArray = new JSONArray(jsonString);

                    //Backendless.initApp(context, "2D5E6DA5-6B22-F84B-FFFD-67F33605D300", "2AE60844-6F42-4417-FFDE-44CA6B050B00");

//                    Backendless.Messaging.registerDevice(new AsyncCallback<DeviceRegistrationResult>() { // launching the push notification we created via backendless and firebase
//                        @Override
//                        public void handleResponse(DeviceRegistrationResult response) {
//                            //Toast.makeText(context, "registered to push", Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void handleFault(BackendlessFault fault) {
//                            Log.e("err", "handleFault: " + fault.getDetail());
//
//                        }
//                    });
                        adapter.notifyDataSetChanged();
                    }

                }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
