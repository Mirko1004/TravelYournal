package zkezic.fesb.mimosproject.presentation.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import zkezic.fesb.mimosproject.R;

public class LocationViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.marked_location_container)
    public LinearLayout locationContainer;

    @BindView(R.id.location_image)
    public ImageView locationImage;

    @BindView(R.id.location_name)
    public TextView locationName;

    @BindView(R.id.location_address)
    public TextView locationAddress;

    @BindView(R.id.location_date)
    public TextView locationDate;

    public LocationViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
