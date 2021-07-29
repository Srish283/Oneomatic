package com.srishti.oneomatic.Translation;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.srishti.oneomatic.Authentication.LoginActivity;
import com.srishti.oneomatic.Dashboard;
import com.srishti.oneomatic.Database.SessionManager;
import com.srishti.oneomatic.R;
import com.srishti.oneomatic.StepCounter.StepCounterActivity;

import java.util.List;

public class TranslateFragment extends Fragment {


    Toolbar toolbar;
    public static TranslateFragment newInstance() {
        return new TranslateFragment();
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setHasOptionsMenu(true);
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.navigation_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_translate, container, false);
        return root;

    }



    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar=view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.navigation_menu);
        toolbar.setTitle("Translate Plus");

        final ImageButton switchButton = view.findViewById(R.id.buttonSwitchLang);
        final ImageButton logout=view.findViewById(R.id.logout);
        final ImageButton homeBtn=view.findViewById(R.id.homebtn);
        final ToggleButton sourceSyncButton = view.findViewById(R.id.buttonSyncSource);
        final ToggleButton targetSyncButton = view.findViewById(R.id.buttonSyncTarget);
        final TextInputEditText srcTextView = view.findViewById(R.id.sourceText);
        final TextView targetTextView = view.findViewById(R.id.targetText);
        final TextView downloadedModelsTextView = view.findViewById(R.id.downloadedModels);
        final Spinner sourceLangSelector = view.findViewById(R.id.sourceLangSelector);
        final Spinner targetLangSelector = view.findViewById(R.id.targetLangSelector);
        //final ImageView mImageListen =  view.findViewById(R.id.image_listen);           //      Mic button for Speech to text
        // final ImageView mImageSpeak =  view.findViewById(R.id.image_speak);
        final ImageView mClearText =  view.findViewById(R.id.clear_text);



        final TranslateViewModel viewModel = new ViewModelProvider(this).get(TranslateViewModel.class);



        // Get available language list and set up source and target language spinners
        // with default selections.
        final ArrayAdapter<TranslateViewModel.Language> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        viewModel.getAvailableLanguages());
        sourceLangSelector.setAdapter(adapter);
        targetLangSelector.setAdapter(adapter);
        sourceLangSelector.setSelection(adapter.getPosition(new TranslateViewModel.Language("en")));
        targetLangSelector.setSelection(adapter.getPosition(new TranslateViewModel.Language("es")));
        sourceLangSelector.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        setProgressText(targetTextView);
                        viewModel.sourceLang.setValue(adapter.getItem(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        targetTextView.setText("");
                    }
                });
        targetLangSelector.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        setProgressText(targetTextView);
                        viewModel.targetLang.setValue(adapter.getItem(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        targetTextView.setText("");
                    }
                });

        //go to dashboard
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),Dashboard.class);
                getActivity().startActivity(intent);
            }
        });

        //logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("Exit");
                builder.setMessage("Do you want to Logout?").setCancelable(false)
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                SessionManager sessionManager=new SessionManager(getActivity());
                                sessionManager.logoutUser();
                                Intent in=new Intent(getActivity(), LoginActivity.class);
                                startActivity(in);
                                Toast.makeText(getActivity(),"Logged Out",Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                            }
                        });
                // Create the Alert dialog
                AlertDialog alertDialog = builder.create();
                // Show the Alert Dialog box
                alertDialog.show();


            }
        });


        switchButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setProgressText(targetTextView);
                        int sourceLangPosition = sourceLangSelector.getSelectedItemPosition();
                        sourceLangSelector.setSelection(targetLangSelector.getSelectedItemPosition());
                        targetLangSelector.setSelection(sourceLangPosition);
                    }
                });

        //  CLEAR TEXT
        mClearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                srcTextView.setText("");
                targetTextView.setText("");
            }
        });


        // Set up toggle buttons to delete or download remote models locally.
        sourceSyncButton.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        TranslateViewModel.Language language =
                                adapter.getItem(sourceLangSelector.getSelectedItemPosition());
                        if (isChecked) {
                            viewModel.downloadLanguage(language);
                        } else {
                            viewModel.deleteLanguage(language);
                        }
                    }
                });
        targetSyncButton.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        TranslateViewModel.Language language =
                                adapter.getItem(targetLangSelector.getSelectedItemPosition());
                        if (isChecked) {
                            viewModel.downloadLanguage(language);
                        } else {
                            viewModel.deleteLanguage(language);
                        }
                    }
                });

        // Translate input text as it is typed
        srcTextView.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        setProgressText(targetTextView);
                        viewModel.sourceText.postValue(editable.toString());

                    }

                });
        viewModel.translatedText.observe(
                getViewLifecycleOwner(),
                new Observer<TranslateViewModel.ResultOrError>() {
                    @Override
                    public void onChanged(TranslateViewModel.ResultOrError resultOrError) {
                        if (resultOrError.error != null) {
                            srcTextView.setError(resultOrError.error.getLocalizedMessage());
                        } else {
                            targetTextView.setText(resultOrError.result);
                        }
                    }
                });

        // Update sync toggle button states based on downloaded models list.
        viewModel.availableModels.observe(
                getViewLifecycleOwner(),
                new Observer<List<String>>() {
                    @Override
                    public void onChanged(@Nullable List<String> translateRemoteModels) {
                        String output =
                                getContext().getString(R.string.downloaded_models_label, translateRemoteModels);
                        downloadedModelsTextView.setText(output);
                        sourceSyncButton.setChecked(
                                translateRemoteModels.contains(
                                        adapter.getItem(sourceLangSelector.getSelectedItemPosition()).getCode()));
                        targetSyncButton.setChecked(
                                translateRemoteModels.contains(
                                        adapter.getItem(targetLangSelector.getSelectedItemPosition()).getCode()));
                    }
                });



    }

    private void setProgressText(TextView tv) {
        tv.setText(getContext().getString(R.string.translate_progress));
    }



}