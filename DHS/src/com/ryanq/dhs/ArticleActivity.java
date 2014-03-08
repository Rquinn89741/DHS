package com.ryanq.dhs;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class ArticleActivity extends Activity {
	String header;
	String message;
	ArrayList<String> links;
	ArrayList<Drawable> drawables = new ArrayList<Drawable>();
	int i = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		header = intent.getStringExtra("title");
		message = intent.getStringExtra("article");
		links = intent.getStringArrayListExtra("links");
		setContentView(R.layout.activity_article);
		fillTitle();
		fillArticle();
		try {
			new DownloadFilesTask().execute(new URL("http://www.random.net/"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.article, menu);

		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setDisplayShowTitleEnabled(false);

		return true;
	}

	public void fillTitle() {

		TextView title = (TextView) findViewById(R.id.title);

		title.setText(header);

	}

	public void fillArticle() {

		TextView article = (TextView) findViewById(R.id.article);

		Log.d("msg", message);
		article.setText(Html.fromHtml(message, new ImageGetter() {
			@Override
			public Drawable getDrawable(String source) {
				Drawable drawFromPath = getResources().getDrawable(
						R.drawable.test);

				drawFromPath.setBounds(0, 0, drawFromPath.getIntrinsicWidth(),
						drawFromPath.getIntrinsicHeight());
				// if (i < links.size())
				// i++;
				return drawFromPath;
			}
		}, null));
		// article.setText(Html.fromHtml(message));

	}

	private class DownloadFilesTask extends AsyncTask<URL, Bitmap, Long> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// get the URL's to

		}

		@Override
		protected Long doInBackground(URL... urls) {
			/*
			 * if (links.size() > 0) { for (String link : links) {
			 * publishProgress(getBitmap(link));
			 * 
			 * } }
			 */
			return null;

		}

		@Override
		protected void onProgressUpdate(Bitmap... data) {

			Drawable draw = new BitmapDrawable(getResources(), data[0]);
			drawables.add(draw);
			;
		}

		@Override
		protected void onPostExecute(Long result) {

		}

	}

	public static Bitmap getBitmap(String link) {
		/*--- this method downloads an Image from the given URL, 
		 *  then decodes and returns a Bitmap object
		 ---*/

		try {
			URL url = new URL(link);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(input);

			return bitmap;

		} catch (IOException e) {

			return null;
		}
	}

}
