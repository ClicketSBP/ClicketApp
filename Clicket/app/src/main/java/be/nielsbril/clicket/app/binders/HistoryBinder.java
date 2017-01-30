package be.nielsbril.clicket.app.binders;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.support.v7.widget.RecyclerView;

import be.nielsbril.clicket.app.adapters.SessionAdapter;
import be.nielsbril.clicket.app.models.Data;

public class HistoryBinder {

    @BindingAdapter("items")
    public static void setHistory(RecyclerView recyclerView, ObservableArrayList<Data> sessions) {
        if(sessions != null) {
            SessionAdapter sessionAdapter = new SessionAdapter(recyclerView.getContext(), sessions);
            recyclerView.setAdapter(sessionAdapter);
            sessionAdapter.notifyDataSetChanged();
        }
    }

}