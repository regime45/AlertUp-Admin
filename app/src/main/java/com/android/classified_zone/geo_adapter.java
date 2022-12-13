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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class geo_adapter extends FirebaseRecyclerAdapter<geo_model, geo_adapter.myviewholder>


{

    private  Context context;
    private Intent intent;

    public geo_adapter(@NonNull FirebaseRecyclerOptions<geo_model> options)

    {
        super(options);
        this.context=context;

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new myviewholder(view);
    }


    public class myviewholder extends RecyclerView.ViewHolder

    {
        CircleImageView img;
        ImageView edit,delete;

        TextView name,course,email, purl, Purok, Description, alert;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);


            name=(TextView)itemView.findViewById(R.id.nametext);
            course=(TextView)itemView.findViewById(R.id.coursetext);
            email=(TextView)itemView.findViewById(R.id.emailtext);
            purl=(TextView)itemView.findViewById(R.id.longitude);
            Purok=(TextView)itemView.findViewById(R.id.Purok);
            Description=(TextView)itemView.findViewById(R.id.Description);
            edit=(ImageView)itemView.findViewById(R.id.editicon);
            delete=(ImageView)itemView.findViewById(R.id.deleteicon);
            alert=(TextView)itemView.findViewById(R.id.alert_message);
        }

    }


    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, final int position, @NonNull final geo_model model)
    {
        // Add Geo_Name from model class (here
        // "geo_adapter.class")to appropriate view in Card
        // view (here "singlerow.xml")
        holder.name.setText(model.getGeo_Name());
        holder.course.setText(model.getRadius());
        holder.email.setText(model.getlatitude());
        holder.purl.setText(model.getlongitude());
        holder.alert.setText(model.getalert_message());
        holder.Purok.setText(model.getPurok());
        holder.Description.setText(model.getDescription());

        context = holder.itemView.getContext();

        FirebaseDatabase.getInstance().getReference("covid_tool").child("covid_zone")
                .child(getRef(position).getKey());

        //Toast.makeText(context, getRef(position).getKey()  , Toast.LENGTH_LONG).show();

/*
        // new date format
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyyMMdd");

        //expiration date
        long cutoff = new Date().getTime()+ TimeUnit.MILLISECONDS.convert(14, TimeUnit.DAYS);
        String limit_date = dateFormatGmt.format(new Date(cutoff));
        Long expire = Long.parseLong( limit_date);

        // Current date
       // SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDate = dateFormatGmt.format(new Date());
        Long current = Long.parseLong(currentDate);

        if (current>expire){
            FirebaseDatabase.getInstance().getReference("covid_tool").child("covid_zone")
                    .child(getRef(position).getKey()).removeValue();
        }

        Toast.makeText(context,limit_date, Toast.LENGTH_LONG).show();

 */
//Glide.with(holder.img.getContext()).load(model.getimageURL()).into(holder.img);

                   holder.edit.setOnClickListener(new View.OnClickListener() {


                       @Override
                        public void onClick(View view) {
                            final DialogPlus dialogPlus=DialogPlus.newDialog(holder.edit.getContext())
                                    .setContentHolder(new ViewHolder(R.layout.dialogcontent))
                                    .setExpanded(false,1800)
                                    .create();

                            View myview=dialogPlus.getHolderView();
                            final EditText purl=myview.findViewById(R.id.uimgurl);
                            final EditText name=myview.findViewById(R.id.uname);
                            final EditText course=myview.findViewById(R.id.ucourse);
                            final EditText email=myview.findViewById(R.id.uemail);
                            final EditText Purok=myview.findViewById(R.id.Purok);
                           final  EditText alerto=myview.findViewById(R.id.alerto_message);
                            final EditText Description=myview.findViewById(R.id.description);

                            Button submit=myview.findViewById(R.id.usubmit);

                            purl.setText(model.getGeo_Name());
                            name.setText(model.getRadius());
                            course.setText(model.getlatitude());
                            email.setText(model.getlongitude());
                            Purok.setText(model.getPurok());
                            alerto.setText(model.getalert_message());
                            Description.setText(model.getDescription());
                            dialogPlus.show();

                           submit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Map<String,Object> map=new HashMap<>();

                                       map.put("Geo_Name",purl.getText().toString());
                                        map.put("Radius",name.getText().toString());
                                        map.put("longitude",email.getText().toString());
                                        map.put("latitude",course.getText().toString());
                                        map.put("Purok",Purok.getText().toString());
                                        map.put("alert_message",alerto.getText().toString());
                                         map.put("Description",Description.getText().toString());

                                        FirebaseDatabase.getInstance().getReference("alerts_zone").child("classified_zone")
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
                                                });
                                    }
                                });




                        }
                    });


                    holder.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder=new AlertDialog.Builder(holder.img.getContext());
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
                                    FirebaseDatabase.getInstance().getReference("alerts_zone").child("classified_zone")
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
