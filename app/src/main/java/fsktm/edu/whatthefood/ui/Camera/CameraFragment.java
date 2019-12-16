package fsktm.edu.whatthefood.ui.Camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;

import java.util.List;
import fsktm.edu.whatthefood.Labeler;
import fsktm.edu.whatthefood.NutritionDetails;
import fsktm.edu.whatthefood.R;

import static android.app.Activity.RESULT_OK;

public class CameraFragment extends Fragment {
    FirebaseVisionImageLabeler labeler;
    private CameraViewModel importViewModel;
    private int REQUEST_IMAGE_CAPTURE =101;
    private ImageView image;
    TextView tvLabel;
    TextView tvConfidence;
    Button detailsBtn;

    @Override


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_camera, container, false);
        importViewModel = ViewModelProviders.of(this).get(CameraViewModel.class);

        Labeler labelerObj = new Labeler();
        labeler = labelerObj.getLabeler();

        tvLabel = root.findViewById(R.id.label);
        tvConfidence = root.findViewById(R.id.confident);
        image = root.findViewById(R.id.imageView);

        Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CAPTURE);
        return root;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            image.setImageBitmap(imageBitmap);

            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);
            labeler.processImage(image)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                            // Task completed successfully
                            // ...
                            for (FirebaseVisionImageLabel label : labels) {
                                String text = label.getText();
                                float confidence = label.getConfidence();



                                tvLabel.setText(text);
                                tvConfidence.setText(String.valueOf((int)(confidence*100))+"%");

                                detailsBtn =  getView().findViewById(R.id.detailsBtn);
                                detailsBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        NutritionDetails(text.substring(0, 1).toUpperCase() + text.substring(1));
                                    }
                                });


                                System.out.println(text);

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Task failed with an exception
                            // ...
                        }
                    });
        }
    }


    public void NutritionDetails(String text){
        Intent i = new Intent(getContext(), NutritionDetails.class);
        i.putExtra("Name",text);
        startActivity(i);
    }
}