package my.com.tm.igated;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class PofileActivity extends Fragment implements View.OnClickListener {

    RequestQueue rq;
    String totalhandle,mitos,monitoring,unresolve;
    //firebase auth object
    private FirebaseAuth firebaseAuth;
    ProgressDialog dialog;
    //view objects

    private TextView textViewUserEmail,tthandle,mitosuser,monitor,un;
    private EditText cpass;
    private Button buttonLogout,btnchange;
    private String JSON_STRING;

    View myView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_pofile, container, false);



        //getting current user
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            //finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();


        //initializing views
        monitor = (TextView) myView.findViewById(R.id.monitoring);
        mitosuser = (TextView) myView.findViewById(R.id.mitosuser) ;
        tthandle = (TextView) myView.findViewById(R.id.tthandle);
        un = (TextView) myView.findViewById(R.id.unresolve);

        textViewUserEmail = (TextView) myView.findViewById(R.id.textViewUserEmail);
        cpass = (EditText) myView.findViewById(R.id.change);
        buttonLogout = (Button) myView.findViewById(R.id.buttonLogout);
        btnchange = (Button) myView.findViewById(R.id.btnchange);

        //displaying logged in user name
        textViewUserEmail.setText("Welcome "+ user.getEmail());

        //adding listener to button
        buttonLogout.setOnClickListener(this);
        btnchange.setOnClickListener(this);

        rq = Volley.newRequestQueue(getActivity());
        sendrequest();

        return myView;

        }

    public void sendrequest(){

        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET,Config.URL_GET_HEADER, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try{
                    totalhandle = response.getString("totaltt");
                    mitos = response.getString("mitosuser");
                    monitoring = response.getString("cabinetmonitor");
                    unresolve = response.getString("totalaging");

                    tthandle.setText(totalhandle);
                    mitosuser.setText(mitos);
                    monitor.setText(monitoring);
                    un.setText(unresolve);

                }catch (JSONException e){
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        rq.add(jsonObjectRequest);
    }


    public void changepassword (View v)
    {
        String password  = cpass.getText().toString().trim();


        //checking if email and passwords are empty
        if(TextUtils.isEmpty(password)){
            Toast.makeText(getActivity(),"Please enter new password",Toast.LENGTH_LONG).show();
            return;
        }

        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
//            dialog.setMessage("Changing Password , Please Wait ...");
//            dialog.show();
            user.updatePassword(cpass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {

                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
//                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Successfully Changed", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                       // finish();
                        Intent i = new Intent(getActivity(),LoginActivity.class);
                        startActivity(i);
                        firebaseAuth.signOut();
                    }

                    else
                    {
                        dialog.dismiss();
                        dialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.gated));
                        Toast.makeText(getActivity(), "Could not change", Toast.LENGTH_SHORT).show();
                    }

            }

        });
    }}


    @Override
    public void onClick(View view) {
        //if logout is pressed
        if(view == buttonLogout){
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            //finish();
            //starting login activity
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        if(view == btnchange){
            changepassword(view);
            //firebaseAuth.();
        }
    }
}
