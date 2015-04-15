package ba.tba.ispocetka3;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.util.ArrayList;


public class FirstActivity extends Activity {

    LinearLayout layout;
    Button buyButton;
    TextView tv;
    Button bt;
    ListView lv;
    ArrayList<String> restaurants;
    RequestQueue queue;
    final  String TAG = FirstActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        //It's recomended you create Singleton class for queue
        queue = Volley.newRequestQueue(this);


         bt = (Button) findViewById(R.id.button);
         tv = (TextView) findViewById(R.id.textView);


        bt.setText("Change Text");
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("BUY button created and added to View");
                if (buyButton != null ) {
                    layout.addView(buyButton);}
            }
        });



          layout = (LinearLayout) findViewById(R.id.layout);
          buyButton = new Button(this);
          buyButton.setText("BUY");
          buyButton.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        //Create URL to get Yahoo developer network Rest JSON service
        String baseUrl = "http://query.yahooapis.com/v1/public/yql?q=";
        String query = "select * from local.search where query=\"sushi\" and location=\"san francisco, ca\" and Rating.AverageRating in (3,4,5)";
        String url = null;
        try {
            url = URLEncoder.encode(query, "UTF-8");
        }
        catch(Exception e){
        }
        String fullUrlStr = baseUrl + url + "&format=json";

        Log.i(TAG, "Before populateRestaurantList");


        restaurants = new ArrayList<String>();
        lv = (ListView) findViewById(R.id.listView);
        populateRestaurantList(fullUrlStr);
        //populateRestaurantList();
    }

    void populateRestaurantList(){
    //Populate restaurants List

    restaurants.add("Lovac");
    restaurants.add("Tisina");
    restaurants.add("Bocone");
    restaurants.add("Mala Kuhinja");
    restaurants.add("Zacin");
    lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, restaurants));
    }


   //= ba.tba.ispocetka3.MySingleton.getInstance(this).getRequestQueue();


    //Implementation with JsonArrayRequest
    protected void populateRestaurantList(String url){
        Log.d("Response", "Prepare Request");

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONObject("query").getJSONObject("results").getJSONArray("Result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                Log.i("JSON RESPONSE", jo.getString("Title") + " " + jo.getString("Phone"));
                                restaurants.add( jo.getString("Title") + " " + jo.getString("Phone"));
                            }
                            lv.setAdapter(new ArrayAdapter<String>(FirstActivity.this,android.R.layout.simple_list_item_1, restaurants));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error.Response" + error.toString());
                    }
                }
        );
        queue.add(getRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.first, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
