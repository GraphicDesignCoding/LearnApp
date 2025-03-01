package com.graphicdesigncoding.learnapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.appbar.AppBarLayout;
import java.util.Objects;

import com.graphicdesigncoding.learnapp.databinding.ActivityMainBinding;



//COPYRIGHT BY GraphicDesignCoding
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private LruCache<String, Bitmap> memoryCache;
    ////////DEBUG///////////////////////////////////////DEBUG/////////////////////DEBUG//////////
    private boolean debugMode = true;
    /////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.graphicdesigncoding.learnapp.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater()); //Sets the binding Layout
        setContentView(binding.getRoot()); // Set the Layout Root

        setSupportActionBar(binding.toolbar); // Set Toolbar

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main); // Get Nav Controller
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build(); // Build the App Bar
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration); // Build the Navigation Controller
        binding.imageButtonBack.setOnClickListener((btn_view)-> onBackPressed());

        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    public void Debug(String trigger, Object obj){
        /////////////////////////////////Activate Debug///////////

        if(debugMode) {
            System.out.println("Trigger: " + trigger + " Message: " + obj.toString());
        }
    }

    public boolean isBitmapInMemoryCache(String key) {
        return (getBitmapFromMemCache(key) != null);
    }

    public Bitmap getBitmapFromMemCache(String key) {

        return memoryCache.get(key);
    }

    public void removeBitmapFromMemCache(String key){
        if (isBitmapInMemoryCache(key)){
            memoryCache.remove(key);
        }
    }
    public void SetControlVisibility(View _view,int _id,boolean _bool){

        if (_bool){

            _view.findViewById(_id).setVisibility(View.VISIBLE);
            _view.findViewById(_id).setEnabled(true);
        }else{

            _view.findViewById(_id).setVisibility(View.INVISIBLE);
            _view.findViewById(_id).setEnabled(false);
        }
    }
    public SharedPreferences GetSharedPrefs(String name){
        SharedPreferences sharedPref;
        sharedPref = getSharedPreferences(name,MODE_PRIVATE);
        return sharedPref;
    }

    public void showExtendedBar(boolean isVisible,String title,boolean nav_backwards_allowed){

        AppBarLayout abl = this.findViewById(R.id.appBarLayout);
        ((TextView)abl.findViewById(R.id.textView_subtitle)).setText(title);

        if(isVisible){
            abl.setVisibility(View.VISIBLE);
        }else{
            abl.setVisibility(View.INVISIBLE);
            abl.setVisibility(View.GONE);
        }
        ImageButton ibb = this.findViewById(R.id.imageButton_back);
        if(nav_backwards_allowed){
            ibb.setEnabled(true);
            ibb.setVisibility(View.VISIBLE);
        }else{
            ibb.setVisibility(View.INVISIBLE);
            ibb.setEnabled(false);
        }
    }

    public Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        assert drawable != null;
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profil) {
            Navigation.findNavController(this, R.id.StartUpForm);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        int navId = Objects.requireNonNull(Navigation.findNavController(this, R.id.nav_host_fragment_content_main).getCurrentDestination()).getId();
        if(navId == R.id.RegisterForm || navId == R.id.ProfileForm || navId == R.id.ScoreboardForm || navId == R.id.QuizForm || navId == R.id.RecoverForm){
            super.onBackPressed();
        }else if(navId == R.id.StartUpForm || navId == R.id.MainMenuForm || navId == R.id.LoginForm){
            this.finish();
        }
    }

}