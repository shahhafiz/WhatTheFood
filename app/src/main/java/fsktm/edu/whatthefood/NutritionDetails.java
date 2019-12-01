package fsktm.edu.whatthefood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class NutritionDetails extends AppCompatActivity {
    TextView calories,carbohydrate,carbper,fat,fatper,fiber,fiberper,protein,proteinper,sodium,sodiumper;
    ImageView image;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_details);

        calories = (TextView)findViewById(R.id.tvCal);
        carbohydrate= (TextView)findViewById(R.id.tvCarbohydrate);
        carbper = (TextView)findViewById(R.id.tvCarbPer);
        fat = (TextView)findViewById(R.id.tvFat);
        fatper = (TextView)findViewById(R.id.tvFatPer);
        fiber = (TextView)findViewById(R.id.tvFiber);
        fiberper = (TextView)findViewById(R.id.tvFiberPer);
        protein = (TextView)findViewById(R.id.tvProtein);
        proteinper = (TextView)findViewById(R.id.tvProteinPer);
        sodium = (TextView)findViewById(R.id.tvSodium);
        sodiumper = (TextView)findViewById(R.id.tvSodiumPer);

        image = (ImageView)findViewById(R.id.imageDetails);


        Intent intent = getIntent();
        String name = intent.getStringExtra("Name");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("nutritionvalue").document(name)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                           calories.setText(documentSnapshot.getString("calories"));
                           carbohydrate.setText(documentSnapshot.getString("carbohydrate"));
                           carbper.setText(documentSnapshot.getString("carbper"));
                           fat.setText( documentSnapshot.getString("fat"));
                           fatper.setText( documentSnapshot.getString("fatper"));
                           fiber.setText( documentSnapshot.getString("fiber"));
                           fiberper.setText( documentSnapshot.getString("fiberper"));
                           protein.setText(  documentSnapshot.getString("protein"));
                           proteinper.setText(  documentSnapshot.getString("proteinper"));
                           sodium.setText( documentSnapshot.getString("sodium"));
                           sodiumper.setText( documentSnapshot.getString("sodiumper"));

                            Glide.with(NutritionDetails.this)
                                    .asBitmap()
                                    .load(documentSnapshot.getString("imageURL"))
                                    .into(image);



                        } else {
                            Log.d("JAWAPAN", "No such document");
                            Toast.makeText(NutritionDetails.this,"Don't Exist",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
