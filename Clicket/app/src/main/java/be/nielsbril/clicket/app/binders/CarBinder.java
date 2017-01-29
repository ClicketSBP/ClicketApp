package be.nielsbril.clicket.app.binders;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.support.v7.widget.RecyclerView;

import be.nielsbril.clicket.app.adapters.CarAdapter;
import be.nielsbril.clicket.app.models.Car;

public class CarBinder {

    @BindingAdapter("items")
    public static void setCars(RecyclerView recyclerView, ObservableArrayList<Car> cars) {
        if(cars != null) {
            CarAdapter carAdapter = new CarAdapter(recyclerView.getContext(), cars);
            recyclerView.setAdapter(carAdapter);
            carAdapter.notifyDataSetChanged();
        }
    }

}