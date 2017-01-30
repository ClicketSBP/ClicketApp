package be.nielsbril.clicket.app.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableDouble;
import android.databinding.ObservableField;
import android.location.Location;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import be.nielsbril.clicket.app.App;
import be.nielsbril.clicket.app.BR;
import be.nielsbril.clicket.app.R;
import be.nielsbril.clicket.app.api.CarResult;
import be.nielsbril.clicket.app.api.ClicketInstance;
import be.nielsbril.clicket.app.api.SessionSingleResult;
import be.nielsbril.clicket.app.api.SessionStopResult;
import be.nielsbril.clicket.app.api.UserResult;
import be.nielsbril.clicket.app.databinding.FragmentParkBinding;
import be.nielsbril.clicket.app.helpers.ApiHelper;
import be.nielsbril.clicket.app.helpers.AuthHelper;
import be.nielsbril.clicket.app.helpers.CustomSnackBar;
import be.nielsbril.clicket.app.helpers.Interfaces;
import be.nielsbril.clicket.app.helpers.Utils;
import be.nielsbril.clicket.app.models.Car;
import be.nielsbril.clicket.app.models.Session;
import be.nielsbril.clicket.app.views.AddCarFragment;
import be.nielsbril.clicket.app.views.HistoryFragment;
import rx.functions.Action1;

public class ParkFragmentViewModel extends BaseObservable implements AdapterView.OnItemSelectedListener {

    private DateTimeZone dateTimeZone = DateTimeZone.forID("Europe/Brussels");
    private DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");

    private Interfaces.changeToolbar mListener;
    private Interfaces.navigate mNavigator;

    private Context mContext;
    private FragmentParkBinding mFragmentParkBinding;

    private Spinner mSpCars;
    private AppCompatButton mBtnStart;
    private AppCompatButton mBtnStop;
    private AppCompatButton mBtnHistory;

    private int mId;
    private Location mLocation;
    private Session mSession;
    private ObservableField<String> started_on = new ObservableField<String>();
    private ObservableField<String> stopped_on = new ObservableField<String>();
    private ObservableField<String> zone = new ObservableField<String>();
    private ObservableField<String> car = new ObservableField<String>();
    private ObservableDouble cost = new ObservableDouble();

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }

    public Session getSession() {
        return mSession;
    }

    public void setSession(Session session) {
        mSession = session;
    }

    public ObservableField<String> getStarted_on() {
        return started_on;
    }

    public void setStarted_on(String started_on) {
        this.started_on.set(started_on);
    }

    public ObservableField<String> getStopped_on() {
        return stopped_on;
    }

    public void setStopped_on(String stopped_on) {
        this.stopped_on.set(stopped_on);
    }

    public ObservableField<String> getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone.set(zone);
    }

    public ObservableField<String> getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car.set(car);
    }

    public ObservableDouble getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost.set(cost);
    }

    private ArrayList<String> mCars = new ArrayList<String>();
    private ArrayList<Integer> mCarIds = new ArrayList<Integer>();

    public ParkFragmentViewModel(Context context, FragmentParkBinding fragmentParkBinding) {
        mContext = context;
        mFragmentParkBinding = fragmentParkBinding;

        mSpCars = (Spinner) fragmentParkBinding.getRoot().findViewById(R.id.spCars);
        mBtnStart = (AppCompatButton) fragmentParkBinding.getRoot().findViewById(R.id.btnStart);
        mBtnStop = (AppCompatButton) fragmentParkBinding.getRoot().findViewById(R.id.btnStop);
        mBtnHistory = (AppCompatButton) fragmentParkBinding.getRoot().findViewById(R.id.btnHistory);

        mSpCars.setOnItemSelectedListener(this);

        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });

        mBtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop();
            }
        });

        mBtnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNavigator.navigateFragment(HistoryFragment.newInstance(), "historyFragment");
            }
        });

        if (context instanceof Interfaces.changeToolbar) {
            mListener = (Interfaces.changeToolbar) context;
            mListener.setTitle("Clicket");
            mListener.toggleNavItems("park");
        } else {
            throw new RuntimeException(context.toString() + " must implement changeToolbar");
        }

        if (context instanceof Interfaces.navigate) {
            mNavigator = (Interfaces.navigate) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement navigate");
        }
    }

    public void init() {
        mFragmentParkBinding.setViewmodel(this);
        loadCars();
        notifyPropertyChanged(BR.viewmodel);
    }

    private void loadCars() {
        ApiHelper.subscribe(ClicketInstance.getClicketserviceInstance().cars(AuthHelper.getAuthToken(mContext)), new Action1<CarResult>() {
            @Override
            public void call(CarResult carResult) {
                if (carResult != null && carResult.isSuccess()) {
                    if (carResult.getData().size() == 0) {
                        showSnackbar("Please add a car first");
                        mNavigator.navigateFragment(AddCarFragment.newInstance(), "addCarFragment");
                    } else {
                        if (mCars.size() == 0) {
                            for (int i = 0, l = carResult.getData().size(); i < l; i++) {
                                Car car = carResult.getData().get(i);
                                mCars.add(car.getName() + " (" + car.getLicense_plate() + ")");
                                mCarIds.add(car.get_id());
                            }

                            ArrayAdapter<String> carsAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, mCars);
                            carsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            mSpCars.setAdapter(carsAdapter);

                            loadCurrentSession();
                        }
                    }
                } else {
                    showSnackbar("Error: try again later");
                    startButton(false);
                    stopButton(false);
                }
            }
        });
    }

    private void loadCurrentSession() {
        ApiHelper.subscribe(ClicketInstance.getClicketserviceInstance().activeSession(AuthHelper.getAuthToken(mContext)), new Action1<SessionSingleResult>() {
            @Override
            public void call(SessionSingleResult sessionSingleResult) {
                if (sessionSingleResult != null && sessionSingleResult.isSuccess()) {
                    Session session = sessionSingleResult.getData();
                    setSession(session);
                    startButton(false);
                    stopButton(true);
                    DateTime start = new DateTime(getSession().getStarted_on());
                    start = start.withZone(dateTimeZone);
                    setStarted_on(start.toString(dateTimeFormatter));
                    setStopped_on("n.a.");
                    setZone(getSession().getZone_id().getName() + " (" + getSession().getStreet() + ")");
                    setCar(getSession().getCar_id().getName() + " (" + getSession().getCar_id().getLicense_plate() + ")");
                    setCost(0);
                } else {
                    noCurrentSession();
                }
            }
        });
    }

    private void start() {
        ApiHelper.subscribe(ClicketInstance.getClicketserviceInstance().startSession(Double.toString(mLocation.getLatitude()), Double.toString(mLocation.getLongitude()), mId, AuthHelper.getAuthToken(mContext)), new Action1<SessionSingleResult>() {
            @Override
            public void call(SessionSingleResult sessionSingleResult) {
                if (sessionSingleResult != null && sessionSingleResult.isSuccess()) {
                    setSession(sessionSingleResult.getData());
                    startButton(false);
                    stopButton(true);
                    DateTime start = new DateTime(getSession().getStarted_on());
                    start = start.withZone(dateTimeZone);
                    setStarted_on(start.toString(dateTimeFormatter));
                    setStopped_on("n.a.");
                    setZone(getSession().getZone_id().getName() + " (" + getSession().getStreet() + ")");
                    setCar(getSession().getCar_id().getName() + " (" + getSession().getCar_id().getLicense_plate() + ")");
                    setCost(0);
                } else {
                    showSnackbar("Error when starting session: zone not found, Clicket won't work here");
                }
            }
        });
    }

    private void stop() {
        ApiHelper.subscribe(ClicketInstance.getClicketserviceInstance().stopSession(getSession().get_id(), AuthHelper.getAuthToken(mContext)), new Action1<SessionStopResult>() {
            @Override
            public void call(SessionStopResult sessionStopResult) {
                if (sessionStopResult != null && sessionStopResult.isSuccess()) {
                    setSession(null);
                    startButton(true);
                    stopButton(false);
                    DateTime stop = new DateTime(sessionStopResult.getData().getSession().getStopped_on());
                    stop = stop.withZone(dateTimeZone);
                    setStopped_on(stop.toString(dateTimeFormatter));
                    setCost(sessionStopResult.getData().getInfo().getPrice().getTotal());
                    showSnackbar("Stopped your session. You parked for €" + Utils.roundToDecimals(sessionStopResult.getData().getInfo().getPrice().getTotal(), 2) + ".");
                    updateUser();
                } else {
                    showSnackbar("Error when stopping session");
                }
            }
        });
    }

    private void updateUser() {
        ApiHelper.subscribe(ClicketInstance.getClicketserviceInstance().user(AuthHelper.getAuthToken(mContext)), new Action1<UserResult>() {
            @Override
            public void call(UserResult userResult) {
                if (userResult.isSuccess()) {
                    ((App) ((Activity) mContext).getApplication()).setUser(userResult.getData());
                }
            }
        });
    }

    private void startButton(boolean enabled) {
        mBtnStart.setEnabled(enabled);
        if (enabled) {
            mBtnStart.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        } else {
            mBtnStart.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorCard));
        }
    }

    private void stopButton(boolean enabled) {
        mBtnStop.setEnabled(enabled);
        if (enabled) {
            mBtnStop.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
            mBtnStop.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        } else {
            mBtnStop.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorCard));
            mBtnStop.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        }
    }

    private void noCurrentSession() {
        setStarted_on("n.a.");
        setStopped_on("n.a.");
        setZone("n.a.");
        setCar("n.a.");
        setCost(0);
        startButton(true);
        stopButton(false);
        notifyPropertyChanged(BR.viewmodel);
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(mSpCars, message, Snackbar.LENGTH_LONG);
        CustomSnackBar.colorSnackBar(snackbar).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (mCars.size() != 0) {
            String car = mCars.get(i);
            mId = mCarIds.get(i);
            showSnackbar(car + " selected");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        showSnackbar("Please select a car");
    }

}