package my.com.tm.igated;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GatedFragment extends Fragment implements ListView.OnItemClickListener{

    private ProgressDialog loading;

    private ListView listView;
    EditText editext;


    View myView;

    private String JSON_STRING;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_gated_fragment, container, false);

        final Button searching =(Button) myView. findViewById(R.id.btnsearch);
        editext = (EditText)myView. findViewById(R.id.gated);

        listView = (ListView) myView.findViewById(R.id.list);
        listView.setOnItemClickListener(this);

//        listView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });
        searching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getJSONgated(editext.getText().toString().toUpperCase());

            }
        });

        return myView;

    }

    private void showEmployee(){
        String gate = editext.getText().toString().trim();
        //checking if email and passwords are empty
        if(TextUtils.isEmpty(gate)){
            Toast.makeText(getActivity(),"Please enter cabinet id",Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject jsonObject = null;
        ArrayList<GatedModel> listgated = new ArrayList<>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_GATED);

            for(int i = 0; i<result.length(); i++){

                //GatedModel gatedModel = new GatedModel();
                JSONObject jo = result.getJSONObject(i);
                String d = jo.getString(Config.TAG_TASK);
                String e = jo.getString(Config.TAG_TEAM);
                String f = jo.getString(Config.TAG_SEQ);
                String g = jo.getString(Config.TAG_FINAL);

                GatedModel gatedModel = new GatedModel();

                gatedModel.setTask(d);
                gatedModel.setTeam(e);
                gatedModel.setSequence(f);
                gatedModel.setFinalstatus(g);

                listgated.add(gatedModel);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        final GatedAdapter sanoAdapter = new GatedAdapter(getActivity(),R.layout.listtgatedd,listgated);
        final ListView sanoview = (ListView) myView.findViewById(R.id.list);
        sanoview.setAdapter(sanoAdapter);

        listView.setAdapter(sanoAdapter);

    }

    private void getJSONgated(final String tmnode){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override

            protected void onPreExecute() {

                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Loading Data","Wait...",false,false);
                loading.setIndeterminateDrawable(getResources().getDrawable(R.drawable.gated));
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                loading.setIndeterminateDrawable(getResources().getDrawable(R.drawable.gated));
                JSON_STRING = s;
                showEmployee();

            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler3 rh = new RequestHandler3();
                String s = rh.sendGetRequest(Config.URL_GET + "?cabinetid="+tmnode);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ListGated.class);
        startActivity(intent);
    }
}



