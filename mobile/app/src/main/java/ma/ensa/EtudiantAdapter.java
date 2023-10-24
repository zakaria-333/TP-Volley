package ma.ensa;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.RadioButton;

import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.ListEtudiantActivity;
import ma.ensa.list.R;

public class EtudiantAdapter extends RecyclerView.Adapter<EtudiantAdapter.EtudiantViewHolder> {
    private static final String TAG = "EtudiantAdapter";
    private List<Etudiant> etudiants;
    private Context context;
    RequestQueue requestQueue;


    String s;

    String insertUrl;

    public EtudiantAdapter(Context context, List<Etudiant> etudiants) {
        this.etudiants = etudiants;
        this.context = context;
        this.insertUrl = "http://192.168.1.102/PhpTpVolley/ws/updateEtudiant.php";
        this.requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }


    @NonNull
    @Override
    public EtudiantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.etudiant_item,
                viewGroup, false);

        final EtudiantAdapter.EtudiantViewHolder holder = new EtudiantAdapter.EtudiantViewHolder(v);
        holder.deleteBtn = v.findViewById(R.id.delete);
        holder.deleteBtn.setOnClickListener(v12 -> {
            String id = ((TextView) v.findViewById(R.id.ids)).getText().toString();

            // Créez une boîte de dialogue de confirmation
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Confirmation");
            builder.setMessage("Voulez-vous vraiment supprimer cet étudiant ?");

            builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Effectuez la suppression ici
                    requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                    StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.1.102/PhpTpVolley/ws/deleteEtudiant.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Type type = new TypeToken<ArrayList<Etudiant>>() {}.getType();
                            etudiants = new Gson().fromJson(response, type);

                            // Mettez à jour la vue après la suppression
                            notifyDataSetChanged();

                            // Affichez un message de succès
                            Toast.makeText(context, "Étudiant supprimé avec succès", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Gérez les erreurs en cas d'échec de la suppression
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<String, String>();
                            params.put("id", id);
                            return params;
                        }
                    };
                    requestQueue.add(request);
                }
            });

            builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        holder.itemView.setOnClickListener(v1 -> {
            View popup = LayoutInflater.from(context).inflate(R.layout.etudiant_edit_item, null,
                    false);
            final EditText nom = popup.findViewById(R.id.no);
            final EditText prenom = popup.findViewById(R.id.pre);
            final TextView id = popup.findViewById(R.id.idddd);
            final Spinner ville = popup.findViewById(R.id.vil);

            final RadioButton homme = popup.findViewById(R.id.h);
            final RadioButton femme = popup.findViewById(R.id.fe);
            nom.setText(((TextView) v1.findViewById(R.id.nomEtudiant)).getText().toString());
            id.setText(((TextView) v1.findViewById(R.id.ids)).getText().toString());
            prenom.setText(((TextView) v1.findViewById(R.id.prenomEtudiant)).getText().toString());

            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) ville.getAdapter();
            String selectedValue = ((TextView) v1.findViewById(R.id.villeEtudiant)).getText().toString();
            int position = adapter.getPosition(selectedValue);
            ville.setSelection(position);

            String sexe = ((TextView) v1.findViewById(R.id.sex)).getText().toString();

            if (sexe.equals("homme")) {
                homme.setChecked(true);
                femme.setChecked(false);
            } else if (sexe.equals("femme")) {
                homme.setChecked(false);
                femme.setChecked(true);
            }
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("Modification")
                    .setView(popup)
                    .setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String n = nom.getText().toString();
                            String p = prenom.getText().toString();
                            String ids = id.getText().toString();

                            if(homme.isChecked())
                                s = "homme";
                            else{
                                s = "femme";
                            }
                            String v1 = ville.getSelectedItem().toString();
                            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                            StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Type type = new TypeToken<ArrayList<Etudiant>>() {}.getType();
                                    etudiants = new Gson().fromJson(response, type);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String, String> params = new HashMap<String, String>();
                                    params.put("nom", n);
                                    params.put("prenom", p);
                                    params.put("ville", v1);
                                    params.put("sexe", s);
                                    params.put("id", ids);
                                    return params;
                                }
                            };
                            requestQueue.add(request); // Make the request as before

                            notifyItemChanged(holder.getAdapterPosition());

                            // Add this code to display a success message
                            Toast.makeText(context, "Étudiant modifié avec succès", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Annuler", null)
                    .create();
            dialog.show();
        });
        return new EtudiantViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull EtudiantViewHolder EtudiantViewHolder, int i) {
        Log.d(TAG, "onBindView call ! "+ i);
        EtudiantViewHolder.nom.setText(etudiants.get(i).getNom().toUpperCase());
        EtudiantViewHolder.prenom.setText(etudiants.get(i).getPrenom());
        EtudiantViewHolder.idss.setText(etudiants.get(i).getId()+"");
        EtudiantViewHolder.ville.setText(etudiants.get(i).getVille());
        EtudiantViewHolder.sexe.setText(etudiants.get(i).getSexe());
    }
    @Override
    public int getItemCount() {
        return etudiants.size();
    }
    public class EtudiantViewHolder extends RecyclerView.ViewHolder {
        public View deleteBtn;
        TextView idss;
        TextView nom;
        TextView prenom;
        TextView ville;
        TextView sexe;
        RelativeLayout parent;
        public EtudiantViewHolder(@NonNull View itemView) {
            super(itemView);
            idss = itemView.findViewById(R.id.ids);
            nom = itemView.findViewById(R.id.nomEtudiant);
            prenom = itemView.findViewById(R.id.prenomEtudiant);
            ville = itemView.findViewById(R.id.villeEtudiant);
            sexe = itemView.findViewById(R.id.sex);

            parent = itemView.findViewById(R.id.parent);
        }
    }
}