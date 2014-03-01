package com.ryanq.dhs;

import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
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
	private ArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		try {
			new DownloadFilesTask().execute(new URL("http://random.net"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// for (String s : titles) {

		// if (s != null)
		// Log.d("wat", s);
		// }
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (ListView) findViewById(R.id.listview);
		article = new Intent(this, ArticleActivity.class);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, titles);

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

						String b = getArticle(d);
						publishProgress(a, b);

					}

				}

			} catch (Exception e) {

			}

			return null;
		}

		@Override
		protected void onProgressUpdate(String... data) {
			// progress info

			if (data[0] != null) {
				titles.add(data[0]);
				Log.d("updsate", "added item to titles");

			}
			if (data[1] != null) {
				articles.add(data[1]);
				Log.d("update", "added item to articles");
				for (String a : articles) {
					Log.d("update", a.substring(0, 50));
				}
			}
			adapter.notifyDataSetChanged();
		}

		@Override
		protected void onPostExecute(Long result) {
			// at the end
		}

		protected String getArticle(Document doc) {

			Log.d("r", "running article code)");
			Element table = doc.select(".sw-newsHeader").get(0).parent()
					.parent().parent();
			Elements s = table.select("p");
			Log.d("s", s.text());
			if (s.text().length() > 0)
				return s.text();
			/*
			 * for (Element para : s) { Log.d("r", "searching element " +
			 * para.text()); if (para.text().length() > 0 &&
			 * (para.previousSibling().previousSibling() .previousSibling()
			 * .equals(doc.select(".sw-newsHeader")) || para
			 * .previousSibling().previousSibling()
			 * .equals(doc.select(".sw-newsHeader")))) {
			 * 
			 * return para.text(); }
			 */
			return null;

		}

	}
}
