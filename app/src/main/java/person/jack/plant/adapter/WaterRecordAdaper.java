package person.jack.plant.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import person.jack.plant.R;
import person.jack.plant.db.entity.WaterRecord;

/**
 *
 * 灌溉记录绑定数据显示 Adapter
 * Created by yanxu on 2018/6/4.
 */

public class WaterRecordAdaper extends ArrayAdapter<WaterRecord> {
    private Integer resourceId;

    public WaterRecordAdaper(@NonNull Context context, int resource, @NonNull List<WaterRecord> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        WaterRecord waterRecord=getItem(position);
        ViewHolder viewHolder=null;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
           viewHolder=(ViewHolder)convertView.getTag();
        }

        viewHolder.tv_waterId.setText(position+1+"");
        viewHolder.tv_waterName.setText(waterRecord.getName());
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        viewHolder.tv_waterDate.setText(format.format(waterRecord.getWaterDate()));
        return convertView;
    }

    public static class ViewHolder {
        public View rootView;
        public TextView tv_waterId;
        public TextView tv_waterName;
        public TextView tv_waterDate;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tv_waterId = (TextView) rootView.findViewById(R.id.tv_waterId);
            this.tv_waterName = (TextView) rootView.findViewById(R.id.tv_waterName);
            this.tv_waterDate = (TextView) rootView.findViewById(R.id.tv_waterDate);
        }

    }
}
