package be.nielsbril.clicket.app.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;

import be.nielsbril.clicket.app.BR;
import be.nielsbril.clicket.app.R;
import be.nielsbril.clicket.app.api.ClicketInstance;
import be.nielsbril.clicket.app.api.SessionsResult;
import be.nielsbril.clicket.app.databinding.FragmentHistoryBinding;
import be.nielsbril.clicket.app.helpers.ApiHelper;
import be.nielsbril.clicket.app.helpers.AuthHelper;
import be.nielsbril.clicket.app.helpers.CustomSnackbar;
import be.nielsbril.clicket.app.helpers.Interfaces;
import be.nielsbril.clicket.app.helpers.Utils;
import be.nielsbril.clicket.app.models.Data;
import rx.functions.Action1;

public class HistoryFragmentViewModel extends BaseObservable {

    private Interfaces.changeToolbar mListener;

    private Context mContext;
    private FragmentHistoryBinding mFragmentHistoryBinding;
    private FloatingActionButton mFab;

    private ObservableField<String> message = new ObservableField<String>();

    public ObservableField<String> getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    private ObservableArrayList<Data> sessions = null;

    public ObservableArrayList<Data> getSessions() {
        return sessions;
    }

    public void setSessions(ObservableArrayList<Data> sessions) {
        this.sessions = sessions;
    }

    public HistoryFragmentViewModel(Context context, FragmentHistoryBinding fragmentHistoryBinding) {
        mContext = context;
        mFragmentHistoryBinding = fragmentHistoryBinding;
        mFab = (FloatingActionButton) fragmentHistoryBinding.getRoot().findViewById(R.id.fabBack);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) mContext).getFragmentManager().popBackStack();
            }
        });

        if (context instanceof Interfaces.changeToolbar) {
            mListener = (Interfaces.changeToolbar) context;
            mListener.setTitle("History");
            mListener.toggleNavItems("park");
        } else {
            throw new RuntimeException(context.toString() + " must implement changeToolbar");
        }
    }

    public void init() {
        mFragmentHistoryBinding.setViewmodel(this);
        setMessage("Loading ...");
        loadSessions();
        notifyPropertyChanged(BR.viewmodel);
    }

    private void loadSessions() {
        setSessions(new ObservableArrayList<Data>());
        ApiHelper.subscribe(ClicketInstance.getClicketserviceInstance().sessions(4, AuthHelper.getAuthToken(mContext)), new Action1<SessionsResult>() {
            @Override
            public void call(SessionsResult sessionsResult) {
                if (sessionsResult != null && sessionsResult.isSuccess()) {
                    if (sessionsResult.getData().size() == 0) {
                        setMessage("No sessions found");
                        mFragmentHistoryBinding.txtMessage.setVisibility(View.VISIBLE);
                        mFragmentHistoryBinding.rvHistory.setVisibility(View.GONE);
                        notifyPropertyChanged(BR.viewmodel);
                    } else {
                        mFragmentHistoryBinding.txtMessage.setVisibility(View.GONE);
                        mFragmentHistoryBinding.rvHistory.setVisibility(View.VISIBLE);
                        for (int i = 0, l = sessionsResult.getData().size(); i < l; i++) {
                            sessions.add(sessionsResult.getData().get(i));
                            notifyPropertyChanged(BR.viewmodel);
                        }
                    }
                } else {
                    if (Utils.isNetworkConnected(mContext)) {
                        setMessage("Error: try again later");
                    } else {
                        Snackbar snackbar = Snackbar.make(mFab, "No internet connection. Please turn on your internet signal first.", Snackbar.LENGTH_LONG);
                        CustomSnackbar.colorSnackBar(snackbar).show();
                        setMessage("No internet connection");
                    }
                    mFragmentHistoryBinding.txtMessage.setVisibility(View.VISIBLE);
                    mFragmentHistoryBinding.rvHistory.setVisibility(View.GONE);
                    notifyPropertyChanged(BR.viewmodel);
                }
            }
        });
    }

}