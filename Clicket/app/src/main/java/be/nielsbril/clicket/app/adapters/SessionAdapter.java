package be.nielsbril.clicket.app.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import be.nielsbril.clicket.app.R;
import be.nielsbril.clicket.app.databinding.RowSessionBinding;
import be.nielsbril.clicket.app.helpers.Utils;
import be.nielsbril.clicket.app.models.Data;
import be.nielsbril.clicket.app.models.Session;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionViewHolder> {

    private DateTimeZone dateTimeZone = DateTimeZone.forID("Europe/Brussels");
    private DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
    private DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm:ss");

    private Context mContext;
    private ObservableArrayList<Data> mSessions = null;

    public SessionAdapter(Context context, ObservableArrayList<Data> sessions) {
        mContext = context;
        mSessions = sessions;
    }

    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowSessionBinding rowSessionBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_session, parent, false);
        SessionViewHolder sessionViewHolder = new SessionViewHolder(rowSessionBinding);
        return sessionViewHolder;
    }

    @Override
    public void onBindViewHolder(SessionViewHolder holder, int position) {
        Session session = mSessions.get(position).getSession();
        holder.getRowSessionBinding().setSession(session);
        holder.getRowSessionBinding().executePendingBindings();

        holder.getRowSessionBinding().txtPrice.setText("€" + String.valueOf(Utils.roundToDecimals(mSessions.get(position).getInfo().getPrice().getTotal(), 2)));
        DateTime start = new DateTime(session.getStarted_on());
        start = start.withZone(dateTimeZone);
        DateTime stop = new DateTime(session.getStopped_on());
        stop = stop.withZone(dateTimeZone);
        holder.getRowSessionBinding().txtDate.setText(start.toString(dateTimeFormatter) + " - " + stop.toString(timeFormatter));
    }

    @Override
    public int getItemCount() {
        if (mSessions != null) {
            return mSessions.size();
        }
        return 0;
    }

    public class SessionViewHolder extends RecyclerView.ViewHolder {

        private final RowSessionBinding mRowSessionBinding;

        private SessionViewHolder(RowSessionBinding rowSessionBinding) {
            super(rowSessionBinding.getRoot());
            mRowSessionBinding = rowSessionBinding;
        }

        private RowSessionBinding getRowSessionBinding() {
            return mRowSessionBinding;
        }

    }

}