package edu.calvin.cs262.pilot.knightrank;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.List;

/**
 * Class ConfirmCheckboxAdapter defines and configures a BaseAdapter for the purpose of displaying
 * player rankings.
 */
public class ConfirmCheckboxAdapter extends BaseAdapter {

    private List<ConfirmItemDTO> listViewItemDtoList = null;

    private Context ctx = null;

    public ConfirmCheckboxAdapter(Context ctx, List<ConfirmItemDTO> listViewItemDtoList) {
        this.ctx = ctx;
        this.listViewItemDtoList = listViewItemDtoList;
    }

    @Override
    public int getCount() {
        int ret = 0;
        if(listViewItemDtoList!=null)
        {
            ret = listViewItemDtoList.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int itemIndex) {
        Object ret = null;
        if(listViewItemDtoList!=null) {
            ret = listViewItemDtoList.get(itemIndex);
        }
        return ret;
    }

    @Override
    public long getItemId(int itemIndex) {
        return itemIndex;
    }

    @Override
    public View getView(int itemIndex, View convertView, ViewGroup viewGroup) {

        ConfirmItemViewHolder viewHolder = null;

        if(convertView!=null)
        {
            viewHolder = (ConfirmItemViewHolder) convertView.getTag();
        }else
        {
            convertView = View.inflate(ctx, R.layout.fragment_confirm_items, null);

            CheckBox listItemCheckbox = (CheckBox) convertView.findViewById(R.id.list_view_item_checkbox);

            TextView listItemText = (TextView) convertView.findViewById(R.id.list_view_item_text);

            viewHolder = new ConfirmItemViewHolder(convertView);

            viewHolder.setItemCheckbox(listItemCheckbox);

            viewHolder.setItemTextView(listItemText);

            convertView.setTag(viewHolder);
        }

        ConfirmItemDTO confirmItemDto = listViewItemDtoList.get(itemIndex);
        viewHolder.getItemCheckbox().setChecked(confirmItemDto.isChecked());
        viewHolder.getItemTextView().setText(confirmItemDto.getItemText());

        return convertView;
    }
}
