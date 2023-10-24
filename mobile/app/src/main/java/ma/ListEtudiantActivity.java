package ma;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import ma.ensa.Etudiant;
import ma.ensa.EtudiantAdapter;

import ma.ensa.list.R;



public class ListEtudiantActivity extends AppCompatActivity {
    private List<Etudiant> etudiants;
    private RecyclerView recyclerView;

    private Button btn;
    RequestQueue requestQueue;
    String insertUrl = "http://192.168.1.102/PhpTpVolley/ws/loadEtudiant.php";

    private EtudiantAdapter etudiantAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btn = findViewById(R.id.add);
        setContentView(R.layout.activity_list_etudiant);
        etudiants = new ArrayList<>();
        init();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListEtudiantActivity.this, AddEtudiant.class);
                startActivity(intent);
                finish();
            }
        });


    }

    public void init() {
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type type = new TypeToken<ArrayList<Etudiant>>() {}.getType();
                etudiants = new Gson().fromJson(response, type);
                recyclerView = findViewById(R.id.recycle_vie);
                etudiantAdapter = new EtudiantAdapter(ListEtudiantActivity.this, etudiants);
                recyclerView.setAdapter(etudiantAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ListEtudiantActivity.this));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return null;
            }
        };
        requestQueue.add(request);

    }
}