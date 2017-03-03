package me.uptop.testmaps2.ui.adapters;

import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.uptop.testmaps2.R;
import me.uptop.testmaps2.data.storage.realm.PointsRealm;

public class PointsAdapter  extends RecyclerView.Adapter<PointsAdapter.ViewHolder> {
    List<PointsRealm> mData = new ArrayList<>();
    private Location location;

    public PointsAdapter(List<PointsRealm> data, Location location) {
        mData = data;
        this.location = location;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView metersFromPlace;
        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.list_points_title);
            metersFromPlace = (TextView) v.findViewById(R.id.list_points_metres_from_place);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_points_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PointsRealm point = mData.get(position);
        holder.title.setText(point.getTitle());
        if(location != null) holder.metersFromPlace.setText(calculationByDistance(point.getLatitude(), point.getLongitude())+" km");
        else holder.metersFromPlace.setText(".. km");
    }


    public double calculationByDistance(double latitude, double longtitude) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = location.getLatitude();
        double lat2 = latitude;
        double lon1 = location.getLongitude();
        double lon2 = longtitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        //the below code just to return to the size of either kilometers or meters away
//        double valueResult = Radius * c;
//        double km = valueResult / 1;
//        DecimalFormat newFormat = new DecimalFormat("####");
//        int kmInDec = Integer.valueOf(newFormat.format(km));
//        double meter = valueResult % 1000;
//        int meterInDec = Integer.valueOf(newFormat.format(meter));
//        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
//                + " Meter   " + meterInDec);

        return Radius * c;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
