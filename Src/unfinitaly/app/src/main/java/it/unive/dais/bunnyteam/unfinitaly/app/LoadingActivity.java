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

import it.unive.dais.bunnyteam.unfinitaly.app.view.CustomIntroFragment;

/**
 *
 * @author BunnyTeam, Università Ca' Foscari
 */
public class LoadingActivity extends AppIntro {
    private WebView webview;
    private TextView tv_status,tvCountLoad;
    private ProgressBar progressBar;
    Fragment curFragment;
    private View v;
    int status = 0;
    private FloatingActionButton fab;
    private View loadingView;
    private boolean ready = false;
    private boolean readyLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ACTIVITY","LOADING");
        if (getIntent().getBooleanExtra("EXIT", false)) {
            this.finishAffinity();
            finishAffinity();
            System.exit(0);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        resumeLoadingAfterFirebase();
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d("PAUSA","AA");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("RESUME","BB");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d("STOP","CC");
    }

    public void setReadyLoading (boolean value){
        readyLoading = value;
    }

    public void resumeLoadingAfterFirebase(){
        //Check se l'applicazione è stata usata una volta, leggo le shared preferences
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("first",MODE_PRIVATE);
        String first = sharedPreferences.getString("text",null);
        if(first != null){
            //Non era il primo avvio
            Log.d("AVVIO","NON IL PRIMO");
            startMapsActivity();
        }
        else{
            //E' il primo avvio, scrivo qualcosa nelle sharedpreferences
            Log.d("AVVIO","IL PRIMO");
            SharedPreferences.Editor editor = getSharedPreferences("first",MODE_PRIVATE).edit();
            editor.putString("text","avviata");
            editor.commit();
            //Setto le variabili di default dei pin
            editor = getSharedPreferences("flags",MODE_PRIVATE).edit();
            editor.putString("distribuzione","true");
            editor.putString("percentualeRegione","false");
            editor.putString("percentualePin","false");
            editor.commit();
            //Continuo col caricamento di tutto il resto
            addSlide(CustomIntroFragment.newInstance(R.layout.fragmentinfo1));
            ready = true;
            ((TextView)findViewById(com.github.paolorotolo.appintro.R.id.done)).setText("CONTINUA");
            addSlide(CustomIntroFragment.newInstance(R.layout.fragmentinfo2));
            addSlide(CustomIntroFragment.newInstance(R.layout.fragmentinfo3));
            addSlide(CustomIntroFragment.newInstance(R.layout.fragmentinfo4));
            addSlide(CustomIntroFragment.newInstance(R.layout.fragmentinfo5));
            setBarColor(Color.parseColor("#66000000"));
            setSeparatorColor(Color.parseColor("#66000000"));
            curFragment = fragments.get(0);
            setProgressButtonEnabled(true);
            showSkipButton(false);
            setFadeAnimation();
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        startMapsActivity();
    }

    /**
     * Da cancellare
     */
    @Deprecated
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


    @Deprecated
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

