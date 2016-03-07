package org.softeg.morphinebrowser;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Badgeable;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import org.softeg.morphinebrowser.controls.AppWebView;


public class MainActivity extends ActionBarActivity {
    private Drawer.Result drawerResult = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // Инициализирую Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Добавляю Navigation Drawer (Простите за мой французский)
        new Drawer()
                .withActivity(this)
                .withToolbar(toolbar) //отображение тулбара
                .withActionBarDrawerToggle(true) // значок гамбургера (если true, то при нажатии на значок, будет открываться дровер)
                .withHeader(R.layout.drawer_header) //хидэр Navigation Drawer
                        //добавляю пункты в дровер
                .addDrawerItems(
                        //начало списка
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withBadge("999+").withIdentifier(1), //бэйдж
                        new PrimaryDrawerItem().withName(R.string.drawer_item_custom).withIcon(FontAwesome.Icon.faw_eye).withBadge("6").withIdentifier(2),
                        new SectionDrawerItem().withName(R.string.drawer_item_more), //разделитель с заголовком
                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_question).setEnabled(false), //состояние пункта (кликабелность)
                        new DividerDrawerItem(),  //простой разделитель
                        new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_github)
                )
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        //скрываю клавиатуру при открытии дровера (на будущее)
                        InputMethodManager inputMethodManager = (InputMethodManager) MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);

                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        //действия при закрытии дровера
                    }
                })
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    // Обработка клика
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {
                            //при нажатии на элемент дровера, вывожу тост с его названием
                            Toast.makeText(MainActivity.this, MainActivity.this.getString(((Nameable) drawerItem).getNameRes()), Toast.LENGTH_SHORT).show();
                        }
                        if (drawerItem instanceof Badgeable) {
                            Badgeable badgeable = (Badgeable) drawerItem;
                            if (badgeable.getBadge() != null) {
                                //вообще-то, это нехорошо, ну да ладно...
                                try {
                                    int badge = Integer.valueOf(badgeable.getBadge());
                                    if (badge > 0) {
                                        drawerResult.updateBadge(String.valueOf(badge - 1), position);
                                    }
                                } catch (Exception e) {
                                    Log.e("MorphLog", "Нахрена ты это сделал? Зачем нажал на плюс?");
                                }
                            }
                        }
                    }
                })
                .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
                    @Override
                    // Обрабатываю длинный клик, (SecondaryDrawerItem)
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem instanceof SecondaryDrawerItem) {
                            Toast.makeText(MainActivity.this, MainActivity.this.getString(((SecondaryDrawerItem) drawerItem).getNameRes()), Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                })
                .build();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportProgressBarIndeterminateVisibility(true);
        createFragment(getIntent() != null ? getIntent().getData() : null);
    }

    private void createFragment(Uri uri) {
        Fragment newFragment = getSupportFragmentManager().findFragmentById(R.id.topic_fragment_container);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (newFragment == null) {
            newFragment = WebFragment.getInstance(uri);
        }

        transaction.replace(R.id.topic_fragment_container, newFragment, WebFragment.ID);

        transaction.commit();
    }

    @Override
    public void onBackPressed() {
       /* if(drawerResult.isDrawerOpen()){
            drawerResult.closeDrawer();
        }*/
        AppWebView webView = ((WebFragment)getSupportFragmentManager().findFragmentById(R.id.topic_fragment_container)).getWebView();
        if (webView!=null && webView.canGoBack()) {
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }
}
