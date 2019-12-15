package fsktm.edu.whatthefood.ui.Import;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import fsktm.edu.whatthefood.MainActivity;
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


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        importViewModel =
                ViewModelProviders.of(this).get(ImportViewModel.class);
        View root = inflater.inflate(R.layout.fragment_import, container, false);


        imageView = root.findViewById(R.id.imageView);
        //btn_gallery = root.findViewById(R.id.btn_gallery);

   
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            //set image to image view
            imageView.setImageURI(data.getData());




        }
    }
}