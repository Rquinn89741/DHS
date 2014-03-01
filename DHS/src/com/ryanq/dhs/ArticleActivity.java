package com.ryanq.dhs;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class ArticleActivity extends Activity {
	String header;
	String message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		header = intent.getStringExtra("title");
		message = intent.getStringExtra("article");
		setContentView(R.layout.activity_article);
		fillTitle();
		fillArticle();

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
		article.setText(message);

	}
}