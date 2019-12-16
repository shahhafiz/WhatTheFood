package fsktm.edu.whatthefood.ui.Import;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import java.io.IOException;
import java.util.List;

import fsktm.edu.whatthefood.Labeler;
import fsktm.edu.whatthefood.NutritionDetails;
import fsktm.edu.whatthefood.R;

import static android.app.Activity.RESULT_OK;

public class ImportFragment extends Fragment {

    private ImportViewModel importViewModel;
    FirebaseVisionImageLabeler labeler;

    ImageView imageView;
    //Button btn_gallery;
    private static final int PICK_IMAGE = 100;
    private static final int PERMISSION_CODE = 101;
    TextView tvLabel;
    TextView tvConfidence;
    Button detailsBtn;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        importViewModel =
                ViewModelProviders.of(this).get(ImportViewModel.class);
        View root = inflater.inflate(R.layout.fragment_import, container, false);





        Labeler labelerObj = new Labeler();
        labeler = labelerObj.getLabeler();
    //initialiaze views
        tvLabel = root.findViewById(R.id.label);
        tvConfidence = root.findViewById(R.id.confident);
        imageView = root.findViewById(R.id.imageView);

   //check permission run time
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //to check if the activity has permission  to access gallery
                    //activitycompat is used because fragment
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission not granted, request it.
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup for runtime
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        //permission already granted
                        openGallery();
                    }
                } else {
                    //system os is less than marshmallow
                    openGallery();
                }



        return root;
    }

    private void openGallery() {
        //intent to pick image
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setType("image/*");
        startActivityForResult(gallery, PICK_IMAGE);
    }
    //handle result of runtime permission
    //this method override in activity to detect when user has accepted the permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted
                    openGallery();
                } else {
                    //permission was denied
                    Toast.makeText(getContext(), "Permission denied...!", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    //handle result of picked image
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){

            //set image to image view
            imageView.setImageURI(data.getData());

            FirebaseVisionImage image = null;
            try {
                image = FirebaseVisionImage.fromFilePath(getContext(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }

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

                                detailsBtn =  getView().findViewById(R.id.btnNutDet);
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