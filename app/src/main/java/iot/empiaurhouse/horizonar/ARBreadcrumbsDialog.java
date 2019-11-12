package iot.empiaurhouse.horizonar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class ARBreadcrumbsDialog extends DialogFragment {


    public static final String TAG = "ARBreadcrumbs Dialog";
    private static int RESULT_AUDIO_PICK = 1;

    private Toolbar crumbsToolbar;
    private CardView seeCrumbsEditor;
    private CardView listenCrumbsEditor;
    private CustomToggle customToggle;
    private TextView fileBrowser;
    private Button updateMaximButton;
    private EditText seeMaxim;
    private EditText listenMaxim;
    private EditText seeContext;
    private int breadcrumbMode;
    SharedPreferences horizonPreferences;
    SharedPreferences.Editor horizonAR_IO;




    public static ARBreadcrumbsDialog display(FragmentManager fragmentManager) {
        ARBreadcrumbsDialog arBreadcrumbsDialog = new ARBreadcrumbsDialog();
        arBreadcrumbsDialog.show(fragmentManager, TAG);
        return arBreadcrumbsDialog;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
        horizonPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("HORIZON_PREFERENCES",0);

        //breadcrumbMode = 0;

    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.Slide);

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.breadcrumbs_dialog, container, false);

        crumbsToolbar = view.findViewById(R.id.toolbar);
        seeCrumbsEditor = view.findViewById(R.id.cardviewSeeCrumbEditor);
        listenCrumbsEditor = view.findViewById(R.id.cardviewListenCrumbEditor);
        customToggle = view.findViewById(R.id.crumbModeToggle);
        fileBrowser = view.findViewById(R.id.fileBrowseButton);
        updateMaximButton = view.findViewById(R.id.maximUpdateButton);
        seeMaxim = view.findViewById(R.id.breadcrumbSeeEditorMaxim);
        seeContext = view.findViewById(R.id.breadcrumbSeeEditorContext);
        listenMaxim = view.findViewById(R.id.breadcrumbListenEditorMaxim);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        crumbsToolbar.setNavigationOnClickListener(v -> dismiss());
        crumbsToolbar.setTitle(R.string.crumbs_editor);
        crumbsToolbar.setOnMenuItemClickListener(item -> {
            dismiss();
            return true;
        });


        this.breadcrumbEditorEventHandler();

        customToggle.setOnToggleClickListener(new CustomToggle.OnToggleClickListener() {
            @Override
            public void onLefToggleEnabled(boolean enabled) {

                breadcrumbMode = 0;
                viewDirector(breadcrumbMode);

            }

            @Override
            public void onRightToggleEnabled(boolean enabled) {

                breadcrumbMode = 1;
                viewDirector(breadcrumbMode);
                Toast.makeText(getContext(), "Listen mode is no longer supported by ARCore SDK", Toast.LENGTH_LONG).show();


            }
        });



    }





    private void viewDirector(int crumbMode){

        final Animation fadeInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fadein);
        final Animation fadeOutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fadeout);
        //final Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.push_up_in);

        if(crumbMode == 0) {

            seeCrumbsEditor.startAnimation(fadeInAnimation);
            listenCrumbsEditor.startAnimation(fadeOutAnimation);
            seeCrumbsEditor.setVisibility(View.VISIBLE);
            listenCrumbsEditor.setVisibility(View.GONE);


        }
        else if (crumbMode == 1){

            listenCrumbsEditor.startAnimation(fadeInAnimation);
            seeCrumbsEditor.startAnimation(fadeOutAnimation);
            listenCrumbsEditor.setVisibility(View.VISIBLE);
            seeCrumbsEditor.setVisibility(View.GONE);



        }


    }



    private void breadcrumbEditorEventHandler(){
        fileBrowser.setOnClickListener(view -> {

            selectSoundFile();

        });


        updateMaximButton.setOnClickListener(view -> {

            sendData(breadcrumbMode);
            dismiss();


        });



    }




    private void sendData(int crumbMode)
    {

        horizonAR_IO = horizonPreferences.edit();


        if(crumbMode == 0) {

            //INTENT OBJ
            Intent i = new Intent(Objects.requireNonNull(getActivity()).getBaseContext(),
                    ARPurviewActivity.class);

            //PACK DATA
            i.putExtra("SENDER_KEY", "CrumbEditor");
            i.putExtra("MODE_KEY", crumbMode);
            i.putExtra("MAXIM_KEY", seeMaxim.getText().toString());
            i.putExtra("CONTEXT_KEY", seeContext.getText().toString());
            horizonAR_IO.putInt("crumbMode", crumbMode);
            horizonAR_IO.putString("seeMaxim", seeMaxim.getText().toString());
            horizonAR_IO.putString("seeContext", seeContext.getText().toString());
            horizonAR_IO.apply();


            //STARTS ACTIVITY
            //getActivity().startActivity(i);

        }
        else if (crumbMode == 1){

            //INTENT OBJ
            Intent i = new Intent(Objects.requireNonNull(getActivity()).getBaseContext(),
                    ARPurviewActivity.class);

            //PACK DATA
            i.putExtra("SENDER_KEY", "CrumbEditor");
            i.putExtra("MODE_KEY", crumbMode);
            i.putExtra("MAXIM_KEY", listenMaxim.getText().toString());
            horizonAR_IO.putInt("crumbMode", 0);
            horizonAR_IO.putString("listenMaxim", listenMaxim.getText().toString());
            horizonAR_IO.apply();
            //i.putExtra("SOUND_KEY", seeContext.getText().toString());


            //STARTS ACTIVITY
            //getActivity().startActivity(i);


        }


    }



    private void selectSoundFile(){

        Intent intent;
        intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(Intent.createChooser(intent,"Select Audio "), RESULT_AUDIO_PICK);


    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_AUDIO_PICK && resultCode == Activity.RESULT_OK){
            if ((data != null) && (data.getData() != null)){
                Uri audioFileUri = data.getData();
                // Now you can use that Uri to get the file path, or upload it, ...
                //set sound file path as preference
                horizonAR_IO = horizonPreferences.edit();
                horizonAR_IO.putString("soundFile", null);
                horizonAR_IO.apply();


            }
        }
    }





}
