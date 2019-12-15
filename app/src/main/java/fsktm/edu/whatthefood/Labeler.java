package fsktm.edu.whatthefood;

import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.automl.FirebaseAutoMLLocalModel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;

public class Labeler {
    public FirebaseVisionImageLabeler getLabeler(){
        FirebaseAutoMLLocalModel localModel = new FirebaseAutoMLLocalModel.Builder()
                .setAssetFilePath("manifest.json")
                .build();

        try {
            FirebaseVisionOnDeviceAutoMLImageLabelerOptions options = new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder(localModel)
                    .setConfidenceThreshold(0.5f)  // Evaluate your model in the Firebase console
                                                    // to determine an appropriate value.
                    .build();

            return FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(options);

        } catch (FirebaseMLException e) {
            // ...
            return null;
        }
    }
}
