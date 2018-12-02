package edu.calvin.cs262.pilot.knightrank;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class ConfirmCheckboxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_confirm_match);

        setTitle("Confirm Results");

        // Get listview checkbox.
        final ListView listViewWithCheckbox = (ListView)findViewById(R.id.list_view_with_checkbox);

        // Initiate listview data.
        final List<ConfirmItemDTO> initItemList = this.getInitViewItemDtoList();

        // Create a custom list view adapter with checkbox control.
        final ConfirmCheckboxAdapter listViewDataAdapter = new ConfirmCheckboxAdapter(getApplicationContext(), initItemList);

        listViewDataAdapter.notifyDataSetChanged();

        // Set data adapter to list view.
        listViewWithCheckbox.setAdapter(listViewDataAdapter);

        // When list view item is clicked.
        listViewWithCheckbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {
                // Get user selected item.
                Object itemObject = adapterView.getAdapter().getItem(itemIndex);

                // Translate the selected item to DTO object.
                ConfirmItemDTO itemDto = (ConfirmItemDTO)itemObject;

                // Get the checkbox.
                CheckBox itemCheckbox = (CheckBox) view.findViewById(R.id.list_view_item_checkbox);

                // Reverse the checkbox and clicked item check state.
                if(itemDto.isChecked()) {
                    itemCheckbox.setChecked(false);
                    itemDto.setChecked(false);
                } else {
                    itemCheckbox.setChecked(true);
                    itemDto.setChecked(true);
                }
            }
        });

        // Click this button to reverse select listview items.
        Button invertButton = (Button)findViewById(R.id.list_select);
        invertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = initItemList.size();
                for(int i=0;i<size;i++)
                {
                    ConfirmItemDTO dto = initItemList.get(i);

                    if(dto.isChecked()) {
                        dto.setChecked(false);
                    } else {
                        dto.setChecked(true);
                    }
                }
                listViewDataAdapter.notifyDataSetChanged();
            }
        });

        // Click this button to remove selected items from listview.
        Button rejectButton = (Button)findViewById(R.id.list_reject);
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog alertDialog = new AlertDialog.Builder(ConfirmCheckboxActivity.this).create();
                alertDialog.setMessage("Are you sure to reject the selected match results?");

                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        int size = initItemList.size();
                        for(int i=0;i<size;i++) {
                            ConfirmItemDTO dto = initItemList.get(i);

                            if(dto.isChecked()) {
                                initItemList.remove(i);
                                i--;
                                size = initItemList.size();
                            }
                        }
                        listViewDataAdapter.notifyDataSetChanged();
                    }
                });
                alertDialog.show();
            }
        });

        Button confirmButton = (Button)findViewById(R.id.list_confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog alertDialog = new AlertDialog.Builder(ConfirmCheckboxActivity.this).create();
                alertDialog.setMessage("Are you sure to confirm the selected match results?");

                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        int size = initItemList.size();
                        for(int i=0;i<size;i++) {
                            ConfirmItemDTO dto = initItemList.get(i);

                            if(dto.isChecked()) {
                                initItemList.remove(i);
                                i--;
                                size = initItemList.size();
                            }
                        }
                        listViewDataAdapter.notifyDataSetChanged();
                    }
                });
                alertDialog.show();
            }
        });

    }

    // Return an initialize list of ConfirmItemDTO.
    private List<ConfirmItemDTO> getInitViewItemDtoList()
    {
        String itemTextArr[] = {"\tOpponent: score\n\tUser: score",
                "\tAlex: 300\n\tMark: 0",
                "\tIan: 2\n\tCaleb: 1",
                "other examples", "other examples", "other examples", "other examples", "other examples", "other examples"};

        List<ConfirmItemDTO> ret = new ArrayList<ConfirmItemDTO>();

        int length = itemTextArr.length;

        for(int i=0; i<length; i++) {
            String itemText = itemTextArr[i];

            ConfirmItemDTO dto = new ConfirmItemDTO();
            dto.setChecked(false);
            dto.setItemText(itemText);

            ret.add(dto);
        }
        return ret;
    }
}