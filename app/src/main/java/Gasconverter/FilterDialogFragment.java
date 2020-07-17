package Gasconverter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.gasconverter.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static Gasconverter.MainActivity.filter_brand;
import static Gasconverter.MainActivity.filter_group;

public class FilterDialogFragment extends androidx.fragment.app.DialogFragment {

    List<String> filterResult;
    EditText editText_filter_number;

    public FilterDialogFragment(ArrayList <String> filterResult, EditText editText_filter_number) {
        this.filterResult = filterResult;
        this.editText_filter_number = editText_filter_number;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Группа = " + filter_group + "\nМарка = " + filter_brand);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.select_dialog_singlechoice,filterResult);
        builder.setSingleChoiceItems(adapter2, -1, myClickListener2);
        builder.setPositiveButton("ok", myClickListener2);
        Dialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.dialog_animation_fade;
        return dialog;
    }

    DialogInterface.OnClickListener myClickListener2 = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            ListView lv = ((AlertDialog) dialog).getListView();
            if (which == Dialog.BUTTON_POSITIVE && lv.getCheckedItemPosition()!=-1)
                editText_filter_number.setText(filterResult.get(lv.getCheckedItemPosition()));
        }
    };
}