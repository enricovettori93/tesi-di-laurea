package it.unive.dais.bunnyteam.unfinitaly.app;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.mikepenz.materialdrawer.*;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;
import java.util.Collections;

public abstract class BaseActivity extends AppCompatActivity {
    protected Drawer drawer;
    protected Activity thisActivity;
    final ArrayList<Integer> selectedRegionsItems = new ArrayList<>();
    final ArrayList<Integer> selectedCategoriesItems = new ArrayList<>();
    boolean resetfilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * @author Giacomo
     */
    protected void buildDrawer(Toolbar toolbar) {
        thisActivity = this;
        resetfilter = false;
        setSupportActionBar(toolbar);
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withSelectionListEnabledForSingleProfile(false)
                .withHeaderBackground(R.drawable.background)
                .addProfiles(
                        new ProfileDrawerItem().withName("Bunny Team").withEmail("unfinitaly.app@gmail.com").withIcon(getResources().getDrawable(R.drawable.bunnylogo))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        startInfoActivity();
                        return false;
                    }
                })
                .build();
        //Creazione voci di menu
        PrimaryDrawerItem informazioni = new PrimaryDrawerItem().withIdentifier(1).withName("Informazioni").withIcon(R.drawable.info);
        PrimaryDrawerItem impostazioni = new PrimaryDrawerItem().withIdentifier(1).withName("Impostazioni").withIcon(R.drawable.settings);
        informazioni.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                startInfoActivity();
                return false;
            }
        });
        impostazioni.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                startSettingsActivity();
                return false;
            }
        });
        if (this instanceof MapsActivity) {
            PrimaryDrawerItem tutte = new PrimaryDrawerItem().withIdentifier(1).withName("Reset filtri").withIcon(R.drawable.regione);
            PrimaryDrawerItem regione = new PrimaryDrawerItem().withIdentifier(2).withName("Filtro per regione").withIcon(R.drawable.regione);
            PrimaryDrawerItem categoria = new PrimaryDrawerItem().withIdentifier(3).withName("Filtro per categoria").withIcon(R.drawable.categoria);
            //PrimaryDrawerItem percentuale = new PrimaryDrawerItem().withIdentifier(1).withName("Filtro per percentuale").withIcon(R.drawable.percentage);
            SwitchDrawerItem percentuale = new SwitchDrawerItem().withIdentifier(4).withName("Filtro per percentuale").withIcon(R.drawable.percentage);
            //Associazione listener alle varie voci
            tutte.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                    ((MapsActivity)thisActivity).getClusterManager().resetMarkers();
                    resetfilter = true;
                    return false;
                }
            });
            //Associazione listener alle varie voci
            regione.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                    /*TO DO: mostrare lista regioni, estrarre regioni selezionate*/
                    showAlertDialogRegions();
                    return false;
                }
            });
            categoria.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                    showAlertDialogCategory();
                    return false;
                }
            });
            percentuale.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Toast.makeText(getApplicationContext(), "Pulsante %", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
            percentuale.withOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        drawer.closeDrawer();
                        ((MapsActivity)thisActivity).getClusterManager().setPercentageRenderer();
                    }
                    else{
                        drawer.closeDrawer();
                        ((MapsActivity)thisActivity).getClusterManager().unsetPercentageRender();
                    }
                    //Toast.makeText(getApplicationContext(), "Pulsante %: "+isChecked, Toast.LENGTH_SHORT).show();
                }
            });
            drawer = new com.mikepenz.materialdrawer.DrawerBuilder()
                    .withActivity(this)
                    .withAccountHeader(headerResult)
                    .withToolbar(toolbar)
                    .addDrawerItems(
                           tutte, regione, categoria, percentuale, new DividerDrawerItem(), informazioni, impostazioni, new DividerDrawerItem()
                    )
                    .build();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
        } else {
            PrimaryDrawerItem mappa = new PrimaryDrawerItem().withIdentifier(1).withName("Mappa").withIcon(R.drawable.info);
            mappa.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                    startMapsActivity();
                    return false;
                }
            });
            drawer = new com.mikepenz.materialdrawer.DrawerBuilder()
                    .withActivity(this)
                    .withAccountHeader(headerResult)
                    .withToolbar(toolbar)
                    .addDrawerItems(
                            mappa, new DividerDrawerItem(), informazioni, impostazioni, new DividerDrawerItem()
                    )
                    .build();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
        }
    }


    public void startSettingsActivity() {
        Intent intent_info = new Intent(this, SettingsActivity.class);
        startActivity(intent_info);
        //overridePendingTransition(R.xml.slide_up_info, R.xml.no_change);
    }

    public void startInfoActivity() {
        if (!(this instanceof InfoActivity)) {
            Intent intent_info = new Intent(this, InfoActivity.class);
            startActivity(intent_info);
            //overridePendingTransition(R.xml.slide_up_info, R.xml.no_change);
        }
    }

    public void startMapsActivity() {
        if (!(this instanceof MapsActivity)) {
            Intent maps_info = new Intent(this, MapsActivity.class);
            startActivity(maps_info);
        }
    }
    private void showAlertDialogRegions() {
        if (thisActivity instanceof MapsActivity) {
            final String[] allRegions = getResources().getStringArray(R.array.regions);
            final String[] allRegionsWithNumbers = new String[allRegions.length];
            final ArrayList<String> selectedRegions = new ArrayList<>();
            for (int i = 0; i < allRegionsWithNumbers.length; i++)
                allRegionsWithNumbers[i] = allRegions[i] + " (" + ((MapsActivity) thisActivity).getClusterManager().countMarkerByRegion(allRegions[i]) + ")";
            final boolean[] selectedReg = new boolean[allRegions.length];
            if(resetfilter){
                for(int i : selectedRegionsItems)
                    selectedReg[i] = false;
                resetfilter = false;
            }else{
                for(int i : selectedRegionsItems)
                    selectedReg[i] = true;
            }
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Scegli le regioni")
                    .setMultiChoiceItems(allRegionsWithNumbers, selectedReg , new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                            if (isChecked) {
                                selectedRegionsItems.add(indexSelected);
                            } else if (selectedRegionsItems.contains(indexSelected)) {
                                selectedRegionsItems.remove(Integer.valueOf(indexSelected));
                            }
                        }
                    }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            /*cliccato OK, mi ricavo le regioni salvate.*/

                            for (int number : selectedRegionsItems)
                                selectedRegions.add(allRegions[number]);
                            ((MapsActivity) thisActivity).getClusterManager().showRegions(selectedRegions);

                        }
                    }).setNegativeButton("Indietro", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).create();
            dialog.show();
        }
    }
    private void showAlertDialogCategory(){
        if(thisActivity instanceof MapsActivity) {
            final ArrayList<String> allCategory = ((MapsActivity)thisActivity).getClusterManager().getAllMarkerCategory();
            Collections.sort(allCategory);
            final String[] allCategoryWithNumbers = new String[allCategory.size()];
            for(int i=0; i<allCategoryWithNumbers.length; i++)
                allCategoryWithNumbers[i] = allCategory.get(i)+" ("+((MapsActivity)thisActivity).getClusterManager().countMarkerByCategory(allCategory.get(i))+")";
            final ArrayList<String> selectedCategory = new ArrayList<>();
            final boolean[] selectedCat = new boolean[allCategory.size()];
            if (resetfilter){
                for(int i : selectedCategoriesItems)
                    selectedCat[i] = false;
                resetfilter = false;
            }else{
                for(int i : selectedCategoriesItems)
                    selectedCat[i] = true;
            }
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Scegli le categorie")
                    .setMultiChoiceItems(allCategoryWithNumbers, selectedCat, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                            if (isChecked) {
                                selectedCategoriesItems.add(indexSelected);
                            } else if (selectedCategoriesItems.contains(indexSelected)) {
                                selectedCategoriesItems.remove(Integer.valueOf(indexSelected));
                            }
                        }
                    }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            /*cliccato OK, mi ricavo le regioni salvate.*/
                            for (int number : selectedCategoriesItems)
                                selectedCategory.add(allCategory.get(number));
                            ((MapsActivity)thisActivity).getClusterManager().showCategory(selectedCategory);

                        }
                    }).setNegativeButton("Indietro", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).create();
            dialog.show();
        }
    }
}
