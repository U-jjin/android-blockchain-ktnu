package com.universeluvv.ktnuvoting.AdapterPackage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.universeluvv.ktnuvoting.InfoClass.VoteInfo;
import com.universeluvv.ktnuvoting.R;

import java.util.ArrayList;

public class VoteListviewAdapter extends BaseAdapter {

    private ArrayList<VoteInfo> voteInfos =new ArrayList<VoteInfo>();
    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    public VoteListviewAdapter(ArrayList<VoteInfo> voteInfos){
        this.voteInfos =voteInfos;

    }

    @Override
    public int getCount() {
        return voteInfos.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public VoteInfo getItem(int position) {
        return voteInfos.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;
        final Context context = parent.getContext();

        VoteInfo info = voteInfos.get(pos);

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_main_progress_voting, parent, false); }

        TextView name = convertView.findViewById(R.id.vote_name_tv);
        TextView term =convertView.findViewById(R.id.vote_term_tv);
        ArcProgress arcProgress =convertView.findViewById(R.id.progress);
        LinearLayout linearLayout =convertView.findViewById(R.id.colorcheck);
        TextView end_tv =convertView.findViewById(R.id.vote_end_tv);

        int temp = pos%7;

        switch (temp){
            case 0: linearLayout.setBackgroundResource(R.drawable.main_listview_shape_0); break;
            case 1: linearLayout.setBackgroundResource(R.drawable.main_listview_shape_1); break;
            case 2: linearLayout.setBackgroundResource(R.drawable.main_listview_shape_2); break;
            case 3: linearLayout.setBackgroundResource(R.drawable.main_listview_shape_3); break;
            case 4: linearLayout.setBackgroundResource(R.drawable.main_listview_shape_4); break;
            case 5: linearLayout.setBackgroundResource(R.drawable.main_listview_shape_5); break;
            case 6: linearLayout.setBackgroundResource(R.drawable.main_listview_shape_6); break;
        }

        name.setText(info.getName());
        term.setText("마감기한: "+info.getTerm());

        arcProgress.setMax(100);

        int a =info.getCurrent_cnt();
        int b=info.getTotal_cnt();
        double c =(double)a/(double)b*100;

        arcProgress.setProgress((int)c);

        if(info.getCheck()==1){
            arcProgress.setVisibility(View.VISIBLE);
            end_tv.setVisibility(View.GONE);
        }else if(info.getCheck()==2){
            arcProgress.setVisibility(View.GONE);
            end_tv.setVisibility(View.VISIBLE);
        }




        return convertView;
    }
}
