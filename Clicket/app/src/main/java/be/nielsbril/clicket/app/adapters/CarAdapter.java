package be.nielsbril.clicket.app.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nielsbril.clicket.app.R;
import be.nielsbril.clicket.app.databinding.RowCarBinding;
import be.nielsbril.clicket.app.helpers.Interfaces;
import be.nielsbril.clicket.app.models.Car;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private Interfaces.onCarSelectedListener mListener;

    private Context mContext;
    private ObservableArrayList<Car> mCars = null;

    public CarAdapter(Context context, ObservableArrayList<Car> cars) {
        mContext = context;
        mCars = cars;
        if (context instanceof Interfaces.onCarSelectedListener) {
            mListener = (Interfaces.onCarSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement onCarSelectedListener");
        }
    }

    @Override
    public CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowCarBinding rowCarBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_car, parent, false);
        CarViewHolder carViewHolder = new CarViewHolder(rowCarBinding);
        return carViewHolder;
    }

    @Override
    public void onBindViewHolder(CarViewHolder holder, int position) {
        Car car = mCars.get(position);
        holder.getRowCarBinding().setCar(car);
        holder.getRowCarBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if (mCars != null) {
            return mCars.size();
        }
        return 0;
    }

    public class CarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final RowCarBinding mRowCarBinding;

        private CarViewHolder(RowCarBinding rowCarBinding) {
            super(rowCarBinding.getRoot());
            mRowCarBinding = rowCarBinding;
            rowCarBinding.getRoot().setOnClickListener(this);
        }

        private RowCarBinding getRowCarBinding() {
            return mRowCarBinding;
        }

        @Override
        public void onClick(View view) {
            Car selectedCar = mCars.get(getAdapterPosition());
            mListener.onCarSelected(selectedCar);
        }

    }

}