package it.unive.dais.bunnyteam.unfinitaly.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.paolorotolo.appintro.AppIntro;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.unive.dais.bunnyteam.unfinitaly.app.entities.User;
import it.unive.dais.bunnyteam.unfinitaly.app.slider.CustomSlider;
import it.unive.dais.bunnyteam.unfinitaly.app.slider.CustomSliderLoading;
import it.unive.dais.bunnyteam.unfinitaly.app.storage.FirebaseUtilities;

/**
 *
 * @author BunnyTeam, Università Ca' Foscari
 */
public class LoadingActivity extends AppIntro {
    private WebView webview;
    private TextView tv_status;
    private TextView tvCountLoad;
    private ProgressBar progressBar;
    CustomSliderLoading csl;
    Fragment curFragment;
    private View v;
    int status = 0;
    private FloatingActionButton fab;
    private View loadingView;
    private boolean ready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            System.exit(0);
        }
        Log.d("LETTURA DATI","STO PER LEGGERE I DATI DA FIREBASE");
        FirebaseUtilities.getIstance().readFromFirebase(LoadingActivity.this);
        //TODO: togliere sto sleep che fa cagare
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resumeLoadingAfterFirebase(){
        //Check se l'applicazione è stata usata una volta, leggo le shared preferences
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("first",MODE_PRIVATE);
        String first = sharedPreferences.getString("text",null);
        if(first != null){
            //Non era il primo avvio
            if(FirebaseUtilities.getIstance().isLogged()){
                User.getIstance().setName(FirebaseUtilities.getIstance().getNome());
                User.getIstance().setEmail(FirebaseUtilities.getIstance().getEmail());
            }
            startMapsActivity();
        }
        else{
            //E' il primo avvio, scrivo qualcosa nelle sharedpreferences
            SharedPreferences.Editor editor = getSharedPreferences("first",MODE_PRIVATE).edit();
            editor.putString("text","avviata");
            editor.commit();
            //Continuo col caricamento di tutto il resto
            csl = CustomSliderLoading.newInstance(R.layout.fragmentinfo1, this);
            addSlide(csl);
            ready = true;
            ((TextView)findViewById(com.github.paolorotolo.appintro.R.id.done)).setText(R.string.msg_ok);
            addSlide(CustomSlider.newInstance(R.layout.fragmentinfo2));
            addSlide(CustomSlider.newInstance(R.layout.fragmentinfo3));
            addSlide(CustomSlider.newInstance(R.layout.fragmentinfo4));
            addSlide(CustomSlider.newInstance(R.layout.fragmentinfo5));
            setBarColor(Color.parseColor("#66000000"));
            setSeparatorColor(Color.parseColor("#66000000"));
            curFragment = fragments.get(0);
            setProgressButtonEnabled(true);
            showSkipButton(false);
            setFadeAnimation();
        }
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public WebView getWebview(){
        return webview;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        startMapsActivity();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        if(isReady())
            startLoginActivity();
        else {
            Snackbar snack = Snackbar.make(currentFragment.getView(), R.string.loading_snackbarnotready, Snackbar.LENGTH_SHORT);
            View view = snack.getView();
            view.setBackgroundColor(getResources().getColor(R.color.md_red_900));
            snack.show();
        }
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        curFragment = newFragment;
    }

    public void showSkip(){
        showSkipButton(true);
    }


    public void showFinishSnackbar() {
        ((TextView)findViewById(com.github.paolorotolo.appintro.R.id.done)).setText(R.string.loading_snackbarcontinue);
        if(curFragment.getView() != null){
            Snackbar snack = Snackbar.make(curFragment.getView(), R.string.loading_snackbarready, Snackbar.LENGTH_SHORT);
            View view = snack.getView();
            view.setBackgroundColor(getResources().getColor(R.color.md_green_700));
            snack.show();
            snack.setActionTextColor(getResources().getColor(R.color.md_white_1000));
            snack.setAction(R.string.loading_snackbarcontinue, new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    startMapsActivity();
                }
            });
        }
    }

    public void startMapsActivity() {
        startActivity(new Intent(this, MapsActivity.class));
    }

    private void startLoginActivity(){
        Intent i = new Intent(this,LoginActivity.class);
        i.putExtra("Activity","Loading");
        startActivity(i);
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}

