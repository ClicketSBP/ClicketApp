package be.nielsbril.clicket.app.views;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.LocationRequest;

import be.nielsbril.clicket.app.App;
import be.nielsbril.clicket.app.R;
import be.nielsbril.clicket.app.databinding.FragmentParkBinding;
import be.nielsbril.clicket.app.helpers.CustomSnackbar;
import be.nielsbril.clicket.app.helpers.Utils;
import be.nielsbril.clicket.app.viewmodels.ParkFragmentViewModel;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.functions.Action1;

@RuntimePermissions
public class ParkFragment extends Fragment {

    private ParkFragmentViewModel mParkFragmentViewModel;
    private FragmentParkBinding mFragmentParkBinding;
    private ReactiveLocationProvider mReactiveLocationProvider;

    LocationRequest mLocationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setNumUpdates(5).setInterval(1000);

    public static Fragment newInstance() {
        return new ParkFragment();
    }

    public ParkFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentParkBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_park, container, false);
        mParkFragmentViewModel = new ParkFragmentViewModel(getActivity(), mFragmentParkBinding);
        mReactiveLocationProvider = new ReactiveLocationProvider(getActivity());
        return mFragmentParkBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        mParkFragmentViewModel.init();
        App.setParkFragment(this);
        ParkFragmentPermissionsDispatcher.getLocationPollingWithCheck(this);
    }

    public static void requestLocation() {
        ParkFragmentPermissionsDispatcher.getLocationWithCheck(App.getParkFragment());
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mReactiveLocationProvider.getLastKnownLocation().subscribe(new Action1<Location>() {
            @Override
            public void call(Location location) {
                mParkFragmentViewModel.setLocation(location);
            }
        });
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public void getLocationPolling() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mReactiveLocationProvider.getUpdatedLocation(mLocationRequest).subscribe(new Action1<Location>() {
            @Override
            public void call(Location location) {
                mParkFragmentViewModel.setLocation(location);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ParkFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    public void showRationaleForAccounts(PermissionRequest request) {
        Utils.showRationaleDialog(getActivity(), "Location permission needed to get location", request);
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    public void onAccountsDenied() {
        Snackbar snackbar = Snackbar.make(mFragmentParkBinding.btnStart, "Location permission denied, consider accepting to use this app", Snackbar.LENGTH_SHORT);
        CustomSnackbar.colorSnackBar(snackbar).show();
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    public void onAccountsNeverAskAgain() {
        Snackbar snackbar = Snackbar.make(mFragmentParkBinding.btnStart, "Location permission denied with never ask again", Snackbar.LENGTH_SHORT);
        CustomSnackbar.colorSnackBar(snackbar).show();
    }

}