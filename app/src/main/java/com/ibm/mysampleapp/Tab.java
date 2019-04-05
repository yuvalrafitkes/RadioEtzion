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
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.backendless.rt.RTTypes.log;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context context;
    private View rootView;

    private List<ClsRadio> radioList;
    private ListAdapter adapter;
    private ListView listView;

    private OnFragmentInteractionListener mListener;

    public Tab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab newInstance(String param1, String param2) {
        Tab fragment = new Tab();
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
        rootView = inflater.inflate(R.layout.fragment_tab, container, false);
        setPointer();
        return rootView;
    }

    private void setPointer() {
        context = getActivity();
        FirebaseApp.initializeApp(context);
        listView = rootView.findViewById(R.id.list);
        radioList = new ArrayList<>();
        adapter = new ListAdapter(context, radioList);
        listView.setAdapter(adapter);

        new DataTask().execute();

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


    private class DataTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... voids) {

            HttpURLConnection connection = null;

            try {
                //connection
                connection = (HttpURLConnection) new URL("http://be.repoai.com:5080/WebRTCAppEE/rest/broadcast/getVodList/0/100?fbclid=IwAR3T5numCWbEGoiDcbAbd9zlqUepMifjMOx-W3m5DpEIjXCMRR8u3lTFpFI").openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                ////insert to string
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();

                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String jsonString) {

            if (jsonString != null) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonString);

//                    Backendless.initApp(context, "2D5E6DA5-6B22-F84B-FFFD-67F33605D300", "2AE60844-6F42-4417-FFDE-44CA6B050B00");

                    Backendless.Messaging.registerDevice(new AsyncCallback<DeviceRegistrationResult>() { // launching the push notification we created via backendless and firebase
                        @Override
                        public void handleResponse(DeviceRegistrationResult response) {
                            //Toast.makeText(context, "registered to push", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                           Log.e("err", "handleFault: " + fault.getDetail());

                        }
                    });

                    radioList.clear();
                    for (int i = 0; i < jsonArray.length(); i += 1) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ClsRadio radio = new ClsRadio(jsonObject.getString("vodName"), jsonObject.getString("filePath"));
                        radioList.add(radio);
                        broadcasts newBroadcast = new broadcasts();
                        for (int j = 0; j < radioList.size(); j += 1) {
                            if ((Backendless.Data.of(jsonObject.getString("filePath")).equals(radioList.get(j).filePath))) { // checks if broadcasts already exists according to the filePath
                                newBroadcast.setVodId(jsonObject.getString("vodId"));
                                newBroadcast.setStreamName(jsonObject.getString("streamName"));
                                newBroadcast.setVodName(jsonObject.getString("vodName"));
                                newBroadcast.setStreamId(jsonObject.getString("streamId"));
                                newBroadcast.setCreationDate(jsonObject.getInt("creationDate"));
                                newBroadcast.setDuration(jsonObject.getInt("duration"));
                                newBroadcast.setFileSize(jsonObject.getInt("fileSize"));
                                newBroadcast.setFilePath(jsonObject.getString("filePath"));
                                newBroadcast.setType(jsonObject.getString("type"));

                                Backendless.Data.of(broadcasts.class).save(newBroadcast, new AsyncCallback<broadcasts>() {
                                    @Override
                                    public void handleResponse(broadcasts response) {
                                        Toast.makeText(context, "all data saved", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Log.e("err", "handleFault: " + fault.getDetail());
                                    }
                                });
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        }

    }
