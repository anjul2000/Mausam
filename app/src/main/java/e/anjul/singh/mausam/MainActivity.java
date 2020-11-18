package e.anjul.singh.mausam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText cityEditor;
    TextView tem, pressure, humidity, visibility, mint, maxt;
    JSONObject root, main;
    int visi,temp, pres, hum, mintt, maxtt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews()
    {
        if(tem==null)
        {
            tem=findViewById(R.id.temp);
        }

        if(humidity==null)
        {
            humidity=findViewById(R.id.Humidity);
        }
        if(visibility==null)
        {
            visibility=findViewById(R.id.Visibility);
        }
        if(pressure==null)
        {
            pressure=findViewById(R.id.pressure);
        }
        if(mint==null)
        {
            mint=findViewById(R.id.mint);
        }
        if(maxt==null)
        {
            maxt=findViewById(R.id.maxt);
        }
        if(cityEditor == null)
        {
            cityEditor =findViewById(R.id.cityeditor);
        }

        findViewById(R.id.hit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cityEditor.getText().toString().equals("")) {
                    new jsonTask().execute("P");
                }else {
                    Toast.makeText(MainActivity.this, "Please enter a city", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public  void jsonFun(String s)
    {
        try {
            root=new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            main=root.getJSONObject("main");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            temp=main.getInt("temp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        temp=temp-273;
        String sz=Integer.toString(temp);
        tem.setText(sz+ "°C");

        try {
            pres=main.getInt("pressure");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String pp=Integer.toString(pres);
        pp="Pressure-"+pp+"mbar";
        pressure.setText(pp);
        try {
            hum=main.getInt("humidity");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String hmm=Integer.toString(hum);
        hmm="Humidity-"+hmm+"%";
        humidity.setText(hmm);
        try {
            mintt=main.getInt("temp_min");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mintt=mintt-273;
        String mnn=Integer.toString(mintt);
        mnn="Min Temp-"+mnn+"°C";
        mint.setText(mnn);
        try {
            maxtt=main.getInt("temp_max");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        maxtt=maxtt-273;
        String mxx=Integer.toString(maxtt);
        mxx="Max Temp-"+mxx+"°C";
        maxt.setText(mxx);
        try {
            visi=root.getInt("visibility");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        visi=visi/1000;
        visibility.setText("Visibility-"+visi+"Km");
    }


    class jsonTask extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... strings)
        {
            HttpURLConnection httpConn = null;
            InputStreamReader isReader = null;
            BufferedReader bufReader = null;
            StringBuffer readTextBuf = new StringBuffer();
            String urll=cityEditor.getText().toString();
            urll="https://api.openweathermap.org/data/2.5/weather?q="+urll+"&appid=cda4f181a1adecf2dee0db6e9e6e54e7";
            try
            {

                URL url=new URL(urll);
                httpConn = (HttpURLConnection)url.openConnection();
                httpConn.setRequestMethod("GET");
                httpConn.setConnectTimeout(10000);
                httpConn.setReadTimeout(10000);
                InputStream inputStream = httpConn.getInputStream();
                isReader = new InputStreamReader(inputStream);
                bufReader = new BufferedReader(isReader);
                String line = bufReader.readLine();
                while(line != null)
                {
                    readTextBuf.append(line);
                    line = bufReader.readLine();
                }
                return (readTextBuf.toString());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally {
                try {
                    if (bufReader != null) {
                        bufReader.close();
                    }

                    if (isReader != null) {
                        isReader.close();
                    }

                    if (httpConn != null) {
                        httpConn.disconnect();
                    }
                }catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String st)
        {
            super.onPostExecute(st);
            jsonFun(st);
        }
    }
}