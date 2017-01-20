package com.app.comic.ui.Activity.SignUp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.app.comic.MainController;
import com.app.comic.R;
import com.app.comic.application.MainApplication;
import com.app.comic.base.BaseFragment;
import com.app.comic.ui.Activity.Login.LoginActivity;
import com.app.comic.ui.Model.Receive.SignDriverReceive;
import com.app.comic.ui.Model.Receive.SignPassengerReceive;
import com.app.comic.ui.Model.Request.SignDriverRequest;
import com.app.comic.ui.Model.Request.SignPassengerRequest;
import com.app.comic.ui.Module.LoginModule;
import com.app.comic.ui.Module.SignDriverModule;
import com.app.comic.ui.Presenter.HomePresenter;
import com.app.comic.ui.Realm.RealmObjectController;
import com.app.comic.utils.DropDownItem;
import com.app.comic.utils.SharedPrefManager;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class SignAsDriverFragment extends BaseFragment implements HomePresenter.SignDriverView, Validator.ValidationListener {

    @Inject
    HomePresenter presenter;

    @InjectView(R.id.btnSignUp)
    Button btnSignUp;

    @NotEmpty(sequence = 1, messageResId = R.string.student_id_empty)
    @InjectView(R.id.txtStudentID)
    EditText txtStudentID;

    @NotEmpty(sequence = 2, messageResId = R.string.email_empty)
    @InjectView(R.id.txtUsername)
    EditText txtUsername;

    @NotEmpty(sequence = 3, messageResId = R.string.password_empty)
    @InjectView(R.id.txtPassword)
    EditText txtPassword;

    @NotEmpty(sequence = 4, messageResId = R.string.phone_empty)
    @InjectView(R.id.txtPhoneNumber)
    EditText txtPhoneNumber;

    @InjectView(R.id.txtSmoker)
    TextView txtSmoker;

    @NotEmpty(sequence = 4, messageResId = R.string.phone_empty)
    @InjectView(R.id.txtPlatNumber)
    EditText txtPlatNumber;

    @NotEmpty(sequence = 4, messageResId = R.string.phone_empty)
    @InjectView(R.id.txtLicenseNumber)
    EditText txtLicenseNumber;

    @InjectView(R.id.txtTypeOfCar)
    TextView txtTypeOfCar;

    private SharedPrefManager pref;
    private Validator mValidator;
    private Activity act;
    private ArrayList<DropDownItem> purposeList = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> carList = new ArrayList<DropDownItem>();

    public static SignAsDriverFragment newInstance() {

        SignAsDriverFragment fragment = new SignAsDriverFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = getActivity();
        MainApplication.get(getActivity()).createScopedGraph(new SignDriverModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.share_ride_sign_as_driver, container, false);
        ButterKnife.inject(this, view);

        purposeList = getSmoker(act);
        carList = getCarType(act);

        txtSmoker.setText(purposeList.get(0).getText());
        txtSmoker.setTag(purposeList.get(0).getCode());

        txtTypeOfCar.setText(carList.get(0).getText());
        txtTypeOfCar.setTag(carList.get(0).getCode());

        txtSmoker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Log.e(purposeList.get(0).getCode().toString(),purposeList.get(1).getCode().toString());
                popupSelection(purposeList, getActivity(), txtSmoker, true, view);
            }
        });

        txtTypeOfCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Log.e(purposeList.get(0).getCode().toString(),purposeList.get(1).getCode().toString());
                popupSelection(carList, getActivity(), txtTypeOfCar, true, view);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mValidator.validate();

            }
        });

        return view;
    }

    @Override
    public void onSignDriverReceive(SignDriverReceive obj) {

        dismissLoading();

        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void onValidationSucceeded() {

        /* Validation Success - Start send data to server */
        SignDriverRequest signDriverRequest = new SignDriverRequest();
        signDriverRequest.setUsername(txtUsername.getText().toString());
        signDriverRequest.setGender("Male");
        signDriverRequest.setPassword(txtPassword.getText().toString());
        signDriverRequest.setPhone(txtPhoneNumber.getText().toString());
        signDriverRequest.setPrefGender("Male");
        signDriverRequest.setSmoker(txtSmoker.getTag().toString());
        signDriverRequest.setStudentID(txtStudentID.getText().toString());
        signDriverRequest.setPlatNumber(txtPlatNumber.getText().toString());
        signDriverRequest.setLicenseNumber(txtLicenseNumber.getText().toString());
        signDriverRequest.setCarType(txtTypeOfCar.getTag().toString());
        presenter.onSignDriverRequest(signDriverRequest);

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        /* Validation Failed - Toast Error */

        //boolean firstView = true;
        for (ValidationError error : errors) {
            View view = error.getView();
            setShake(view);
            view.setFocusable(true);
            view.requestFocus();

            /* Split Error Message. Display first sequence only */
            String message = error.getCollatedErrorMessage(getActivity());
            String splitErrorMsg[] = message.split("\\r?\\n");

            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(splitErrorMsg[0]);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

}
