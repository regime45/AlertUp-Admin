package com.android.classified_zone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class list_of_disease_adapter extends FirebaseRecyclerAdapter<geo_model, list_of_disease_adapter.myviewholder>


{

    private  Context context;
    private Intent intent;

    public list_of_disease_adapter(@NonNull FirebaseRecyclerOptions<geo_model> options)

    {
        super(options);
        this.context=context;

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_of_disease,parent,false);
        return new myviewholder(view);
    }


    public class myviewholder extends RecyclerView.ViewHolder

    {
        CircleImageView img;
        ImageView edit,delete, add;

        TextView name, description, safety;
        EditText radius;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);


            name=(TextView)itemView.findViewById(R.id.disease_name);
            radius=(EditText)itemView.findViewById(R.id.radius);
            description= (TextView)itemView.findViewById(R.id.discreption);
            safety= (TextView)itemView.findViewById(R.id.safety);
            edit=(ImageView)itemView.findViewById(R.id.editicon);
            delete=(ImageView)itemView.findViewById(R.id.deleteicon);

        }

    }


    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, final int position, @NonNull final geo_model model)
    {
        // Add Geo_Name from model class (here
        // "geo_adapter.class")to appropriate view in Card
        // view (here "singlerow.xml")
        holder.name.setText(model.getdisease_name());
        holder.description.setText(model.getdisease_description());


        context = holder.itemView.getContext();
        //Toast.makeText(context, model.getdisease_name()  , Toast.LENGTH_LONG).show();

        holder.edit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus=DialogPlus.newDialog(holder.edit.getContext())
                        .setContentHolder(new ViewHolder(R.layout.content_disease))
                        .setExpanded(false,1800)
                        .create();

                View myview=dialogPlus.getHolderView();
                final EditText purl=myview.findViewById(R.id.uimgurl);

                final EditText  descript_1=myview.findViewById(R.id. descript_1);
                final EditText  rad =myview.findViewById(R.id.radius);
                final EditText saf= myview.findViewById(R.id.safety);




                Button submit=myview.findViewById(R.id.usubmit);

                purl.setText(model.getdisease_name());
                descript_1.setText(model.getdisease_description());
                saf.setText(model.getalert_message());




                dialogPlus.show();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, point_1.class);
                       // intent.putExtra("name_disease",model.getdisease_name());

                        String radi = rad.getText().toString();
                        String disease_name = model.getdisease_name();
                        String description_dis= model.getdisease_description();
                        String safe= model.getalert_message();

                        intent.putExtra("name_disease",disease_name );
                        intent.putExtra("description_disease",description_dis);
                        intent.putExtra("keyradius",radi );
                        intent.putExtra("keysafe",safe );

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("alerts_zone").child("classified_zone");
                        DatabaseReference newChildRef = myRef.push();
                        String key = newChildRef.getKey();
                        intent.putExtra("key",key );

                        if (key!= null) {

                            myRef.child(key).child("Geo_Name").setValue(disease_name);
                            myRef.child(key).child("Description").setValue(description_dis);
                            myRef.child(key).child("alert_message").setValue(safe);

                            //  }
                            //Toast.makeText(context, " save successfully...", Toast.LENGTH_SHORT).show();
                        }

                         context.startActivity(intent);


                        /*
                        Map<String,Object> map=new HashMap<>();

                        map.put("Geo_Name",purl.getText().toString());

                        FirebaseDatabase.getInstance().getReference("alerts_zone").child("list_of_disease")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialogPlus.dismiss();
                                    }
                                });*/
                    }

                });




            }
        });


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.edit.getContext());
                builder.setTitle("Delete Panel");
                builder.setMessage("Delete...?");
                // Intent intent = new Intent(view.getContext(), Fire_Activity.class);


                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // String time    =  model.getlatitude();
                        //String  lastime = model.getlongitude();

                        // Long times = Long.parseLong( time);
                        // Long lastimes = Long.parseLong( lastime);

                        //if (times > lastimes){
                        FirebaseDatabase.getInstance().getReference("alerts_zone").child("list_of_disease")
                                .child(getRef(position).getKey()).removeValue();

                        // String ss = getRef(position).getKey();
                        //Toast.makeText(context, model.getlatitude()  , Toast.LENGTH_LONG).show();
                    }//}
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });


    } // End of OnBindViewMethod
}
