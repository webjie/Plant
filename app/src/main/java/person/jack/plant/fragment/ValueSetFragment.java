package person.jack.plant.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import person.jack.plant.R;
import person.jack.plant.common.AppContext;
import person.jack.plant.db.dao.ValueSetDao;
import person.jack.plant.db.entity.ValueSet;

/**
 * Created by yanxu on 2018/6/4.
 */

public class ValueSetFragment  extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_valueset, container, false);
       ValueSet valueSet=new ValueSet(1,"广玉兰",20,30);
        ValueSetDao valueSetDao=new ValueSetDao(AppContext.getInstance());
        valueSetDao.add(valueSet);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
