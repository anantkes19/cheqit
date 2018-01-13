package devops.colby.cheqit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by bybsn on 1/13/2018.
 */

public class TransactionAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Transaction> mDataSource;

    public TransactionAdapter(Context context, ArrayList<Transaction> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        ViewHolder holder;

        if(convertView == null) {

            convertView = mInflater.inflate(R.layout.list_item_transaction, parent, false);

            holder = new ViewHolder();
            holder.nameTextView = (TextView) convertView.findViewById(R.id.transaction_list_name);
            holder.timeTextView = (TextView) convertView.findViewById(R.id.transaction_list_time);
            holder.amountTextView = (TextView) convertView.findViewById(R.id.transaction_list_amount);
            holder.commentTextView = (TextView) convertView.findViewById(R.id.transaction_list_comment);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        TextView nameTextView = holder.nameTextView;
        TextView timeTextView = holder.timeTextView;
        TextView amountTextView = holder.amountTextView;
        TextView commentTextView = holder.commentTextView;
        Transaction transaction = (Transaction) getItem(position);

        nameTextView.setText(transaction.name);
        amountTextView.setText(transaction.amount);
        timeTextView.setText(transaction.time);
        commentTextView.setText(transaction.comment);
        //Picasso.with(mContext).load(recipe.imageUrl).placeholder(R.mipmap.ic_launcher).into(thumbnailImageView);
        return convertView;
    }
    private static class ViewHolder {
        public TextView nameTextView;
        public TextView timeTextView;
        public TextView amountTextView;
        public TextView commentTextView;
    }

}
