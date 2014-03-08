package com.ryanq.dhs;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
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
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ArticleActivity extends Activity {
	String header;
	String message;
	ArrayList<String> links = new ArrayList<String>();
	ArrayList<Drawable> drawables = new ArrayList<Drawable>();
	String link;
	int i = 0;

	TextView article;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		header = intent.getStringExtra("title");
		message = intent.getStringExtra("article");
		setContentView(R.layout.activity_article);
		article = (TextView) findViewById(R.id.article);
		for (int l = 0; l < intent.getIntExtra("amountOfLinks", -1); l++) {

			links.add(intent.getStringExtra(String.valueOf(l)));
		}
		Log.d("channel1", String.valueOf(links.size()));

		fillTitle();

		try {
			new DownloadFilesTask(this, article).execute(new URL(
					"http://www.random.net/"));

		} catch (Exception e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.

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

				drawFromPath.setBounds(0, 0,
						drawFromPath.getIntrinsicWidth() * 6,
						drawFromPath.getIntrinsicHeight() * 6);
				Log.d("channel1", "drawing");

				return drawFromPath;
			}
		}, null));
		// article.setText(Html.fromHtml(message));

	}

	private class DownloadFilesTask extends AsyncTask<URL, Bitmap, Long> {

		Context context;
		TextView text;
		ProgressBar progress;

		public DownloadFilesTask(Context context, TextView text) {
			this.context = context;
			this.text = text;

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progress = (ProgressBar) findViewById(R.id.progressBar2);
			progress.setIndeterminate(true);

			// text = (TextView) findViewById(R.id.article);
			// get the URL's to

		}

		@Override
		protected Long doInBackground(URL... urls) {
			Bitmap bmp;
			try {
				if (links.size() > 0) {
					for (String linkx : links) {

						bmp = getBitmap(linkx);
						publishProgress(bmp);
						Log.d("channel1", String.valueOf(bmp.getHeight()));

					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
			//
		}

		@Override
		protected void onProgressUpdate(Bitmap... data) {
			Drawable draw = new BitmapDrawable(getResources(), data[0]);
			drawables.add(draw);

		}

		@Override
		protected void onPostExecute(Long result) {
			if (i < drawables.size()) {
				Log.d("channel1", "rawrawerawer");
				article.setText(Html.fromHtml(message, new ImageGetter() {
					@Override
					public Drawable getDrawable(String source) {
						Drawable drawFromPath = drawables.get(i);

						drawFromPath.setBounds(0, 0,
								drawFromPath.getIntrinsicWidth(),
								drawFromPath.getIntrinsicHeight());

						i++;

						return drawFromPath;
					}
				}, null));
			}
			progress.setVisibility(View.GONE);
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
			Bitmap bmp = BitmapFactory.decodeStream(input);
			Bitmap bitmap = Bitmap.createScaledBitmap(bmp, bmp.getWidth() * 4,
					bmp.getHeight() * 4, false);

			return bitmap;

		} catch (IOException e) {

			return null;
		}
	}

}
