package my.com.tm.igated;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListGated extends AppCompatActivity {

    private ProgressDialog loading;

    private ListView listView;
    EditText editext;
    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_gated);

        getJSON();
    }
    private void showEmployee(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_SCHEDULE);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                //  String a = jo.getString(Config.TAG_NO);
                // String b = jo.getString(Config.TAG_REGION);
                String c = jo.getString(Config.TAG_CABID);
                String d = jo.getString(Config.TAG_IPA);
                String e = jo.getString(Config.TAG_STA);

                String f = jo.getString(Config.TAG_SEQ);
                String g = jo.getString(Config.TAG_TASK);
                String h = jo.getString(Config.TAG_TEAM);
                String ii = jo.getString(Config.TAG_FINAL);



                HashMap<String,String> employees = new HashMap<>();
                //  employees.put(Config.TAG_NO,a);
                // employees.put(Config.TAG_REGION,b);
                employees.put(Config.TAG_CABID,c);
                employees.put(Config.TAG_IPA,d);
                employees.put(Config.TAG_STA,e);

                employees.put(Config.TAG_SEQ,f);
                employees.put(Config.TAG_TASK,g);
                employees.put(Config.TAG_TEAM,h);
                employees.put(Config.TAG_FINAL,ii);

                list.add(employees);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                getApplicationContext(), list, R.layout.activity_list_gated,
                new String[]{Config.TAG_CABID,Config.TAG_IPA,Config.TAG_STA
                        ,Config.TAG_SEQ,Config.TAG_TASK,Config.TAG_TEAM,Config.TAG_FINAL},

                new int[]{R.id.dua,R.id.dua, R.id.tiga, R.id.empat, R.id.lima, R.id.enam, R.id.tujuh});

        listView.setAdapter(adapter);

    }


    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
               // loading = ProgressDialog.show(getApplicationContext(),"Loading Data","Wait...",false,false);
                //  loading.setIndeterminateDrawable(getResources().getDrawable(R.drawable.gated));
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                loading.dismiss();
                //loading.setIndeterminateDrawable(getResources().getDrawable(R.drawable.gated));
                JSON_STRING = s;
                showEmployee();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler3 rh = new RequestHandler3();
                String s = rh.sendGetRequest(Config.URL_GET);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

}
