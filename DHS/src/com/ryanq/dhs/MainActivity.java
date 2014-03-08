package com.ryanq.dhs;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Intent article;
	private ListView listView;
	private final ArrayList<String> titles = new ArrayList<String>();
	private final ArrayList<String> articles = new ArrayList<String>();
	private final ArrayList<ArrayList<String>> links = new ArrayList<ArrayList<String>>();
	private ArrayAdapter adapter;
	private final ArrayList<String> watwat = new ArrayList<String>();
	boolean loaded = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		try {

			new DownloadFilesTask().execute(new URL("http://random.net"));

		} catch (Exception e) {
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (ListView) findViewById(R.id.listview);
		article = new Intent(this, ArticleActivity.class);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, titles);

		;

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(itemClickedHandler);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;

	}

	private final OnItemClickListener itemClickedHandler = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView parent, View v, int position,
				long id) {
			article.putExtra("title", titles.get(position));
			article.putExtra("article", articles.get(position));
			// if (links.get(position) != null)
			// article.putExtra("imageLinks", links.get(position));

			startActivity(article);
		}
	};

	public void displayMessage(String message) {

		CharSequence text = message;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(this, text, duration);
		toast.show();

	}

	private class DownloadFilesTask extends AsyncTask<URL, String, Long> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// get the URL's to

		}

		@Override
		protected Long doInBackground(URL... urls) {

			Log.d("procedure", "Start of download process");
			try {

				Document newsPage = Jsoup.connect(
						"http://www.stcharles.k12.la.us/news.cfm?school=17")
						.post();

				ArrayList<Document> pages = new ArrayList<Document>();
				for (Element link : newsPage.select("a[href]")) {

					if (link.select("strong").size() > 0) {
						String s = link.attr("abs:href");
						s = s.replace("popup_info", "news");
						pages.add(Jsoup.connect(s).post());

					}

				}

				Log.d("procedure", "proceeding to get title from pages");
				for (Document d : pages) {

					Log.d("procedure", "starting page check");
					if (d != null) {
						String a = d.select(".sw-newsHeader").text();
						Log.d("title", a);
						Log.d("procedure", "run page check");

						String b = getArticle(d);
						//
						publishProgress(a, b);

					}

				}

			} catch (Exception e) {

			}

			return null;
		}

		@Override
		protected void onProgressUpdate(String... data) {

			// progress info, loading spinner code will also go here
			if (data[1] != null)
				Log.d("data", data[1]);

			if (data[0] != null) {
				titles.add(data[0]);
				Log.d("update", "added item to titles");

			}
			if (data[1] != null) {
				articles.add(data[1]);
				Log.d("update", "added item to articles");
				for (String a : articles) {
				}
			}
			if (articles.get(0) != null)
				Log.d("data", articles.get(0));
			adapter.notifyDataSetChanged();
		}

		@Override
		protected void onPostExecute(Long result) {
			// at the end
			Log.d("data", articles.toString());
			loaded = true;
		}

		protected String getArticle(Document doc) {

			Log.d("procedure", "getting article");
			Element td = doc.select(".sw-newsHeader").get(0).parent();
			Elements kids = td.children();

			Elements images = td.select("img");
			String article = "";

			List<Node> nodes = td.childNodes();
			ArrayList<String> imageLinks = new ArrayList<String>();
			if (doc.select(".sw-newsHeader").text()
					.equals("LadyCats Advanced to the State Championships!"))
				Log.d("channel1", nodes.toString() + 0);
			for (int n = 0; n < nodes.size(); n++) {

				if (images.size() > 0) {

					for (int a = 0; a < images.size(); a++) {
						if (nodes.get(n).outerHtml()
								.contains(images.get(a).attr("src"))) {

							imageLinks.add(images.get(a).absUrl("src"));
						}

					}

				}

				if (!nodes.get(n).outerHtml().contains("sw-newsHeader"))

					article += nodes.get(n).toString();
			}
			watwat.add(doc.select(".sw-newsHeader").text());
			links.add(imageLinks);
			return article;
		}
	}

	public static Bitmap getBitmap(String link) { // will use this to grab the
													// images? Can probably be
													// moved to ArticleActivity
													// assuming no image
													// thumbnails are used for
													// the listview
		/*--- this method downloads an	 Image from the given URL, 
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
