package com.github.hanlyjiang.loaderdemo;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

public class LoaderDemoActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener,LoaderManager.LoaderCallbacks<Cursor> {

    private String mCurText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader_demo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        bindViews();
        getLoaderManager().initLoader(0, null, this);

    }

    private ListView mListView;

    private SimpleCursorAdapter mCursorAdapter;
    /** Bind all Views  */
    private void bindViews() {


        mListView = (ListView) findViewById(R.id.listView);


        mCursorAdapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_2,null,
            new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.CONTACT_STATUS},
            new int[]{android.R.id.text1,android.R.id.text2},0);

        mListView.setAdapter(mCursorAdapter);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuItem item = menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView sv = new SearchView(this);
        sv.setOnQueryTextListener(this);
        item.setActionView(sv);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    static final String[] CONTACTS_SUMMARY_PROJECTION = new String[]{
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.CONTACT_STATUS,
            ContactsContract.Contacts.CONTACT_PRESENCE,
            ContactsContract.Contacts.PHOTO_ID,
            ContactsContract.Contacts.LOOKUP_KEY,

    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri ;
        if(mCurText != null){
            baseUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI,
                    Uri.encode(mCurText));
        }else{
            baseUri = ContactsContract.Contacts.CONTENT_URI;
        }

        String select = "((" + ContactsContract.Contacts.DISPLAY_NAME + " NOTNULL) AND (" +
                ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND (" +
                ContactsContract.Contacts.DISPLAY_NAME +" != '' ))";

        return  new CursorLoader(this,baseUri,CONTACTS_SUMMARY_PROJECTION,select,null,null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mCurText = !TextUtils.isEmpty(newText)?newText:null;
        getLoaderManager().restartLoader(0,null,this);
        return true;
    }
}
