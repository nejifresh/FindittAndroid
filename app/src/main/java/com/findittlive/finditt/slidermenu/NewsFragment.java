package com.findittlive.finditt.slidermenu;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cloudant.sync.datastore.BasicDocumentRevision;
import com.cloudant.sync.datastore.Datastore;
import com.cloudant.sync.datastore.DatastoreManager;
import com.findittlive.finditt.News.News;
import com.findittlive.finditt.News.NewsAdapter;
import com.findittlive.finditt.R;
import com.findittlive.finditt.Search.Content;
import com.findittlive.finditt.Search.GlobalClass;
import com.findittlive.finditt.Search.ItemAdapter;
import com.findittlive.finditt.Search.Items;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Neji on 11/8/2014.
 */
public class NewsFragment extends Fragment {
    private ListView listView1;
    private static final String DATASTORE_MANGER_DIR = "findittData";
    private static final String DATASTORE_NAME = "findittnews";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_news, container, false);
        listView1 = (ListView)getActivity().findViewById(R.id.listview1);
        File path = getActivity().getApplicationContext().getDir(DATASTORE_MANGER_DIR, 1);
        DatastoreManager manager = new DatastoreManager(path.getAbsolutePath());
        Datastore datastore = manager.openDatastore(DATASTORE_NAME);
        Log.d("store", "Store created");
       Toast.makeText(getActivity(),"created dbase",Toast.LENGTH_SHORT).show();

        int nDocs = datastore.getDocumentCount();

        // showToast("Loading Completed " +nDocs+ " Locations Found");
        List<BasicDocumentRevision> alldocs = datastore.getAllDocuments(0, nDocs, true);
        List<News> myNews = new ArrayList<News>();
        for(BasicDocumentRevision rev : alldocs) {
            News t = News.giveNewsDetails(rev, getActivity());
            if (t != null) {
                myNews.add(t);
            }
            myListView(myNews);

        }
        return rootView;
    }

    public void myListView(final List theNews) {

        List<News> News = theNews;
        //Collections.sort(News);
        NewsAdapter adapter = new NewsAdapter(getActivity(),theNews);
        adapter.notifyDataSetChanged();
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                /**
                Items thisItem = (Items)(adapterView.getItemAtPosition(position));

                globalVariable.setName(thisItem.getBusinessName());
                globalVariable.setPhone(thisItem.getPhone());
                globalVariable.setDescription(thisItem.getDescription());
                globalVariable.setType(thisItem.getType());
                globalVariable.setAddress(thisItem.getAddress());
                globalVariable.setMaLongitude(thisItem.getMaLongitude());
                globalVariable.setMaLatitude(thisItem.getMaLatitude());
                globalVariable.setImage(thisItem.getImage());
                globalVariable.setImage2(thisItem.getImage2());
                globalVariable.setImage3(thisItem.getImage3());
                globalVariable.setImage4(thisItem.getImage4());
                globalVariable.setImage5(thisItem.getImage5());
                Intent i = new Intent(SearchResults.this,Content.class);

                startActivity(i);
                 **/
            }
        });
        listView1.setAdapter(adapter);
    }
}
