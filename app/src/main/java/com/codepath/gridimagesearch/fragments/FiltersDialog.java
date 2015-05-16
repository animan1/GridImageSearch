package com.codepath.gridimagesearch.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.models.Filters;

public class FiltersDialog extends DialogFragment {
  public interface Listener {
    void onFinish(Filters filters);
  }

  public static final String FILTERS = "filters";

  private Spinner sizeSpinner;
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
    initCancelButton(view);
    initSaveButton(view);
    return view;
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
        Filters filters = new Filters(size);
        if (listener != null) {
          listener.onFinish(filters);
        }
        dismiss();
      }
    });
  }

  private void initSizeArraySpinner(View view) {
    sizeSpinner = (Spinner) view.findViewById(R.id.imageSizeSpinner);
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Filters.SIZE_DISPLAY);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    sizeSpinner.setAdapter(adapter);
    if (this.filters.size != null) {
      int position = Filters.SIZE_VALUES.indexOf(this.filters.size);
      sizeSpinner.setSelection(position);
    }
  }
}
