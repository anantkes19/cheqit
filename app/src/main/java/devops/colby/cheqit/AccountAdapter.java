package devops.colby.cheqit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by bybsn on 1/19/2018.
 */

public class AccountAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Account> mDataSource;

    public AccountAdapter(Context context, ArrayList<Account> items) {
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
        AccountAdapter.ViewHolder holder;

        if(convertView == null) {

            convertView = mInflater.inflate(R.layout.list_item_account, parent, false);

            holder = new AccountAdapter.ViewHolder();
            holder.nameTextView = (TextView) convertView.findViewById(R.id.account_list_name);
            holder.totalAmountTextView = (TextView) convertView.findViewById(R.id.account_list_total_amount);
            holder.amountTextView = (TextView) convertView.findViewById(R.id.account_list_current_amount);
            holder.commentTextView = (TextView) convertView.findViewById(R.id.account_list_comment);
            convertView.setTag(holder);
        }
        else{
            holder = (AccountAdapter.ViewHolder) convertView.getTag();
        }
        Account account = (Account) getItem(position);
        String amount = "Current Amount: $" + String.valueOf(account.getAmount());
        String totalAmount = "Total Spent: $" + String.valueOf(account.getTotalSpent());
        TextView nameTextView = holder.nameTextView;
        TextView totalAmountTextView = holder.totalAmountTextView;
        TextView amountTextView = holder.amountTextView;
        TextView commentTextView = holder.commentTextView;

        nameTextView.setText(account.getName());
        amountTextView.setText(amount);
        totalAmountTextView.setText(totalAmount);
        commentTextView.setText(account.getComment());
        //Picasso.with(mContext).load(recipe.imageUrl).placeholder(R.mipmap.ic_launcher).into(thumbnailImageView);
        return convertView;
    }
    private static class ViewHolder {
        public TextView nameTextView;
        public TextView totalAmountTextView;
        public TextView amountTextView;
        public TextView commentTextView;
    }
}
