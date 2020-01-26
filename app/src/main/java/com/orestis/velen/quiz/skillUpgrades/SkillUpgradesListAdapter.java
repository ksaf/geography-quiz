package com.orestis.velen.quiz.skillUpgrades;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.orestis.velen.quiz.R;

import java.util.List;

public class SkillUpgradesListAdapter extends ArrayAdapter<SkillUpgradesItem> {

    private List<SkillUpgradesItem> dataSet;
    Context context;
    private ViewHolder viewHolder;


    private static class ViewHolder {
        ImageView upgradeSkillIconBGColor;
        ImageView upgradeSkillIconDrawable;
        TextView skillDescription;
        TextView currentSkillLevel;
        Button plusSkillBtn;
        Button minusSkillBtn;
    }

    public SkillUpgradesListAdapter(List<SkillUpgradesItem> data, Context context) {
        super(context, R.layout.skill_upgrade_item, data);
        this.dataSet = data;
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SkillUpgradesItem dataModel = getItem(position);

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.skill_upgrade_item, parent, false);
            viewHolder.upgradeSkillIconBGColor = convertView.findViewById(R.id.upgrade_skill_icon_bg_color);
            viewHolder.upgradeSkillIconDrawable = convertView.findViewById(R.id.upgrade_skill_icon_drawable);
            viewHolder.skillDescription =  convertView.findViewById(R.id.skillDescription);
            viewHolder.currentSkillLevel = convertView.findViewById(R.id.current_skill_level_text);
            viewHolder.plusSkillBtn = convertView.findViewById(R.id.plus_skill_btn);
            viewHolder.minusSkillBtn = convertView.findViewById(R.id.minus_skill_btn);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.upgradeSkillIconBGColor.setImageResource(dataModel.getBackGroundColorId());
        viewHolder.upgradeSkillIconDrawable.setImageResource(dataModel.getIconDrawableId());
        if(dataModel.isUnlocked()) {
            viewHolder.skillDescription.setText(Html.fromHtml(dataModel.getDescriptionForSelectedLevel(context)));
            viewHolder.currentSkillLevel.setText(String.valueOf(dataModel.getSkillLevel()));
            viewHolder.plusSkillBtn.setOnClickListener(dataModel.getPlusClickListener());
            viewHolder.plusSkillBtn.setTag(position);
            viewHolder.minusSkillBtn.setOnClickListener(dataModel.getMinusClickListener());
            viewHolder.minusSkillBtn.setTag(position);
        } else {
            viewHolder.skillDescription.setText(Html.fromHtml("Unlock at level: <b><font color='#000000'>" + dataModel.getUnlockLevel() + "</font></b>"));
            viewHolder.currentSkillLevel.setVisibility(View.INVISIBLE);
            viewHolder.plusSkillBtn.setVisibility(View.INVISIBLE);
            viewHolder.minusSkillBtn.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }
}
