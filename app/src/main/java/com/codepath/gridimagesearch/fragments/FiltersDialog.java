package com.codepath.gridimagesearch.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.models.Filters;

import java.util.List;

public class FiltersDialog extends DialogFragment {


  public interface Listener {
    void onFilterDialogFinish(Filters filters);
  }

  public static final String FILTERS = "filters";

  private Spinner sizeSpinner;
  private Spinner colorSpinner;
  private Spinner typeSpinner;
  private EditText siteEditText;
  private Listener listener;
  private Filters filters;

  public static FiltersDialog newInstance(Filters filters) {
    FiltersDialog frag = new FiltersDialog();
    Bundle args = new Bundle();
    args.putSerializable(FILTERS, filters);
    frag.setArguments(args);
    return frag;
  }

  public void setListener(Listener listener) {
    this.listener = listener;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.filters_dialog, container);
    getDialog().setTitle("Advanced Filters");
    if (getActivity() instanceof Listener) {
      setListener((Listener) getActivity());
    }
    this.filters = (Filters) getArguments().getSerializable(FILTERS);
    initSizeArraySpinner(view);
    initColorArraySpinner(view);
    initTypeArraySpinner(view);
    initSiteEditText(view);
    initCancelButton(view);
    initSaveButton(view);
    return view;
  }

  private void initSiteEditText(View view) {
    siteEditText = (EditText) view.findViewById(R.id.siteFilterEditText);
    if (this.filters.site != null) {
      siteEditText.setText(this.filters.site);
    }
  }

  private void initCancelButton(View view) {
    Button cancelFilterButton = (Button) view.findViewById(R.id.cancelFilterButton);
    cancelFilterButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dismiss();
      }
    });
  }

  private void initSaveButton(View view) {
    Button saveFilterButton = (Button) view.findViewById(R.id.saveFiltersButton);
    saveFilterButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String size = Filters.SIZES.get(sizeSpinner.getSelectedItem().toString());
        String color = Filters.COLORS.get(colorSpinner.getSelectedItem().toString());
        String type = Filters.TYPES.get(typeSpinner.getSelectedItem().toString());
        String site = siteEditText.getText().toString();
        Filters filters = new Filters(size, color, type, site);
        if (listener != null) {
          listener.onFilterDialogFinish(filters);
        }
        dismiss();
      }
    });
  }

  private void initSizeArraySpinner(View view) {
    sizeSpinner = (Spinner) view.findViewById(R.id.imageSizeSpinner);
    initSpinner(sizeSpinner, Filters.SIZE_DISPLAY, Filters.SIZE_VALUES, this.filters.size);
  }

  private void initColorArraySpinner(View view) {
    colorSpinner = (Spinner) view.findViewById(R.id.imageColorSpinner);
    initSpinner(colorSpinner, Filters.COLOR_DISPLAY, Filters.COLOR_VALUES, this.filters.color);
  }

  private void initTypeArraySpinner(View view) {
    typeSpinner = (Spinner) view.findViewById(R.id.imageTypeSpinner);
    initSpinner(typeSpinner, Filters.TYPE_DISPLAY, Filters.TYPE_VALUES, this.filters.type);
  }

  private void initSpinner(Spinner spinner, List<String> displayValues, List<String> canonicalValues, String currentValue) {
    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, displayValues);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
    if (currentValue != null) {
      int position = canonicalValues.indexOf(currentValue);
      spinner.setSelection(position);
    }
  }
}
