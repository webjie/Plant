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
import java.util.ArrayList;
import java.util.List;

import person.jack.plant.R;
import person.jack.plant.db.entity.WarnRecord;

/**
 * 警告适配器
 * Created by Administrator on 2018/6/4.
 */

public class WarAdapter extends ArrayAdapter<WarnRecord> {
    int resourceId;
    List<WarnRecord> list=new ArrayList<>();

    public WarAdapter(@NonNull Context context, int resource, @NonNull List<WarnRecord> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
        this.list=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.id=(TextView)view.findViewById(R.id.waring_id);
            viewHolder.name=(TextView)view.findViewById(R.id.waring_name);
            viewHolder.type=(TextView)view.findViewById(R.id.waring_type);
            viewHolder.num=(TextView)view.findViewById(R.id.waring_num);
            viewHolder.date=(TextView)view.findViewById(R.id.waring_time);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.id.setText(position+1+"");
        viewHolder.name.setText(list.get(position).getName());
        viewHolder.type.setText(list.get(position).getType());
        viewHolder.num.setText(Integer.toString(list.get(position).getValue()));
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        viewHolder.date.setText(format.format(list.get(position).getWarnDate()));
        return view;
    }

    class ViewHolder {
        TextView id;
        TextView name;
        TextView type;
        TextView num;
        TextView date;
    }
}
