package com.example.currencyconverter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Text;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	TextView mcurrency;
	Spinner mfromCountry,mtoCountry;
	EditText minputText;
	Button mconverterBtn;
	private String selectedFromCountry = null;
	private String selectedToCountry = null;
	String toCountry,fromCountry;
	 private ProgressDialog dialog;
//	ArrayAdapter<String> adapter;
	// An array containing list of Country Names
	String[] countries = new String[] {

			"AED","ANG","ARS","AUD","BDT","BGN","BHD","BND","BOB","BRL","BWP",
			"CAD","CHF","CLP","CNY","COP","CRC","CZK","DKK","DOP","DZD",
			"EEK","EGP","EUR","FJD","GBP","HKD","HNL","HRK","HUF",
			"IDR","ILS","INR","JMD","JOD","JPY","KES","KRW","KWD","KYD","KZT",
			"LBP","LKR","LTL","LVL","MAD","MDL","MKD","MUR","MVR","MXN","MYR",
			"NAD","NGN","NIO","NOK","NPR","NZD",
			"OMR","PEN","PGK","PHP","PKR","PLN","PYG","QAR","RON","RSD","RUB",
			"SAR","SCR","SEK","SGD","SKK","SLL","SVC","THB","TND","TRY","TTD",
			"TWD","TZS","UAH","UGX","USD","UYU","UZS","VEF","VND","XOF",
			"YER","ZAR","ZMK"
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Getting a reference to Spinner Widget containing list of countries
		mfromCountry = (Spinner) findViewById(R.id.fromCountry);
		mtoCountry=(Spinner)findViewById(R.id.toCountry);

		// country from the spinner widget
		mcurrency = (TextView) findViewById(R.id.resultSet);
		minputText =(EditText)findViewById(R.id.editText);
		mconverterBtn=(Button)findViewById(R.id.converter_btn);

		mfromCountry.setOnItemSelectedListener(new MyOnItemSelectedListener() );
		mtoCountry.setOnItemSelectedListener(new MyOnItemSelectedListener() );

		mconverterBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				try {
					 dialog = ProgressDialog.show(MainActivity.this, "","Loading..Wait.." , true);
					 dialog.show();
					coverterCurrencyFunction();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	public void coverterCurrencyFunction() throws JSONException {
		String convertedValue = null;
		String inputText= minputText.getText().toString();
		System.out.println("Clicked....!!!"+inputText);
		String finalResult=readJSONFeed("http://www.google.com/ig/calculator?hl=en&q="+inputText+""+fromCountry+"%3D%3F"+toCountry);
		System.out.println("Final result::"+finalResult);
		try {
			JSONObject json= (JSONObject) new JSONTokener(finalResult).nextValue();
			convertedValue = (String) json.get("rhs");
		} catch(Exception ex){
			//don't forget this
		}
		dialog.dismiss();
		System.out.println("converted value:"+convertedValue);
		mcurrency.setText(convertedValue);
	}


	public class MyOnItemSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

			String selectedItem = parent.getItemAtPosition(pos).toString();
			int index=parent.getSelectedItemPosition();
			String selectedCountry=countries[index];
			System.out.println("the clicked code is::::"+selectedCountry);
			//check which spinner triggered the listener
			switch (parent.getId()) {
			//country spinner
			case R.id.fromCountry:
				//make sure the country was already selected during the onCreate
				if(selectedFromCountry != null){
					Toast.makeText(parent.getContext(), "FromCountry you selected is " + selectedItem,
							Toast.LENGTH_LONG).show();
					fromCountry=selectedCountry;
				}
				selectedFromCountry = selectedItem;
				break;
				//animal spinner  
			case R.id.toCountry:
				//make sure the animal was already selected during the onCreate
				if(selectedToCountry != null){
					Toast.makeText(parent.getContext(), "ToCountry you selected is" + selectedItem,
							Toast.LENGTH_LONG).show();
					toCountry=selectedCountry;
				}  
				selectedToCountry = selectedItem;
				break;
			}


		}

		public void onNothingSelected(AdapterView<?> parent) {
			// Do nothing.
		}
	}
	public String readJSONFeed(String URL) {
		
		StringBuilder stringBuilder = new StringBuilder();
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(URL);
		try {
			HttpResponse response = httpClient.execute(httpGet);

			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream inputStream = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream));
				String line;
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
				}
				inputStream.close();
			} else {
				Log.d("JSON", "Failed to download file");
			}
		} catch (Exception e) {
			Log.d("readJSONFeed", e.getLocalizedMessage());
		}        
		return stringBuilder.toString();
	}
}

//http://www.google.com/finance/converter?a=10&from=SAR&to=INR&meta=ei%3Dl0sjUZDFA4OikAXFtgE