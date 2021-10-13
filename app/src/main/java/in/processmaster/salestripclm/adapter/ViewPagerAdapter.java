package in.processmaster.salestripclm.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.Objects;

import in.processmaster.salestripclm.R;
import in.processmaster.salestripclm.models.DownloadFileModel;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<DownloadFileModel> images;
    LayoutInflater mLayoutInflater;

    public ViewPagerAdapter(Context context, ArrayList<DownloadFileModel> images) {
        this.context = context;
        this.images = images;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        View itemView = mLayoutInflater.inflate(R.layout.itemexp, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewMain);

        DownloadFileModel model=images.get(position);
        imageView.setImageBitmap(BitmapFactory.decodeFile(model.getFilePath()));
        Objects.requireNonNull(container).addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((LinearLayout) object);
    }
}