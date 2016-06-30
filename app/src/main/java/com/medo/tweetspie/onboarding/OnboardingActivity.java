package com.medo.tweetspie.onboarding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;

import com.medo.tweetspie.R;
import com.medo.tweetspie.system.PreferencesInteractor;
import com.medo.tweetspie.system.StringInteractor;
import com.medo.tweetspie.utils.AfterTextChangedWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class OnboardingActivity extends AppCompatActivity implements OnboardingContract.View {

  @BindView(R.id.editUsername)
  EditText editUsername;
  @BindView(R.id.buttonConfirm)
  Button buttonConfirm;

  private OnboardingContract.Actions presenter;

  @NonNull
  public static Intent getIntent(@NonNull Activity parent) {

    return new Intent(parent, OnboardingActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_onboarding);
    ButterKnife.bind(this);

    presenter = new OnboardingPresenter(
            this,
            new PreferencesInteractor(this),
            new StringInteractor(this));

    initViews();
  }

  private void initViews() {

    editUsername.addTextChangedListener(new AfterTextChangedWatcher() {

      @Override
      public void afterTextChanged(Editable s) {

        presenter.onInputChanged();
      }
    });
    presenter.onViewInitialized();
  }

  @Override
  public void finishWithSuccess() {

    setResult(RESULT_OK);
    finish();
  }

  @Override
  public String getUsername() {

    return editUsername.getText().toString();
  }

  @Override
  public void enableConfirmButton(boolean enabled) {

    buttonConfirm.setEnabled(enabled);
  }

  @Override
  public void showError(String message) {

    editUsername.setError(message);
  }

  @OnClick(R.id.buttonConfirm)
  public void onClick() {

    presenter.onConfirmButtonClick();
  }
}
