package com.example.echo;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;

import com.ibm.watson.personality_insights.v3.model.Content;
import com.ibm.watson.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.tone_analyzer.v3.model.ToneOptions;
import com.ibm.watson.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.visual_recognition.v3.model.ClassifyOptions;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import okhttp3.Authenticator;

import static android.content.Context.MODE_PRIVATE;

/**
 * Author:Ruibo Liu(ruibo.liu.gr@dartmoputh.edu)
 *
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class CreateNoteFragment extends Fragment {

    private static final String ARG_NOTE_ID = "note_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_USE_CAMERA_AND_EXTERNAL_STORAGE = 4;
    private static final int REQUEST_USE_CAMERA = 1;
    private static final int REQUEST_USE_EXTERNAL_STORAGE = 2;
    private static final int REQUEST_CODE_CAMERA_CAPTURE = 3;

    public static int NEUTRAL = 0;
    public static int ANGER = 1;
    public static int FEAR = 2;
    public static int JOY = 3;
    public static int SADNESS = 4;

    private Note mNote;
    private EditText mTitleField;
    private EditText mNoteTextField;
    private Button mDateButton, mEchoButton;
    private Button mImageUpload;
    public ToneAnalysis tone;
    private Uri mProfileImageUri;
    private ImageView mProfileImageView;

    private boolean mUseCameraEnabled;
    private boolean mUseStorageEnabled;
    private boolean mStartCameraActivityWaiting = false;

    /**
     * helper function to create instance of CreateNoteFragment given note_id
     *
     * @param noteID
     * @return
     */
    public static CreateNoteFragment newInstance(UUID noteID) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE_ID, noteID);

        CreateNoteFragment fragment = new CreateNoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // extract the corresponding note
        UUID noteID = (UUID) getArguments().getSerializable(ARG_NOTE_ID);
        mNote = NoteFactory.get(getActivity()).getNote(noteID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        mTitleField = view.findViewById(R.id.note_title);
        mTitleField.setText(mNote.getTitle()); // set text when given the note
        Typeface titleFont = Typeface.createFromAsset(getContext().getAssets(), "font/title.ttf");
        mTitleField.setTypeface(titleFont);
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mNoteTextField = view.findViewById(R.id.note_text);
        mNoteTextField.setText(mNote.getNoteText()); // set text when given the note
        mNoteTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setNoteText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = view.findViewById(R.id.note_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                NoteDateFragment dialog = NoteDateFragment.newInstance(mNote.getDate());
                dialog.setTargetFragment(CreateNoteFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mProfileImageView = view.findViewById(R.id.image);
        mImageUpload = view.findViewById(R.id.img_button);
        mImageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
                if (mUseCameraEnabled && mUseStorageEnabled) {
                    startCameraActivity();
                } else {
                    mStartCameraActivityWaiting = true;
                }
            }
        });

        mEchoButton = view.findViewById(R.id.echo_button);
        mEchoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = mNote.getNoteText();
                final UpdateTask task = new UpdateTask();
                task.execute(text);
            }
        });


        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(NoteDateFragment.EXTRA_NOTE_DATE);
            mNote.setDate(date);
            updateDate();
            Toast.makeText(getActivity(), date.toString(), Toast.LENGTH_SHORT).show();
        }
        if(requestCode ==  REQUEST_CODE_CAMERA_CAPTURE){
            mProfileImageView.setImageURI(mProfileImageUri);
            saveProfileImage();
        }
    }

    private void updateDate() {
        mDateButton.setText(mNote.getDate().toString());
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    //tone analysis thread
    private class UpdateTask extends AsyncTask<String, String, double[]> {
        protected double[] doInBackground(String... url) {
            double[] scores = new double[5];
            IamAuthenticator authenticator = new IamAuthenticator("oJ_C6B2FGC3IJkD83cZPishvciqDyi0M-VAT4qc3NrmT");
            ToneAnalyzer toneAnalyzer = new ToneAnalyzer("2017-09-21", authenticator);
            toneAnalyzer.setServiceUrl("https://api.us-south.tone-analyzer.watson.cloud.ibm.com/instances/c6b291ad-a585-4dcf-9ddd-c75157a1b104");


            ToneOptions toneOptions = new ToneOptions.Builder()
                    .text(url[0])
                    .build();

            tone = toneAnalyzer.tone(toneOptions).execute().getResult();
            System.out.println(tone);
            String jsonText = tone.toString();

            JsonObject jsonObject = new JsonParser().parse(jsonText).getAsJsonObject();
            JsonArray arr = jsonObject.getAsJsonObject("document_tone").getAsJsonArray("tones");
            for(int i = 0; i < arr.size(); i++){
                double score = arr.get(i).getAsJsonObject().get("score").getAsDouble();
                String emotion = arr.get(i).getAsJsonObject().get("tone_id").getAsString();

                System.out.println(score);
                if(emotion.equals("sadness")){
                    scores[SADNESS] = score;
                }else if(emotion.equals("anger")){
                    scores[ANGER] = score;
                }else if(emotion.equals("fear")){
                    scores[FEAR] = score;
                }else if(emotion.equals("joy")){
                    scores[JOY] = score;
                }
            }

            authenticator = new IamAuthenticator("uVEjX85LaRA_5h9t7bOQ-g1v4HlM-zCTwCPCAsAHzvhG");
            VisualRecognition visualRecognition = new VisualRecognition("2018-03-19", authenticator);
            visualRecognition.setServiceUrl("https://api.us-south.visual-recognition.watson.cloud.ibm.com/instances/83c60631-284c-47f1-8c68-44ce46952134");

            try{
                if(mProfileImageUri != null){
                    File imgfile = new File(getRealPathFromUri(getContext(), mProfileImageUri));
                    if(imgfile.exists()) {
                        System.out.println("file exists.");
                    }

                    ClassifyOptions options = new ClassifyOptions.Builder()
                            .imagesFile(imgfile) // replace with path to file
                            .imagesFilename("profile.jpg")
                            .threshold((float) 0.001)
                            .classifierIds(Arrays.asList("EchoV2_1201066994"))
                            .build();
                    ClassifiedImages result = visualRecognition.classify(options).execute().getResult();
                    System.out.println(result);

                    String jsonImage = result.toString();
                    JsonObject jsonImageObject = new JsonParser().parse(jsonImage).getAsJsonObject();
                    JsonArray classImage = jsonImageObject.getAsJsonArray("images");

                    String classifier = classImage.get(0).toString();
                    JsonObject classifierObj = new JsonParser().parse(classifier).getAsJsonObject();
                    JsonArray classifierArr = classifierObj.getAsJsonArray("classifiers");

                    String classes = classifierArr.get(0).toString();
                    JsonObject classesObj = new JsonParser().parse(classes).getAsJsonObject();
                    JsonArray classesArr = classesObj.getAsJsonArray("classes");

                    for(int i = 0; i < classesArr.size(); i++){
                        double score = classesArr.get(i).getAsJsonObject().get("score").getAsDouble();
                        String emotion = classesArr.get(i).getAsJsonObject().get("class").getAsString();


                        if(emotion.equals("sadness")){
                            scores[SADNESS] += score;
                        }else if(emotion.equals("anger")){
                            scores[ANGER] += score;
                        }else if(emotion.equals("fear")){
                            scores[FEAR] += score;
                        }else if(emotion.equals("joy")){
                            scores[JOY] += score;
                        }else if(emotion.equals("neutrual")){
                            scores[NEUTRAL] += score;
                        }
                    }
                }

            }
            catch(FileNotFoundException ex) {
                System.out.println("File not found");
            }catch (Exception ex){
                System.out.println("Error!");
            }

            return scores;

        }

        protected void onPostExecute(double[] result) {
            for (int i = 0; i < result.length; i++) {
                System.out.println(i + " " + result[i]);
            }
            int maxIndex = 0;
            double maxScore = 0;
            for (int i = 0; i < 5; i++) {
                if (result[i] > maxScore) {
                    maxScore = result[i];
                    maxIndex = i;
                }
            }

            Intent intent = new Intent(getActivity(), ResponseListActivity.class);
            Response response = new Response(maxIndex);
            ResponseFactory.get(getActivity()).addResponse(response);
            startActivity(intent);
            getActivity().finish();
        }
    }

    private void saveProfileImage() {
        mProfileImageView.buildDrawingCache();
        Bitmap bmap = mProfileImageView.getDrawingCache();
        try {
            FileOutputStream fosProfile = getActivity().openFileOutput(getString(R.string.profile_image_file_name),
                    MODE_PRIVATE);
            bmap.compress(Bitmap.CompressFormat.PNG, 100, fosProfile);
            fosProfile.flush();
            fosProfile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startCameraActivity() {
        mStartCameraActivityWaiting = false;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues vals = new ContentValues(1);
        vals.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
        mProfileImageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, vals);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mProfileImageUri);
        intent.putExtra("return-data", true);

        try {
            startActivityForResult(intent, REQUEST_CODE_CAMERA_CAPTURE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void checkPermissions() {
        mUseCameraEnabled = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
        mUseStorageEnabled = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;

        if (!mUseCameraEnabled && !mUseStorageEnabled) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_USE_CAMERA_AND_EXTERNAL_STORAGE);
        } else if (!mUseCameraEnabled) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_USE_CAMERA);
        } else if (!mUseStorageEnabled) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_USE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_USE_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mUseCameraEnabled = true;
                } else {
                    mUseCameraEnabled = false;
                }
            }

            case REQUEST_USE_CAMERA_AND_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mUseCameraEnabled = true;
                } else {
                    mUseCameraEnabled = false;
                }

                if (grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    mUseStorageEnabled = true;
                } else {
                    mUseStorageEnabled = false;
                }
            }

            case REQUEST_USE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mUseStorageEnabled = true;
                } else {
                    mUseStorageEnabled = false;
                }
            }
        }

        if (mStartCameraActivityWaiting && mUseStorageEnabled && mUseCameraEnabled) {
            startCameraActivity();
        } else {
            mStartCameraActivityWaiting = false;
        }
    }
}
