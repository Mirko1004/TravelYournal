package zkezic.fesb.mimosproject.presentation.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import zkezic.fesb.mimosproject.R;
import zkezic.fesb.mimosproject.data.models.Location;
import zkezic.fesb.mimosproject.presentation.activities.LocationDetailsActivity;
import zkezic.fesb.mimosproject.presentation.utils.Router;
import zkezic.fesb.mimosproject.presentation.viewholders.LocationViewHolder;


public class LocationListAdapter extends RecyclerView.Adapter<LocationViewHolder> {

    private List<Location> locationsMarked = new ArrayList<>();
    private Context context;

    public LocationListAdapter(final Context context, final List<Location> locationsMarked) {
        this.context = context;
        this.locationsMarked = locationsMarked;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location_marked, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LocationViewHolder holder, final int position) {
        final Location location = locationsMarked.get(position);

        holder.locationAddress.setText(location.getAddress());
        holder.locationName.setText(location.getName());
        holder.locationDate.setText(location.getDate());
//        holder.locationImage.setImageBitmap(BitmapFactory.decodeByteArray(location.getImage(),
//                0, location.getImage().length));

        Glide.with(context)
                .load(new File(Environment.getExternalStorageDirectory(),
                        "Travel journal/image_" + Integer.toString(location.getId()) + ".png")) // Uri of the picture
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .into(holder.locationImage);

        holder.locationContainer.setOnClickListener(view ->
                Router.showLocationDetails(context, location.getId())
        );

    }

    @Override
    public long getItemId(int i) {
        return (long) locationsMarked.get(i).getId();
    }

    @Override
    public int getItemCount() {
        return locationsMarked.size();
    }
}
