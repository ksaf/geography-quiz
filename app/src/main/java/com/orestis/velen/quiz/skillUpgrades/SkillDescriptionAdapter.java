package com.orestis.velen.quiz.skillUpgrades;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.orestis.velen.quiz.R;
import java.util.List;

public class SkillDescriptionAdapter extends ArrayAdapter<SkillDescriptionItem> {
    private List<SkillDescriptionItem> dataSet;
    private ViewHolder viewHolder;

    private static class ViewHolder {
        TextView skillLevel;
        TextView bonusNumber;
        TextView skillUsages;
    }

    public SkillDescriptionAdapter(List<SkillDescriptionItem> data, Context context) {
        super(context, R.layout.skill_description_item, data);
        this.dataSet = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final SkillDescriptionItem dataModel = getItem(position);

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.skill_description_item, parent, false);
            viewHolder.skillLevel = convertView.findViewById(R.id.skillLevel);
            viewHolder.bonusNumber = convertView.findViewById(R.id.bonusNumber);
            viewHolder.skillUsages = convertView.findViewById(R.id.skillUsages);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.skillLevel.setText(String.valueOf(dataModel.getLevel()) + ".");
        viewHolder.bonusNumber.setText(dataModel.getBonusSign() + dataModel.getBonus());
        viewHolder.skillUsages.setText(dataModel.getUsages() > 0 ? String.valueOf(dataModel.getUsages()) : "");

        if(position ==  dataModel.getCurrentLevel() - 1) {
            convertView.setBackgroundColor(Color.parseColor("#80ee9f3c"));
            viewHolder.skillLevel.setTextColor(Color.parseColor("#ffffff"));
            viewHolder.bonusNumber.setTextColor(Color.parseColor("#ffffff"));
            viewHolder.skillUsages.setTextColor(Color.parseColor("#ffffff"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#00ee9f3c"));
            viewHolder.skillLevel.setTextColor(Color.parseColor("#333333"));
            viewHolder.bonusNumber.setTextColor(Color.parseColor("#333333"));
            viewHolder.skillUsages.setTextColor(Color.parseColor("#333333"));
        }

        return convertView;
    }
}
